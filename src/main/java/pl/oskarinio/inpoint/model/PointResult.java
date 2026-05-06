package pl.oskarinio.inpoint.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.oskarinio.inpoint.model.request.ParcelLockerAvailabilityStatus;
import pl.oskarinio.inpoint.model.request.ParcelLockerType;

import java.util.List;
import java.util.Map;

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
    private String airIndexLevel;
    private List<ParcelLockerType> type;
    private ParcelLockerAvailabilityStatus lockerAvailabilityStatus;
    private List<String> functions;
}