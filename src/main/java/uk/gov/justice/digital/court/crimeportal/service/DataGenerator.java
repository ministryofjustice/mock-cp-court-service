package uk.gov.justice.digital.court.crimeportal.service;

import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Component;
import uk.gov.justice.digital.court.crimeportal.data.entity.*;
import uk.gov.justice.digital.court.crimeportal.data.generate.BlockDefinition;
import uk.gov.justice.digital.court.crimeportal.data.generate.CaseCategory;
import uk.gov.justice.digital.court.crimeportal.data.generate.OffenceDefinition;
import uk.gov.justice.digital.court.crimeportal.data.generate.SessionDefinition;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

@Component
public class DataGenerator {
    private final DataDefinitionSupplier dataDefinitionSupplier;
    private final Random random;
    private final List<Map<String, String>> people;



    public DataGenerator(DataDefinitionSupplier dataDefinitionSupplier) {
        this.dataDefinitionSupplier = dataDefinitionSupplier;
        this.random = new Random();
        this.people = readCsv();
    }

    public DocumentType generateOf(String courtName, LocalDate dateOfAppearance) {
        return document(courtName, dateOfAppearance);
    }

    private DocumentType document(String courtName, LocalDate dateOfAppearance) {
        var document = new DocumentType();
        document.setElapsedsecs("10.5");
        document.setEndTime(LocalTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME));
        document.setInfo(info());
        document.setData(data(courtName, dateOfAppearance));
        return document;
    }

    private DataType data(String courtName, LocalDate dateOfAppearance) {
        var data = new DataType();
        data.setJob(job(courtName, dateOfAppearance));
        return data;
    }

    private JobType job(String courtName, LocalDate dateOfAppearance) {
        var job = new JobType();
        job.setPrintdate(LocalDate.now().format(standardDateFormat()));
        job.setUsername("test.user");
        job.setLate("N");
        job.setAdbox("Y");
        job.setMeans("Y");
        job.setSessions(sessions(courtName, dateOfAppearance));
        return job;
    }

    private SessionsType sessions(String courtName, LocalDate dateOfAppearance) {
        var sessions = new SessionsType();
        var sessionDefinition = dataDefinitionSupplier.supplyFor(courtName, dateOfAppearance);
        sessions.getSession().addAll(toSessions(sessionDefinition));
        return sessions;
    }

    private List<SessionType> toSessions(SessionDefinition sessionDefinition) {
        return sessionDefinition
                .getCourtRooms()
                .stream()
                .flatMap(room -> List.of(morningSession(sessionDefinition, room), afternoonSession(sessionDefinition, room)).stream())
                .collect(toList());
    }

    private SessionType afternoonSession(SessionDefinition sessionDefinition, String room) {
        return session(sessionDefinition, room, sessionDefinition::getAfternoon);
    }

    private SessionType morningSession(SessionDefinition sessionDefinition, String room) {
        return session(sessionDefinition, room, sessionDefinition::getMorning);
    }


    private SessionType session(SessionDefinition sessionDefinition, String room, Supplier<Range<LocalTime>> sessionTime) {
        var session = new SessionType();
        session.setCmu(sessionDefinition.getCmu());
        session.setCourt(sessionDefinition.getCourtName());
        session.setDoh(sessionDefinition.getDateOfAppearance().format(standardDateFormat()));
        session.setLja(sessionDefinition.getLocalJusticeArea());
        session.setPanel(sessionDefinition.getPanel());
        session.setRoom(room);
        session.setSId(randomId());
        session.setSstart(sessionTime.get().getMinimum().format(standardTimeFormat()));
        session.setSend(sessionTime.get().getMaximum().format(standardTimeFormat()));
        session.setBlocks(blocksFor(sessionDefinition, sessionTime));
        return session;
    }

    private DateTimeFormatter standardTimeFormat() {
        return DateTimeFormatter.ofPattern("HH:mm");
    }

    private BlocksType blocksFor(SessionDefinition sessionDefinition, Supplier<Range<LocalTime>> sessionTime) {
        var blocks = new BlocksType();
        blocks.getBlock().addAll(
                IntStream.range(1, moreOrLess(sessionDefinition.getBlocksPerSession()))
                        .mapToObj(blockIndex -> blockFor(blockIndex, sessionDefinition, sessionTime.get()))
                        .collect(Collectors.toList()));
        return blocks;
    }

    private BlockType blockFor(int blockIndex, SessionDefinition sessionDefinition, Range<LocalTime> sessionTime) {
        var block = new BlockType();
        block.setBstart(sessionTime.getMinimum().plusMinutes((blockIndex - 1) * around(20, 5) ).format(standardTimeFormat()));
        block.setBend(sessionTime.getMinimum().plusMinutes(((blockIndex - 1) * around(20, 5)) + around(20, 5) ).format(standardTimeFormat()));
        block.setDesc(sessionDefinition.getBlockDefinitions().get(blockIndex).getDescription());
        block.setSbId(randomId());
        block.setCases(casesFor(sessionDefinition.getBlockDefinitions().get(blockIndex)));
        return block;
    }

    private CasesType casesFor(BlockDefinition blockDefinition) {
        var cases = new CasesType();
        cases.getCase().addAll(
                IntStream.range(1, moreOrLess(blockDefinition.getNumberOfCases()))
                .mapToObj(caseIndex -> caseFor(caseIndex, blockDefinition))
                .collect(Collectors.toList()));

        return cases;
    }

    private CaseType caseFor(int caseIndex, BlockDefinition blockDefinition) {
        var aCase = new CaseType();
        var caseCategory = blockDefinition.getCaseCategories().get(moreOrLess(Range.between(0L, (long)blockDefinition.getCaseCategories().size())));
        aCase.setCId(randomId());
        if (chance(10)) {
            aCase.setAddinfo(format("DVLA RESPONSE RECEIVED %s", LocalDate.now().minusDays(around(25, 10)).format(standardDateFormat())));
        }
        if (!List.of(CaseCategory.FINE, CaseCategory.HEALTH_AND_SAFETY, CaseCategory.COUNCIL, CaseCategory.RSPCA, CaseCategory.DRIVING).contains(caseCategory)) {
            aCase.setAsn(format("%04dEE02%010dA", randomUpTo(3000), randomUpTo(10000)));
        }

        if (List.of(CaseCategory.THEFT, CaseCategory.VIOLENCE).contains(caseCategory) && chance(30)) {
            aCase.setBailcond("Assessments/Reports: undergo an assessment conducted by AS DIRECTED for dependency on Class A drugs and participate in any follow-up");
        }

        aCase.setCaseno(format("1900%05d", randomUpTo(100000)));
        if (!List.of(CaseCategory.HEALTH_AND_SAFETY, CaseCategory.COUNCIL, CaseCategory.RSPCA).contains(caseCategory)) {
            aCase.setUrn(format("06AA%07d", randomBetween(70000000, 80000000)));
        }
        aCase.setCseq(String.valueOf(caseIndex));
        aCase.setInf(informantFor(caseCategory));
        aCase.setListno(blockDefinition.isFirstListingOnly() ? "1st" : randomListing());
        aCase.setEstdur(String.valueOf(randomUpTo(15)));
        if (Objects.equals(CaseCategory.VIOLENCE, caseCategory)) {
            if (chance(20)) {
                aCase.setMarker("DV");
            } else if(chance(5)) {
                aCase.setMarker("DVVI");
            }
        }
        aCase.setDefAddr(randomAddress());
        var male = chance(90);
        aCase.setDefName(randomName(male));
        aCase.setDefSex(male ? "M" : "F");
        var dateOfBirth = randomDateOfBirth();
        aCase.setDefDob(dateOfBirth.format(standardDateFormat()));
        aCase.setDefAge(String.valueOf(ChronoUnit.YEARS.between(dateOfBirth.minusMonths(randomUpTo(12)), LocalDate.now())));
        aCase.setValid("Y");
        aCase.setBailcond(randomBailConditions());
        aCase.setOffences(offencesFor(caseCategory, aCase.getListno()));

        if (aCase.getOffences().getOffence().stream().allMatch(OffenceType::isCompany)) {
            aCase.setDefName(randomCompany());
        }
        if (aCase.getOffences().getOffence().stream().allMatch(OffenceType::isCouncil)) {
            aCase.setDefName(randomCouncil());
        }
        return aCase;
    }

    private String randomCouncil() {
        return anyOf("Sheffield City Council, Rotherham District Council", "Barnsley Town Council", "Doncaster Town Council");
    }

    private String randomCompany() {
        var person = randomPerson();
        return String.format("%s Limited", person.get("surname"));
    }

    private DateTimeFormatter standardDateFormat() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }

    private OffencesType offencesFor(CaseCategory caseCategory, String listingsNumber) {
        var offences = new OffencesType();
        switch(caseCategory) {

            case COUNCIL:
                offences.getOffence().addAll(someCases(caseCategory, listingsNumber, 1, randomPlea()));
                break;
            case FINE:
            case RSPCA:
            case NPS:
            case DRIVING:
                offences.getOffence().addAll(someCases(caseCategory, listingsNumber, 2, randomPlea()));
                break;
            case SEX:
                offences.getOffence().addAll(someCases(caseCategory, listingsNumber, 20, randomPlea()));
                break;
            case VIOLENCE:
            case THEFT:
            case GENERAL:
            case HEALTH_AND_SAFETY:
                offences.getOffence().addAll(someCases(caseCategory, listingsNumber, 4, randomPlea()));
                break;
        }
        return offences;
    }

    private List<OffenceType> someCases(CaseCategory caseCategory, String listingsNumber, int maxOffences, String plea) {
        return IntStream.range(1, randomBetween(2, maxOffences + 2)).mapToObj(offenceIndex -> offenceFor(offenceIndex, caseCategory, listingsNumber, plea)).collect(toList());
    }

    private OffenceType offenceFor(int offenceIndex, CaseCategory caseCategory, String listingsNumber, String plea) {
        var offence = offenceFor(caseCategory);
        offence.setCoId(randomId());
        offence.setOseq(String.valueOf(offenceIndex));
        if (!Objects.equals(listingsNumber, "1st")) {
            offence.setAdjdate(LocalDate.now().minusDays(randomUpTo(30)).format(standardDateFormat()));
            offence.setAdjreason(randomAdjournmentReason());
            offence.setPleadate(LocalDate.now().minusDays(randomUpTo(15)).format(standardDateFormat()));
            offence.setPlea(plea);
        }
        offence.setMaxpen(randomMaxPenalty());
        return offence;
    }

    private String randomId() {
        return String.valueOf(Math.abs(random.nextLong()));
    }

    private OffenceType offenceFor(CaseCategory caseCategory) {
        var offence =  new OffenceType();
        var offenceDefinition = anyFrom(dataDefinitionSupplier.supplyAllFor(caseCategory));
        var replacements = offenceDefinition.getFactTypes().stream().map(factType -> ImmutablePair.of(factType, randomValueOf(factType))).collect(toList());
        offence.setAs(offenceDefinition.getContraryToActAndSection());
        offence.setCode(offenceDefinition.getCode());
        offence.setTitle(offenceDefinition.getTitle());
        offence.setSum(replaceVariables(replacements, offenceDefinition.getSummary()));
        offence.setSof(replaceVariables(replacements, offenceDefinition.getStatementOfFact()));
        offence.setCouncil(offenceDefinition.isCouncil());
        offence.setCompany(offenceDefinition.isCompany());
        return offence;
    }

    private String replaceVariables(List<ImmutablePair<OffenceDefinition.FactType, String>> replacements, String maybeValue) {
        return Optional.ofNullable(maybeValue).map(value -> replacements.stream()
                .reduce(value
                        , (updatedValue, pair) -> updatedValue.replaceAll(format("\\$\\{%s\\}", pair.getLeft().name()), pair.getRight())
                        , (identity, t) -> {
                            throw new IllegalStateException("Can't be used in parallel stream");
                        })).orElse(null);
    }

    private String randomValueOf(OffenceDefinition.FactType factType) {
        var person = randomPerson();
        switch (factType) {
            case NAME:
                return format("%s %s", person.get("firstName"), person.get("surname")).toUpperCase();
            case CAR_REGISTRATION:
                return format("%s%d%s", randomLetters(2), randomBetween(11, 99), randomLetters(1));
            case DATE:
                return LocalDate.now().minusDays(randomUpTo(365)).format(standardDateFormat());
            case TIME:
                return LocalTime.of(randomBetween(0, 23), randomBetween(0, 59)).format(standardTimeFormat());
            case DATE_RANGE_FIRST:
                return LocalDate.now().minusDays(randomBetween(151, 365)).format(standardDateFormat());
            case DATE_RANGE_SECOND:
                return LocalDate.now().minusDays(randomBetween(10, 150)).format(standardDateFormat());
            case CAR_TYPE:
                return anyOf("BMW", "MINI", "MERCEDES", "JEEP", "DATSUN", "LADA", "FIAT");
            case SPEED:
                return String.valueOf(randomBetween(75, 105));
            case SPEED_LIMIT:
                return anyOf("50", "60", "70");
            case ROAD_LOCATION:
                return anyOf("M18-2234A J12 J13 Northbound", "M1-6363A J23 J27 Northbound", "M3-2234A J4 J6 Southbound");
            case PLACE:
                return format("%s %s", person.get("town"), person.get("county")).toUpperCase();
            case TOWN:
                return person.get("town");
            case POSTCODE:
                return person.get("postcode");
            case COUNTY:
                return person.get("county");
            case AGE:
                return String.valueOf(randomBetween(7, 13));
            case SCHOOL:
                return format("%s high school", person.get("town")).toUpperCase();
            case AMOUNT:
                return format("£%d", randomBetween(50, 1000));
            case NUMBER:
                return format("%d", randomBetween(1, 100));
            case SHOP:
                return anyOf("M&S", "TESCO", "DEBENHAMS", "BOOTS", "PC WORLD", "H&M", "PRIMARK");
            case COURT:
                return format("%s Magistrates Court", person.get("town"));
            case SENTENCE_DATE:
                return LocalDate.now().minusDays(randomBetween(200, 600)).format(standardDateFormat());
            case FOOD_LOCATION:
                return format("The %s Restaurant", person.get("surname"));
        }
        return "";
    }

    private int randomBetween(int start, int end) {
        return randomUpTo(end - start) + start;
    }

    private String randomLetters(int numberOfLetters) {
        return IntStream.range(0, numberOfLetters).mapToObj(number -> String.valueOf((char)('A' + randomUpTo(25)) )).collect(Collectors.joining());
    }

    private String randomMaxPenalty() {
        return anyOf("S: L3", "EW: 6M &amp;/or Ultd Fine");
    }

    private String randomPlea() {
        return anyOf("GSJP", "G", "NG");
    }

    private String randomAdjournmentReason() {
        return anyOf("for consideration of advance information",
                "for a medical or psychiatric report to be prepared; for a pre-sentence report to be prepared. [1]",
                "for legal advice or representation; for legal representation; to attend or warrant to issue; for the defendant to have a further opportunity to attend; the defendant is in custody on other matters; to",
                "for a bail application to be made",
                "to produce evidence of means",
                "to appear or the case may proceed",
                "for an interpreter [1]; at request of the defence [1]");
    }

    private String randomBailConditions() {
        if (chance(10)) {
            return "Assessments/Reports: undergo an assessment conducted by AS DIRECTED for dependency on Class A drugs and participate in any follow-up";
        }

        if (chance(10)) {
            var person1 = randomPerson();
            var person2 = randomPerson();
            return format("ng cond: Exclusion: not to enter %s %s ; Residence: live and sleep each night at %s %s", person1.get("line1"), person1.get("town"), person2.get("line1"), person2.get("town"));
        }
        return null;
    }

    private LocalDate randomDateOfBirth() {
        return LocalDate.now().minusDays(randomUpTo(365 * 50) + 18 * 365);
    }

    private String randomName(boolean male) {
        var person = randomPerson();
        var name = new StringBuilder();

        if (chance(60)) {
            if (male) {
                name.append("MR ");
            } else {
                name.append("MS ");
            }
        }
        name.append(person.get("firstName"));
        name.append(" ");
        if (chance(50)) {
            var anotherPerson = randomPerson();
            name.append(anotherPerson.get("firstName"));
            name.append(" ");
        }
        name.append(person.get("surname").toUpperCase());
        if (chance(20)) {
            return name.toString().toUpperCase();
        }
        return name.toString();
    }

    private DefAddrType randomAddress() {
        var address = new DefAddrType();
        var person = randomPerson();
        address.setLine1(person.get("line1"));
        address.setLine2(person.get("town"));
        address.setLine3(person.get("county"));
        address.setPcode(person.get("postcode"));
        return address;
    }

    private String randomListing() {
        return anyOf("1st", "2nd", "3rd", "4th");
    }
    private String anyOf(String... values) {
        return values[randomUpTo(values.length)];
    }
    private <T> T anyFrom(List<T> values) {
        return values.get(randomUpTo(values.size()));
    }

    private String informantFor(CaseCategory caseCategory) {
        switch (caseCategory) {
            case NPS:
                return "SHEFNPS";
            case RSPCA:
                return "RSPCA";
            case COUNCIL:
                return "SHRATD";
            default:
                return "SHEFPOL";
        }
    }

    private int moreOrLess(Range<Long> range) {
        return (int)random.longs(range.getMinimum(), range.getMaximum()).findAny().orElseThrow();
    }

    private long around(long number, long offset) {
        return (int)random.longs(number - offset, number + offset).findAny().orElseThrow();
    }

    private InfoType info() {
        var info = new InfoType();
        info.setGeneral(general());
        info.setParameters(parameters());
        info.setStartTime(LocalTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME));
        info.setPrinters("");
        return info;
    }

    private ParametersType parameters() {
        var parameters  = new ParametersType();
        parameters.setPiMccId("1");
        parameters.setPiReportType("F");
        parameters.setPiChId("82");
        parameters.setPiChId("127");
        parameters.setPiDate1("01-SEP-16");
        parameters.setPiDate1("30-SEP-16");
        parameters.setPiSessionTypes("AM,PM,ALL");
        parameters.setPiCrIdList("652,653,654,655,656,657,658,659,660,661,662,1724,1725,1726,1727,1728,1729,1730,1731,1704,1744,1745,1746,1764");
        parameters.setPiResequence("Y");
        parameters.setPiLateEntry("N");
        parameters.setPiMeansRegister("Y");
        parameters.setPiAdjudicationBox("Y");
        return parameters;
    }

    private GeneralType general() {
        var general = new GeneralType();
        general.setDocType("FullCourtList");
        general.setOutputtype("R");
        general.setVersion("2_2");
        general.setDocref("2669");
        general.setSystem("L");
        general.setPreviewText("");
        general.setAlertsAddress("");
        general.setErmFlag("");
        return general;
    }

    private boolean chance(int percent) {
        return randomUpTo(100) < percent;
    }
    private int randomUpTo(int bound) {
        return random.nextInt(bound);
    }

    private List<Map<String, String>> readCsv() {
        return new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/uk-500.csv")))
                .lines()
                .map(line -> line.split("\",\""))
                .map(part -> Arrays.stream(part).map(item -> item.replace("\"", ""))
                        .collect(Collectors.toList()))
                .map(this::toPersonMap)
                .collect(Collectors.toList());
    }

    private Map<String, String> toPersonMap(List<String> row) {
        // "first_name","last_name","company_name","address","city","county","postal","phone1","phone2","email","web"

        final HashMap<String, String> offenderMap = new HashMap<>();
        offenderMap.put("firstName", row.get(0));
        offenderMap.put("surname", row.get(1));
        offenderMap.put("line1", row.get(3));
        offenderMap.put("town", row.get(4));
        offenderMap.put("county", row.get(5));
        offenderMap.put("postcode", row.get(6));
        offenderMap.put("telephoneNumber", row.get(7));
        offenderMap.put("emailAddress", row.get(9));
        return offenderMap;
    }

    private Map<String, String> randomPerson() {
        return people.get(randomUpTo(people.size()));
    }



}
