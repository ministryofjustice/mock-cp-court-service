package uk.gov.justice.digital.court.crimeportal.data.generate;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.Range;

import java.util.List;

@Data
@Builder
public class BlockDefinition {
    private String description;
    private List<CaseCategory> caseCategories;
    private Range<Long> numberOfCases;
    private boolean firstListingOnly;
}
