package uk.gov.justice.digital.court.crimeportal.service;

import org.apache.commons.lang3.Range;
import org.springframework.stereotype.Component;
import uk.gov.justice.digital.court.crimeportal.data.generate.BlockDefinition;
import uk.gov.justice.digital.court.crimeportal.data.generate.CaseCategory;
import uk.gov.justice.digital.court.crimeportal.data.generate.OffenceDefinition;
import uk.gov.justice.digital.court.crimeportal.data.generate.SessionDefinition;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Map.entry;
import static uk.gov.justice.digital.court.crimeportal.data.generate.CaseCategory.COUNCIL;
import static uk.gov.justice.digital.court.crimeportal.data.generate.CaseCategory.RSPCA;
import static uk.gov.justice.digital.court.crimeportal.data.generate.OffenceDefinition.FactType.*;

@Component
public class DataDefinitionSupplier {
    private Map<CaseCategory, List<OffenceDefinition>> offenceDefinitionMap;

    public DataDefinitionSupplier() {
        this.offenceDefinitionMap = offenceDefinitions();
    }

    public SessionDefinition supplyFor(String courtName, LocalDate dateOfAppearance) {
        return SessionDefinition
                .builder()
                .morning(Range.between(LocalTime.of(10, 0), LocalTime.of(12, 0)))
                .afternoon(Range.between(LocalTime.of(13, 0), LocalTime.of(16, 0)))
                .cmu("Sheffield CMU")
                .localJusticeArea("South Yorkshire Magistrates' Court")
                .panel("South Yorkshire Adult Panel")
                .courtRooms(IntStream.range(1, 12).mapToObj(room -> String.format("%02d", room)).collect(Collectors.toList()))
                .courtName(courtName)
                .dateOfAppearance(dateOfAppearance)
                .blocksPerSession(Range.between(4L, 9L))
                .blockDefinitions(blockDefinitions())
                .build();
    }

    private List<BlockDefinition> blockDefinitions() {
        return List.of(
                BlockDefinition
                        .builder()
                        .description("Not Guilty Anticipated Plea")
                        .caseCategories(List.of(CaseCategory.VIOLENCE, CaseCategory.DRIVING, CaseCategory.THEFT, CaseCategory.GENERAL))
                        .numberOfCases(Range.between(2L, 6L))
                        .build(),
                BlockDefinition
                        .builder()
                        .description("Guilty Anticipated Plea")
                        .caseCategories(List.of(CaseCategory.VIOLENCE, CaseCategory.DRIVING, CaseCategory.THEFT, CaseCategory.GENERAL))
                        .numberOfCases(Range.between(4L, 8L))
                        .firstListingOnly(true)
                        .build(),
                BlockDefinition
                        .builder()
                        .description("Pre-Sentence Report")
                        .caseCategories(List.of(CaseCategory.VIOLENCE, CaseCategory.DRIVING, CaseCategory.THEFT, CaseCategory.GENERAL))
                        .numberOfCases(Range.between(3L, 5L))
                        .build(),
                BlockDefinition
                        .builder()
                        .description("Fines")
                        .caseCategories(List.of(CaseCategory.FINE))
                        .numberOfCases(Range.between(6L, 10L))
                        .build(),
                BlockDefinition
                        .builder()
                        .description("Adult General Work")
                        .caseCategories(List.of(CaseCategory.VIOLENCE, CaseCategory.THEFT, CaseCategory.GENERAL))
                        .numberOfCases(Range.between(5L, 9L))
                        .build(),
                BlockDefinition
                        .builder()
                        .description("Road traffic - Disq")
                        .caseCategories(List.of(CaseCategory.DRIVING))
                        .numberOfCases(Range.between(3L, 6L))
                        .build(),
                BlockDefinition
                        .builder()
                        .description("Trials - Criminal")
                        .caseCategories(List.of(CaseCategory.VIOLENCE, CaseCategory.THEFT, CaseCategory.GENERAL, CaseCategory.SEX))
                        .numberOfCases(Range.between(5L, 9L))
                        .build(),
                BlockDefinition
                        .builder()
                        .description("Private Prosecutions")
                        .caseCategories(List.of(CaseCategory.HEALTH_AND_SAFETY, RSPCA, COUNCIL))
                        .numberOfCases(Range.between(2L, 5L))
                        .build()
        );
    }

    public List<OffenceDefinition> supplyAllFor(CaseCategory caseCategory) {
        return offenceDefinitionMap.get(caseCategory);
    }

    private Map<CaseCategory, List<OffenceDefinition>> offenceDefinitions() {
        return Map.ofEntries(
                entry(CaseCategory.COUNCIL, List.of(
                        OffenceDefinition
                                .builder()
                                .code("ED96001")
                                .title("Parent of child of compulsory school age registered at a school who failed to attend regularly")
                                .contraryToActAndSection("Contrary to section 444(1) and (8) of the Education Act 1996.")
                                .summary("Between ${DATE_RANGE_FIRST} ${DATE_RANGE_SECOND} at ${PLACE} you were the parent of ${NAME} a child of compulsory school age, namely ${AGE}, who was registered at ${SCHOOL} and failed to attend regularly at that school.")
                                .statementOfFact("Between ${DATE_RANGE_FIRST} ${DATE_RANGE_SECOND} at ${PLACE} you were the parent of ${NAME} a child of compulsory school age, namely ${AGE}, who was registered at ${SCHOOL} and failed to attend regularly at that school.")
                                .factTypes(List.of(DATE_RANGE_FIRST, DATE_RANGE_SECOND, PLACE, NAME, AGE, SCHOOL ))
                                .build(),
                        OffenceDefinition
                                .builder()
                                .code("CT92501")
                                .title("Complaint for council tax liability order")
                                .contraryToActAndSection("In accordance with regulation 34 of the Council Tax (Administration and Enforcement) Regulations 1992.")
                                .summary("Complaint for a liability order for non-payment of council tax and costs of ${AMOUNT} ${PLACE}")
                                .factTypes(List.of(AMOUNT, PLACE ))
                                .company(true)
                                .build(),
                        OffenceDefinition
                                .builder()
                                .code("LM76504")
                                .title("Appeal against decision to suspend/revoke/refuse to renew a hackney carriage/private hire driver's licence")
                                .contraryToActAndSection("In accordance with Section 61 of the Local Government (Miscellaneous Provisions) Act 1976")
                                .summary("Appeal against the decision made on ${DATE} to revoke a hackney carriage driver's licence.")
                                .factTypes(List.of(DATE))
                                .council(true)
                                .build()
                )),
                entry(CaseCategory.FINE, List.of(
                        OffenceDefinition
                                .builder()
                                .code("MC80704")
                                .title("Non-payment of fine, CT/Account No.: 124/13018881U")
                                .contraryToActAndSection("Magistrates' Courts Act 1980, section 83")
                                .summary("Non-payment of fine of ${AMOUNT} imposed on ${DATE}")
                                .factTypes(List.of(AMOUNT, DATE ))
                                .build()

                )),
                entry(CaseCategory.SEX, List.of(
                        OffenceDefinition
                                .builder()
                                .code("SX03013")
                                .title("Rape a girl under 13")
                                .contraryToActAndSection("Contrary to section 5 of the Sexual Offences Act 2003")
                                .summary("Between ${DATE_RANGE_FIRST} ${DATE_RANGE_SECOND} at ${PLACE} intentionally penetrated the anus of a girl under the age of 13 with your penis")
                                .factTypes(List.of(DATE_RANGE_FIRST, DATE_RANGE_SECOND, PLACE ))
                                .build(),
                        OffenceDefinition
                                .builder()
                                .code("SX03013A")
                                .title("Attempt rape of a girl under 13 - SOA 2003")
                                .contraryToActAndSection("Contrary to section 1(1) of the Criminal Attempts Act 1981")
                                .summary("Between ${DATE_RANGE_FIRST} ${DATE_RANGE_SECOND} at ${PLACE} intentionally attempted to penetrate the anus of a girl under the age of 13 with your penis")
                                .factTypes(List.of(DATE_RANGE_FIRST, DATE_RANGE_SECOND, PLACE ))
                                .build(),
                        OffenceDefinition
                                .builder()
                                .code("SX03017")
                                .title("Assault a girl under 13")
                                .contraryToActAndSection("Contrary to section 7 of the Sexual Offences Act 2003.")
                                .summary("Between ${DATE_RANGE_FIRST} ${DATE_RANGE_SECOND} at ${PLACE}  you intentionally touched a girl under the age of 13, namely 11 or 12 years and the touching was sexual")
                                .factTypes(List.of(DATE_RANGE_FIRST, DATE_RANGE_SECOND, PLACE ))
                                .build(),
                        OffenceDefinition
                                .builder()
                                .code("SX03015")
                                .title("Assault a girl under 13 by penetration with a part of your body / a thing SOA 2003")
                                .contraryToActAndSection("Contrary to section 5 of the Sexual Offences Act 2003")
                                .summary("Between ${DATE_RANGE_FIRST} ${DATE_RANGE_SECOND} at ${PLACE} you intentionally penetrated the vagina of a girl under the age of 13, with a part of you namely your fingers and that penetration was sexual")
                                .factTypes(List.of(DATE_RANGE_FIRST, DATE_RANGE_SECOND, PLACE ))
                                .build(),
                        OffenceDefinition
                                .builder()
                                .code("SX03015")
                                .title("Assault a girl under 13 by penetration with a part of your body / a thing SOA 2003")
                                .contraryToActAndSection("Contrary to section 5 of the Sexual Offences Act 2003")
                                .summary("Between ${DATE_RANGE_FIRST} ${DATE_RANGE_SECOND} at ${PLACE} you intentionally penetrated the vagina of a girl under the age of 13, with a part of you namely your tongue and that penetration was sexual")
                                .factTypes(List.of(DATE_RANGE_FIRST, DATE_RANGE_SECOND, PLACE ))
                                .build(),
                        OffenceDefinition
                                .builder()
                                .code("SX03019")
                                .title("Cause / incite a girl under 13 to engage in sexual activity - no penetration")
                                .contraryToActAndSection("Contrary to section 5 of the Sexual Offences Act 2003")
                                .summary("Between ${DATE_RANGE_FIRST} ${DATE_RANGE_SECOND} at ${PLACE} least ${NUMBER} occasions you intentionally cause or incite a girl under the age")
                                .factTypes(List.of(DATE_RANGE_FIRST, DATE_RANGE_SECOND, PLACE, NUMBER ))
                                .build(),
                        OffenceDefinition
                                .builder()
                                .code("SX03068")
                                .title("Adult sexual activity with a female child family member 13 to 17 - penetration")
                                .contraryToActAndSection("Contrary to section 25(1)(a)-(d). (e)() and (5) of the Sexual Offences Act 2003.")
                                .summary("Between ${DATE_RANGE_FIRST} ${DATE_RANGE_SECOND} at ${PLACE} least ${NUMBER}, being a person whose relation to person aged under 16 was within section of the Sexual Offences Act 2003, namely brother and sister, and you knew or could reasonably be expected to have known was of a description falling within that " +
                                        "and not reasonably believing she was aged 18 or over, Intentionally touched her and the touching was sexual")
                                .factTypes(List.of(DATE_RANGE_FIRST, DATE_RANGE_SECOND, PLACE, NUMBER ))
                                .build(),
                        OffenceDefinition
                                .builder()
                                .code("SX03076")
                                .title("Offender under 18 incite sexual activity with a family member-victim girl 13 to 17-no penetration SOA 2003")
                                .contraryToActAndSection("Contrary to section 25(1)(a)-(d). (e)() and (5) of the Sexual Offences Act 2003.")
                                .summary("Between ${DATE_RANGE_FIRST} ${DATE_RANGE_SECOND} at ${PLACE} least ${NUMBER} occasions, being a person whose relation to a person aged under 16 was within section " +
                                        "of the Sexual Offences Act 2003, namely brother and sister, and you knew or could reasonably be expected to have known was of a description falling within that " +
                                        "and not reasonably believing she was aged 18 or over, Intentionally touched her and the touching was sexual")
                                .factTypes(List.of(DATE_RANGE_FIRST, DATE_RANGE_SECOND, PLACE, NUMBER ))
                                .build(),
                        OffenceDefinition
                                .builder()
                                .code("SX03074")
                                .title("Adult incite sexual activity with a family member-victim girl 13 to 17-penetration- SOA 2003")
                                .contraryToActAndSection("Contrary to section 25(1)(a)-(d). (e)() and (5) of the Sexual Offences Act 2003.")
                                .summary("Between ${DATE_RANGE_FIRST} ${DATE_RANGE_SECOND} at ${PLACE} least ${NUMBER} occasions, being a person whose relation to a person aged under 16 was within section " +
                                        "of the Sexual Offences Act 2003, namely brother and sister, and you knew or could reasonably be expected to have known was of a description falling within that " +
                                        "and not reasonably believing she was aged 18 or over, Intentionally touched her and the touching was sexual")
                                .factTypes(List.of(DATE_RANGE_FIRST, DATE_RANGE_SECOND, PLACE, NUMBER ))
                                .build(),
                        OffenceDefinition
                                .builder()
                                .code("SX03066")
                                .title("Sexual activity with a female child family member 13 to 17 offender under 18")
                                .contraryToActAndSection("Contrary to section 25(1)(a)-(d). (e)() and (5) of the Sexual Offences Act 2003.")
                                .summary("Between ${DATE_RANGE_FIRST} ${DATE_RANGE_SECOND} at ${PLACE} least ${NUMBER} occasions, being a person whose relation to a person aged under 16 was within section " +
                                        "of the Sexual Offences Act 2003, namely brother and sister, and you knew or could reasonably be expected to have known was of a description falling within that " +
                                        "and not reasonably believing she was aged 18 or over, Intentionally touched her and the touching was sexual")
                                .factTypes(List.of(DATE_RANGE_FIRST, DATE_RANGE_SECOND, PLACE, NUMBER ))
                                .build(),
                        OffenceDefinition
                                .builder()
                                .code("SX03068")
                                .title("Adult sexual activity with a female child family member 13 to 17 - penetration")
                                .contraryToActAndSection("Contrary to section 25(1)(a)-(d). (e)() and (5) of the Sexual Offences Act 2003.")
                                .summary("Between ${DATE_RANGE_FIRST} ${DATE_RANGE_SECOND} at ${PLACE} least ${NUMBER} occasions, being a person whose relation to a person aged under 16 was within section " +
                                        "of the Sexual Offences Act 2003, namely brother and sister, and you knew or could reasonably be expected to have known was of a description falling within that " +
                                        "and not reasonably believing she was aged 18 or over, Intentionally touched her and the touching was sexual")
                                .factTypes(List.of(DATE_RANGE_FIRST, DATE_RANGE_SECOND, PLACE, NUMBER ))
                                .build(),
                        OffenceDefinition
                                .builder()
                                .code("SX03008")
                                .title("Sexual assault on a male")
                                .contraryToActAndSection("Contrary to section 3 of the Sexual Offences Act 2003.")
                                .summary("Between ${DATE_RANGE_FIRST} ${DATE_RANGE_SECOND} at ${PLACE}  intentionally touched a man aged 16 or over and that touching was sexual when he did not consent and you did not reasonably believe that he was consenting.")
                                .factTypes(List.of(DATE_RANGE_FIRST, DATE_RANGE_SECOND, PLACE ))
                                .build(),
                        OffenceDefinition
                                .builder()
                                .code("SX03010")
                                .title("Cause a male 13 or over to engage in a non penetrative sexual activity - SOA 2003")
                                .contraryToActAndSection("Contrary to section 4(1) and (5) of the Sexual Offences Act 2003.")
                                .summary("Between ${DATE_RANGE_FIRST} ${DATE_RANGE_SECOND} at ${PLACE}  with intent caused a man aged 16 or over to engage in sexual activity of a non penetrative nature, namely grabbing the A/Ps hand and placing it on your penis, when he did not consent and you did not reasonably believe that he was")
                                .factTypes(List.of(DATE_RANGE_FIRST, DATE_RANGE_SECOND, PLACE ))
                                .build()


                )),
                entry(CaseCategory.DRIVING, List.of(
                        OffenceDefinition
                                .builder()
                                .code("RR84113")
                                .title("Exceed a variable speed limit - automatic camera device")
                                .contraryToActAndSection("Contrary to the above regulations and section 17(2), (3) and (4) of the Road Traffic Regulation Act 1984 and Schedule 2 to the Road Traffic Offenders Act 1988")
                                .summary("On ${DATE} at ${TOWN} in the county of ${COUNTY} drove a mechanically propelled vehicle, being a vehicle, namely ${CAR_TYPE} index ${CAR_REGISTRATION}, on a" +
                                        " special road, namely ${ROAD_LOCATION}, subject of regulations, namely The ${ROAD_LOCATION} (Variable Speed Limits) Regulations 2015, at a speed exceeding ${SPEED_LIMIT} miles per hour")
                                .statementOfFact("At ${TIME} on ${DATE} the defendant drove a mechanically propelled vehicle, being a vehicle, namely ${CAR_TYPE} index ${CAR_REGISTRATION}, on a special road, namely" +
                                        " ${ROAD_LOCATION}, subject of regulations, namely The ${ROAD_LOCATION} (Variable Speed Limits) Regulations 2015, at a speed" +
                                        " exceeding ${SPEED_LIMIT} miles per hour. The speed, recorded by means of HAD20495 Highways Agency Digital Enforcement Camera System 9, was ${SPEED} mph.")
                                .factTypes(List.of(DATE, TOWN, COUNTY, CAR_TYPE, CAR_REGISTRATION, ROAD_LOCATION, SPEED, SPEED_LIMIT, TIME ))
                                .build(),
                        OffenceDefinition
                                .builder()
                                .code("RT88007")
                                .title("Drive motor vehicle when alcohol level above limit")
                                .contraryToActAndSection("Contrary to section (1)(a) of the Road Traffic Act 1988 and Schedule 2 to the Road Traffic Offenders Act 1988.")
                                .summary("On ${DATE} at ${TOWN} in the county of ${COUNTY} drove a motor vehicle, namely  ${CAR_TYPE} ${CAR_REGISTRATION}on a public place, namely ${ROAD_LOCATION}, after consuming so much alcohol that the proportion of it in your breath, namely ${NUMBER} micrograms of alcohol in 100 millilitres of breath, exceeded the prescribed limit")
                                .factTypes(List.of(DATE, TOWN, COUNTY, CAR_TYPE, CAR_REGISTRATION, ROAD_LOCATION, NUMBER))
                                .build(),
                        OffenceDefinition
                                .builder()
                                .code("RT88578")
                                .title("Fail to stop a mechanically propelled vehicle when required by constable / traffic warden")
                                .contraryToActAndSection("Contrary to section 163(3) of the Road Traffic Act 1988 and schedule 2 to the Road Traffic Offenders Act 1988.")
                                .summary("On ${DATE} at ${TOWN} in the county of ${COUNTY} being the driver of a mechanically propelled vehicle namely ${CAR_TYPE} ${CAR_REGISTRATION} owing to the presence of which on a road, namely ${ROAD_LOCATION} an accident occurred whereby damage was caused to property forming part of the land on which the road was situated or land adjacent to it, namely Traffic Sign, failed to stop.")
                                .factTypes(List.of(DATE, TOWN, COUNTY, CAR_TYPE, CAR_REGISTRATION, ROAD_LOCATION))
                                .build(),
                        OffenceDefinition
                                .builder()
                                .code("RT88218")
                                .title("Driver of a vehicle fail to stop after a road accident")
                                .contraryToActAndSection("Contrary to section 170(4) of the Road Traffic Act 1988 and Schedule 2 to the Road Traffic Offenders Act 1988")
                                .summary("On ${DATE} at ${TOWN} in the county of ${COUNTY} being the driver of a mechanically propelled vehicle, namely ${CAR_TYPE} ${CAR_REGISTRATION} on a public place, namely ${ROAD_LOCATION}, failed to stop the vehicle on being required to do so by a constable in uniform")
                                .factTypes(List.of(DATE, TOWN, COUNTY, CAR_TYPE, CAR_REGISTRATION, ROAD_LOCATION ))
                                .build(),
                        OffenceDefinition
                                .builder()
                                .code("RT88191")
                                .title("Use a motor vehicle on a road  public place without third party insurance")
                                .contraryToActAndSection("Contrary to section 143 of the Road Traffic Act 1988 and Schedule 2 to the Road Traffic Offenders Act 1988.")
                                .summary("On ${DATE} at ${TOWN} in the county of ${COUNTY} used a motor vehicle, namely ${CAR_TYPE} ${CAR_REGISTRATION} on a public place, namely ${ROAD_LOCATION}, when there was not in force in relation to that use such a policy of insurance or such a security in respect of third party risks as complied with the requirements of Part VI of the Road Traffic Act 1988.")
                                .factTypes(List.of(DATE, TOWN, COUNTY, CAR_TYPE, CAR_REGISTRATION, ROAD_LOCATION ))
                                .build(),
                        OffenceDefinition
                                .builder()
                                .code("RT88334")
                                .title("Drive a motor vehicle otherwise than in accordance with a licence - endorsable offence")
                                .contraryToActAndSection("Contrary to section 87(1) of the Road Traffic Act 1988 and Schedule 2 to the Road Traffic Offenders Act 1988.")
                                .summary("On ${DATE} at ${TOWN} in the county of ${COUNTY} drove a motor vehicle, namely ${CAR_TYPE} ${CAR_REGISTRATION} on a road, namely ${ROAD_LOCATION}, otherwise than in accordance with a licence authorising you to drive a motor vehicle of that class.")
                                .factTypes(List.of(DATE, TOWN, COUNTY, CAR_TYPE, CAR_REGISTRATION, ROAD_LOCATION ))
                                .build(),
                        OffenceDefinition
                                .builder()
                                .code("RT88334")
                                .title("Drive a motor vehicle otherwise than in accordance with a licence - endorsable offence")
                                .contraryToActAndSection("Contrary to section 87(1) of the Road Traffic Act 1988 and Schedule 2 to the Road Traffic Offenders Act 1988.")
                                .summary("On ${DATE} at ${TOWN} in the county of ${COUNTY} drove a motor vehicle, namely ${CAR_TYPE} ${CAR_REGISTRATION} on a road, namely ${ROAD_LOCATION}, otherwise than in accordance with a licence authorising you to drive a motor vehicle of that class.")
                                .factTypes(List.of(DATE, TOWN, COUNTY, CAR_TYPE, CAR_REGISTRATION, ROAD_LOCATION ))
                                .build()
                )),
                entry(CaseCategory.VIOLENCE, List.of(
                        OffenceDefinition
                                .builder()
                                .code("CJ88116")
                                .title("Assault by beating")
                                .contraryToActAndSection("Contrary to section 39 of the Criminal Justice Act 1988.")
                                .summary("On ${DATE} at ${TOWN} in the county of ${COUNTY} assaulted ${NAME} by beating her")
                                .factTypes(List.of(DATE, TOWN, COUNTY, NAME ))
                                .build(),
                        OffenceDefinition
                                .builder()
                                .code("CJ88001")
                                .title("Common assault")
                                .contraryToActAndSection("Contrary to section 39 of the Criminal Justice Act 1988.")
                                .summary("On ${DATE} at ${TOWN} in the county of ${COUNTY} assaulted ${NAME}")
                                .factTypes(List.of(DATE, TOWN, COUNTY, NAME ))
                                .build(),
                        OffenceDefinition
                                .builder()
                                .code("OF61102")
                                .title("Assault a person thereby occasioning them actual bodily harm")
                                .contraryToActAndSection("Contrary to section 47 of the Offences Against the Person Act 1861.")
                                .summary("On ${DATE} at ${TOWN} in the county of ${COUNTY} assaulted ${NAME} , thereby occasioning her actual bodily harm.")
                                .factTypes(List.of(DATE, TOWN, COUNTY, NAME ))
                                .build(),
                        OffenceDefinition
                                .builder()
                                .code("CD98070")
                                .title("Racially / religiously aggravated common assault / beating")
                                .contraryToActAndSection("Contrary to section 29(1)(c) and (3) of the Crime and Disorder Act 1998.")
                                .summary("On ${DATE} at ${TOWN} in the county of ${COUNTY} assaulted ${NAME} by beating and the offence was racially aggravated within the terms of section 28 of the Crime and Disorder Act 1998.")
                                .factTypes(List.of(DATE, TOWN, COUNTY, NAME ))
                                .build(),
                        OffenceDefinition
                                .builder()
                                .code("CJ88150")
                                .title("Common assault of an emergency worker")
                                .contraryToActAndSection("Contrary to section 39 of the Criminal Justice Act 1988 and section 1 of the Assaults on Emergency Workers (Offences) Act 2018.")
                                .summary("On ${DATE} at ${TOWN} in the county of ${COUNTY} assaulted ${NUMBER} PC an emergency worker, namely PC ${NAME} acting in the exercise of her functions as such a worker")
                                .factTypes(List.of(DATE, TOWN, COUNTY, NAME, NUMBER ))
                                .build(),
                        OffenceDefinition
                                .builder()
                                .code("PU86116")
                                .title("Use threatening / abusive / insulting words / behaviour to cause harassment / alarm / distress")
                                .contraryToActAndSection("Contrary to Section 4A(1) and (5) of the Public Order Act 1986.")
                                .summary("On ${DATE} at ${TOWN} in the county of ${COUNTY}, with intent to cause PC ${NUMBER} ${NAME} harassment, alarm or distress,used threatening, abusive or insulting words or behaviour or disorderly behaviour, thereby causing that person or another harassment, alarm or distress")
                                .factTypes(List.of(DATE, TOWN, COUNTY, NAME, NUMBER ))
                                .build(),
                        OffenceDefinition
                                .builder()
                                .code("PU86003")
                                .title("Affray")
                                .contraryToActAndSection("Contrary to section 3(1) and (7) of the Public Order Act 1988.")
                                .summary("On ${DATE} at ${TOWN} in the county of ${COUNTY}  used or threatened unlawful violence towards another and your conduct was such as would cause a person of reasonable firmness  present at the scene to fear for his personal safety")
                                .factTypes(List.of(DATE, TOWN, COUNTY))
                                .build(),
                        OffenceDefinition
                                .builder()
                                .code("OF61131")
                                .title("Wound/inflict grievous bodily harm without intent")
                                .contraryToActAndSection("Contrary to section 20 of the Offences Against the Person Act 1861")
                                .summary("On ${DATE} at ${TOWN} in the county of ${COUNTY}, unlawfully and maliciously wounded a person, namely ${NAME}")
                                .factTypes(List.of(DATE, TOWN, COUNTY, NAME))
                                .build(),
                        OffenceDefinition
                                .builder()
                                .code("CJ88144")
                                .title("Possess knife blade / sharp pointed article in a public place - Criminal Justice Act 1988")
                                .contraryToActAndSection("Contrary to section 139(1) and (6) of the Criminal Justice Act 1988.")
                                .summary("On ${DATE} at ${TOWN} in the county of ${COUNTY}, had with you, without good reason or lawful authority, in a public place ${SHOP} at ${PLACE} an article which had a blade or was sharply pointed, namely black knife.")
                                .factTypes(List.of(DATE, TOWN, COUNTY, NAME, PLACE, SHOP))
                                .build()

                )),
                entry(CaseCategory.THEFT, List.of(
                        OffenceDefinition
                                .builder()
                                .code("TH68010")
                                .title("Theft from a shop")
                                .contraryToActAndSection("section 1(1) and 7 of the Theft Act 1968.")
                                .summary("Between ${DATE_RANGE_FIRST} and ${DATE_RANGE_SECOND} at ${TOWN} in the county of ${COUNTY}, stole ${NUMBER} items of clothing, to the value of ${AMOUNT}, belonging to ${SHOP}")
                                .factTypes(List.of(DATE_RANGE_FIRST, DATE_RANGE_SECOND, TOWN, COUNTY, NUMBER, AMOUNT, SHOP ))
                                .build(),
                        OffenceDefinition
                                .builder()
                                .code("TH68007")
                                .title("Theft from a motor vehicle")
                                .contraryToActAndSection("section 1(1) and 7 of the Theft Act 1968.")
                                .summary("Between ${DATE_RANGE_FIRST} and ${DATE_RANGE_SECOND} at ${TOWN} in the county of ${COUNTY}, stole ${NUMBER} items of clothing, to the value of ${AMOUNT}, belonging to ${NAME}")
                                .factTypes(List.of(DATE_RANGE_FIRST, DATE_RANGE_SECOND, TOWN, COUNTY, NUMBER, AMOUNT, NAME ))
                                .build(),
                        OffenceDefinition
                                .builder()
                                .code("TH68003")
                                .title("Theft by employee")
                                .contraryToActAndSection("section 1(1) and 7 of the Theft Act 1968.")
                                .summary("Between ${DATE_RANGE_FIRST} and ${DATE_RANGE_SECOND} at ${TOWN} in the County of ${COUNTY} stole goods of a value ${AMOUNT} belonging to ${SHOP}")
                                .factTypes(List.of(DATE_RANGE_FIRST, DATE_RANGE_SECOND, TOWN, COUNTY, NUMBER, AMOUNT, SHOP ))
                                .build(),
                        OffenceDefinition
                                .builder()
                                .code("TH68050")
                                .title("Take a motor vehicle without the owners consent")
                                .contraryToActAndSection("Contrary to section 12(1) of the Theft Act 1968.")
                                .summary("On ${DATE} at ${TOWN} in the County of ${COUNTY} without the consent of the owner, or other lawful authority, took a conveyance, namely ${CAR_TYPE} VRM ${CAR_REGISTRATION}for the use of yourself or another.")
                                .factTypes(List.of(DATE, TOWN, COUNTY, CAR_TYPE, CAR_REGISTRATION ))
                                .build(),
                        OffenceDefinition
                                .builder()
                                .code("TH68027A")
                                .title("Attempt burglary other than dwelling with intent to steal")
                                .contraryToActAndSection("Contrary to section 12(1) of the Theft Act 1968.")
                                .summary("On ${DATE} at ${TOWN} in the County of ${COUNTY} attempted to enter as a trespasser a building, namely a garage in ${TOWN}, with intent to steal.")
                                .factTypes(List.of(DATE, TOWN, COUNTY ))
                                .build()
                )),
                entry(CaseCategory.GENERAL, List.of(
                        OffenceDefinition
                                .builder()
                                .code("VG24005")
                                .title("Begging in a public place")
                                .contraryToActAndSection("Contrary to section 3 of Vagrancy Act 1824 amd section 70 of Criminal Justice Act 1982")
                                .summary("On ${DATE} at ${TOWN} in the county of ${COUNTY}, placed yourself in a public place, street, highway, court or passage namely ${PLACE} to beg or gather aims.")
                                .factTypes(List.of(DATE, TOWN, COUNTY, PLACE))
                                .build(),
                        OffenceDefinition
                                .builder()
                                .code("COML018")
                                .title("Public Nuisance - indecent exposure")
                                .contraryToActAndSection("Contrary to Common Law.")
                                .summary("On ${DATE} at ${TOWN} committed a public nuisance by indecently exposing yourself by Masturbating at the back door of your home, making sure your neighbour could see")
                                .factTypes(List.of(DATE, TOWN))
                                .build(),
                        OffenceDefinition
                                .builder()
                                .code("SX03220")
                                .title("Breach SHPO / interim SHPO / SOPO / interim SOPO / foreign travel order")
                                .contraryToActAndSection("Contrary to section 103M1) and (3) of the Sentences Act 2002")
                                .summary("On ${DATE} at ${TOWN} committed a public nuisance by indecently exposing yourself by Masturbating at the back door of your home, making sure your neighbour could see, that you were prohibited from doing by a sexual harm prevention order made by ${COURT} on ${DATE}")
                                .factTypes(List.of(DATE, TOWN, COURT))
                                .build(),
                        OffenceDefinition
                                .builder()
                                .code("CJ67002")
                                .title("Drunk and disorderly in a public place")
                                .contraryToActAndSection("Contrary to section 91(1) of the Criminal Justice Act 1967.")
                                .summary("On ${DATE} at ${TOWN}  in a public place, namely ${PLACE}, were guilty, while drunk, of disorderly behaviour.")
                                .factTypes(List.of(DATE, TOWN, PLACE))
                                .build(),
                        OffenceDefinition
                                .builder()
                                .code("PU86004")
                                .title("Use threatening / abusive / insulting words / behaviour with intent to cause fear of / provoke unlawful violence")
                                .contraryToActAndSection("Contrary to section 4(1) and (4) of the Public Order Act 1986")
                                .summary("On ${DATE} at ${TOWN}  used towards others threatening, abusive or insulting words or behaviour with intent to cause those persons to believe that immediate unlawful violence would be used against them by any person, or to provoke the immediate use of unlawful violence by them whereby those persons were likely to believe that such violence would be used, or it was likely that such violence would be provoked.")
                                .factTypes(List.of(DATE, TOWN, PLACE))
                                .build(),
                        OffenceDefinition
                                .builder()
                                .code("PU86004")
                                .title("Use threatening / abusive / insulting words / behaviour with intent to cause fear of / provoke unlawful violence")
                                .contraryToActAndSection("Contrary to section 4(1) and (4) of the Public Order Act 1986")
                                .summary("On ${DATE} at ${TOWN}  used towards others threatening, abusive or insulting words or behaviour with intent to cause those persons to believe that immediate unlawful violence would be used against them by any person, or to provoke the immediate use of unlawful violence by them whereby those persons were likely to believe that such violence would be used, or it was likely that such violence would be provoked.")
                                .factTypes(List.of(DATE, TOWN, PLACE))
                                .build(),
                        OffenceDefinition
                                .builder()
                                .code("PUB6116")
                                .title("Use threatening / abusive insulting words / behaviour to cause harassment / alarm/ distress")
                                .contraryToActAndSection("Contrary to Section 4A(1) and (5) of the Public Order Act 1986.")
                                .summary("On ${DATE} at ${TOWN}, with intent to cause PC ${NUMBER} ${NAME}, alarm or distress,used threatening, abusive or insulting words or behaviour or disorderly behaviour, thereby causing that person or another harassment, alarm or distress")
                                .factTypes(List.of(DATE, TOWN, NUMBER, NAME))
                                .build(),
                        OffenceDefinition
                                .builder()
                                .code("CD71039")
                                .title("Criminal damage to property valued under £5000")
                                .contraryToActAndSection("Contrary to Section 1(1) and 4 of the Criminal Damage Act 1971")
                                .summary("On ${DATE} at ${TOWN} without lawful excuse, damaged several windows to the value of ${AMOUNT} belonging to ${SHOP} intending to destroy or damage such property or being reckless as to whether such property would be destroyed or damaged.")
                                .factTypes(List.of(DATE, TOWN, NUMBER, NAME, AMOUNT, SHOP))
                                .build(),
                        OffenceDefinition
                                .builder()
                                .code("MD71529")
                                .title("Concerned in production of a controlled drug of Class B cannabis")
                                .contraryToActAndSection("Contrary to section 4(2)(b) of and Schedule 4 to the Misuse of Drugs Act 1971")
                                .summary("On ${DATE} at ${TOWN} were concerned in the production of a quantity of cannabis, a controlled drug of class B by another in contravention of section 4(1) of the Misuse of Drugs Act 1971")
                                .factTypes(List.of(DATE, TOWN, NUMBER, NAME, AMOUNT, SHOP))
                                .build(),
                        OffenceDefinition
                                .builder()
                                .code("CD98075")
                                .title("Racially / religiously aggravated harassment/alarm/distress by words / writing")
                                .contraryToActAndSection("Contrary to section 31(1)(c) and (5) of the Crime and Disorder Act 1998.")
                                .summary("On ${DATE} at ${TOWN} in the county of ${COUNTY} used threatening or abusive words or behaviour or disorderly behaviour within the hearing or sight of a person likely to be caused harassment, alarm or distress, thereby, and the offence was racially aggravated within the terms of section 28 of the Crime and Disorder Act 1998.")
                                .factTypes(List.of(DATE, TOWN, COUNTY, NAME ))
                                .build(),
                        OffenceDefinition
                                .builder()
                                .code("PH97004")
                                .title("Harassment without violence")
                                .contraryToActAndSection("Contrary to section 2x1) and () of the Protection from Harassment Act 1907")
                                .summary("Between ${DATE_RANGE_FIRST} and ${DATE_RANGE_SECOND} at ${TOWN} in the county of ${COUNTY} pursue a course of which arrested to the harassment ${NAME} and which you knew or ought to have known amounted to the harassment of her in that you attended her address and made hundreds of phone calls")
                                .factTypes(List.of(DATE_RANGE_FIRST, DATE_RANGE_SECOND, TOWN, COUNTY, NAME ))
                                .build()

                )),
                entry(CaseCategory.NPS, List.of(
                        OffenceDefinition
                                .builder()
                                .code("CJ03510")
                                .title("Failing to comply with the requirements of a community order")
                                .contraryToActAndSection("In accordance with Part 2 of Schedule 8 to the Criminal Justice Act 2003")
                                .summary("Failed without reasonable excuse to comply with the requirements of a community order made by ${COURT} on ${DATE_RANGE_FIRST} by failing to " +
                                        "comply with your requirements of the order in that when you attended for your induction appointment on ${DATE_RANGE_SECOND}, due to your obstructive behaviour you were " +
                                        "instructed to leave and attend the group induction on the ${DATE}. You failed to contact ${TOWN} since this date and due to you being of No Fixed Abode your")
                                .factTypes(List.of(COURT, SENTENCE_DATE, DATE, DATE_RANGE_FIRST, DATE_RANGE_SECOND, TOWN))
                                .build(),
                        OffenceDefinition
                                .builder()
                                .code("CJ08508")
                                .title("Application to revoke a youth rehabilitation order without re-sentencing")
                                .contraryToActAndSection("In accordance with Part 3 of Schedule 2 to the Criminal Justice and Immigration Act 2008.")
                                .summary("Application to revoke a youth rehabilitation order (without re-sentencing) made by ${COURT} on ${SENTENCE_DATE} on the grounds that you attended a " +
                                        "Compliance panel on ${DATE} along with the Youth Justice ISS Team manager and your mother to discuss your missed appointments and what we " +
                                        "would all agree should happen. Given your complex needs it was agreed at this meeting that the YRO with ISS is not suitable for you and that your order be returned " +
                                        "back to court for revocation. CJS code (most serious original offence).")
                                .factTypes(List.of(COURT, SENTENCE_DATE, DATE))
                                .build(),
                        OffenceDefinition
                                .builder()
                                .code("PH97501")
                                .title("Application to vary restraining order")
                                .contraryToActAndSection("In accordance with section 5(4) of the Protection from Harassment Act 1997.")
                                .summary("Application to vary a restraining order made on ${SENTENCE_DATE} by ${COURT} for ${NAME} by amending the date of expiry of the order - extension on the grounds that applicants request")
                                .factTypes(List.of(COURT, SENTENCE_DATE, NAME, DATE))
                                .build(),
                        OffenceDefinition
                                .builder()
                                .code("PH97501")
                                .title("Application to vary restraining order")
                                .contraryToActAndSection("In accordance with section 5(4) of the Protection from Harassment Act 1997.")
                                .summary("Application to vary a restraining order made on ${SENTENCE_DATE} by ${COURT} for ${NAME} by amending the date of expiry of the order - extension on the grounds that applicants request")
                                .factTypes(List.of(COURT, SENTENCE_DATE, NAME, DATE))
                                .build(),
                        OffenceDefinition
                                .builder()
                                .code("CJ03510")
                                .title("Failing to comply with the requirements of a community order")
                                .contraryToActAndSection("In accordance with Part 2 of Schedule 8 to the Criminal Justice Act 2003")
                                .summary("Failed without reasonable excuse to comply with the requirements of a community order made by ${COURT} on ${SENTENCE_DATE} by failing to " +
                                        "comply with your requirements of the order in that when you attended for your induction appointment on ${DATE}, due to your obstructive behaviour you were " +
                                        "instructed to leave and attend the group induction on the ${DATE}. You failed to contact Manchester since this date and due to you being of No Fixed Abode your whereabouts are currently unknown")
                                .factTypes(List.of(COURT, SENTENCE_DATE, NAME, DATE))
                                .build()

                )),
                entry(RSPCA, List.of(
                        OffenceDefinition
                                .builder()
                                .code("TH68010")
                                .title("Cause unnecessary suffering to a protected animal or, if being responsible for a protected animal, to permit any unnecessary suffering to be caused to any such animal ")
                                .contraryToActAndSection("Animal Welfare Act 2006 s4 and s9")
                                .summary("Between ${DATE_RANGE_FIRST} and ${DATE_RANGE_SECOND} at ${TOWN} in the county of ${COUNTY}, kept an animal namely a dog in conditions to cause suffering ")
                                .factTypes(List.of(DATE_RANGE_FIRST, DATE_RANGE_SECOND, TOWN, COUNTY, NUMBER, AMOUNT, SHOP ))
                                .build()

                )),
                entry(CaseCategory.HEALTH_AND_SAFETY, List.of(
                        OffenceDefinition
                                .builder()
                                .code("FS13009")
                                .title("Contravene/fail to comply with EU provision concerning food safety and hygiene")
                                .contraryToActAndSection("Contrary to regulation 19(1) and (2) of the Food Safety and Hygiene (England) Regulations 2013.")
                                .summary("On ${DATE} at ${FOOD_LOCATION}, ${TOWN}, ${POSTCODE} in the county of ${COUNTY} contravened or failed to comply with a provision of Regulation 178/2002 that was " +
                                        "specified in column 1 of Schedule 2 to the Food Safety and Hygiene (England) Regulations 2013 and whose subject matter was described in column 2 of that " +
                                        "Schedule, namely Regulation 19(1) Food Safety and Hygiene (England) Regulations 2013 and Regulation (EC) 852/2004 Chapter II Article 5 Para 1 and 2 in that " +
                                        "failed to have procedures based on HACCP principles in that you failed to have a documented food safety management system in place.")
                                .factTypes(List.of(DATE, FOOD_LOCATION, TOWN, POSTCODE, COUNTY ))
                                .company(true)
                                .build(),
                        OffenceDefinition
                                .builder()
                                .code("HS74002")
                                .title("Employer / self-employed person fail to discharge general health / safety duty to person other than employee")
                                .contraryToActAndSection("Contrary to sections 3 and 33(1)(a) of, and schedule 3A to, the Health and Safety at Work etc Act 1974.")
                                .summary("On or before ${DATE} at ${TOWN}, being an employer, failed to conduct your undertaking in such a way to ensure, so far as was reasonably practicable, that " +
                                        "persons not in your employment who may have been affected thereby, were not exposed to risks to their health and safety. ")
                                .company(true)
                                .factTypes(List.of(DATE, TOWN ))
                                .build()

                ))
        );
    }


}
