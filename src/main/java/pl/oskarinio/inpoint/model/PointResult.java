package pl.oskarinio.inpoint.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.oskarinio.inpoint.model.api.ParcelLockerAvailabilityStatus;
import pl.oskarinio.inpoint.model.api.ParcelLockerType;

import java.util.List;

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