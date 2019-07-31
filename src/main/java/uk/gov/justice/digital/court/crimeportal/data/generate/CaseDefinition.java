package uk.gov.justice.digital.court.crimeportal.data.generate;

import lombok.Data;
import org.apache.commons.lang3.Range;

@Data
public class CaseDefinition {
    private CaseCategory caseCategory;
    private String informant;
    private Range<Long> offenceCount;
}
