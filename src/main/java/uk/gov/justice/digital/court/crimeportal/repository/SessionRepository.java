package uk.gov.justice.digital.court.crimeportal.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import uk.gov.justice.digital.court.crimeportal.data.entity.SessionType;

import java.util.List;

public interface SessionRepository extends MongoRepository<SessionType, String> {
    List<SessionType> findByDohAndCourt(String dateOfHearing, String court);
}
