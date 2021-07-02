package ke.co.ngonyoku.covid_track.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class CasesConfirmedGlobally {
    private String countryOrRegion;
    private String provinceOrState;
    private String latitude;
    private String longitude;
    private int latestTotalCasesReported;
}
