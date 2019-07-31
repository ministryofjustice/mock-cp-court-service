package uk.gov.justice.digital.court.crimeportal.data.generate;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.Range;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
public class SessionDefinition {
    private LocalDate dateOfAppearance;
    private String courtName;
    private String cmu;
    private String localJusticeArea;
    private String panel;
    private List<String> courtRooms;
    private Range<LocalTime> morning;
    private Range<LocalTime> afternoon;
    private Range<Long> blocksPerSession;
    private List<BlockDefinition> blockDefinitions;
}
