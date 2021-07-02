package ke.co.ngonyoku.covid_track.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecoveriesConfirmedGlobally {
    private String countryOrRegion;
    private String provinceOrState;
    private String latitude;
    private String longitude;
    private int latestTotalRecoveriesReported;
}
