package uk.gov.justice.digital.court.crimeportal.data.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourtListSummary {
    private int sessionCount;
    private int blockCount;
    private int caseCount;
}
