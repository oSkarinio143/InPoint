package pl.oskarinio.inpoint.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointResult {

    private double agreementPercentage;
    private String name;
    private String fullAddress;
    private double distance;
    private double latitude;
    private double longitude;
    private boolean location247;
    private boolean isNext;
    private boolean easyAccessZone;
    private boolean paymentAvailable;
    private Boolean printInStore;
    private String locationType;

}