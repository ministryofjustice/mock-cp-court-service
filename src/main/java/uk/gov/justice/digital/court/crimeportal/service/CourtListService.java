package uk.gov.justice.digital.court.crimeportal.service;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import uk.gov.justice.digital.court.crimeportal.data.api.CourtList;
import uk.gov.justice.digital.court.crimeportal.data.api.CourtListSummary;
import uk.gov.justice.digital.court.crimeportal.data.entity.*;
import uk.gov.justice.digital.court.crimeportal.repository.SessionRepository;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class CourtListService {
    private final SessionRepository sessionRepository;

    public CourtListService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public CourtList courtList(String court, LocalDate date) {
        final List<SessionType> sessions = sessionRepository.findByDohAndCourt(date.format(DateTimeFormatter.ofPattern("dd/MM/yyy")), court);
        val session = new SessionsType();
        session.getSession().addAll(sessions);
        return CourtList
                .builder()
                .courtName(court)
                .date(date)
                .sessions(session)
                .build();

    }

    public CourtList allCourtLists() {
        final List<SessionType> sessions = sessionRepository.findAll();
        val session = new SessionsType();
        session.getSession().addAll(sessions);
        return CourtList
                .builder()
                .sessions(session)
                .build();
    }


    public CourtListSummary load(InputStream inputStream) throws JAXBException {
        val sessions = readDataAsXML(inputStream).getData().getJob().getSessions().getSession();
        sessions.forEach(session -> {
            log.info(String.format("Writing session %s %s", session.getSId(), session.getDoh()));
            sessionRepository.save(session);
        });

        return CourtListSummary
                .builder()
                .sessionCount(sessions.size())
                .blockCount(sessions
                        .stream()
                        .map(SessionType::getBlocks)
                        .map(BlocksType::getBlock)
                        .mapToInt(List::size).sum())
                .caseCount(sessions
                        .stream()
                        .map(SessionType::getBlocks)
                        .map(BlocksType::getBlock)
                        .flatMap(List::stream)
                        .map(BlockType::getCases)
                        .map(CasesType::getCase)
                        .mapToInt(List::size)
                        .sum()
                )
                .build();
    }

    public void deleteAll() {
        sessionRepository.deleteAll();
    }

    private DocumentType readDataAsXML(InputStream inputStream) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(DocumentType.class);

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        return (DocumentType) jaxbUnmarshaller.unmarshal(inputStream);

    }

}
