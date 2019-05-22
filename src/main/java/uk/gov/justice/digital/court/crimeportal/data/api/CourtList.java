package uk.gov.justice.digital.court.crimeportal.data.api;

import lombok.Builder;
import lombok.Data;
import uk.gov.justice.digital.court.crimeportal.data.entity.SessionsType;

import java.time.LocalDate;

@Builder
@Data
public class CourtList {

    private String courtName;
    private LocalDate date;
    private SessionsType sessions;
}
