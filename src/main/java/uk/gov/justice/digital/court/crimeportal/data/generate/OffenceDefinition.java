package uk.gov.justice.digital.court.crimeportal.data.generate;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OffenceDefinition {
    public enum FactType {
        NAME,
        CAR_REGISTRATION,
        DATE,
        TIME,
        DATE_RANGE_FIRST,
        DATE_RANGE_SECOND,
        CAR_TYPE,
        SPEED,
        SPEED_LIMIT,
        ROAD_LOCATION,
        PLACE,
        TOWN,
        POSTCODE,
        COUNTY,
        AGE,
        SCHOOL,
        AMOUNT,
        NUMBER,
        SHOP,
        COURT,
        SENTENCE_DATE,
        FOOD_LOCATION
    }
    private String code;
    private String title;
    private String summary;
    private String contraryToActAndSection;
    private String statementOfFact;
    private List<FactType> factTypes;
    private boolean company;
    private boolean council;
}
