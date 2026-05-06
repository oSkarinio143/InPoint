package pl.oskarinio.inpoint.model.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParcelLocker {

    private String name;

    private LocationDto location;

    private AddressDto address;

    @JsonProperty("status")
    private ParcelLockerStatus status;

    @JsonProperty("location_247")
    private boolean location247;

    @JsonProperty("is_next")
    private boolean isNext;

    @JsonProperty("easy_access_zone")
    private boolean easyAccessZone;

    @JsonProperty("payment_available")
    private boolean paymentAvailable;

    @JsonProperty("print_in_store")
    private Boolean printInStore;

    @JsonProperty("location_type")
    private String locationType;

    @JsonProperty("air_index_level")
    private String airIndexLevel;

    @JsonProperty("type")
    private List<ParcelLockerType> type;

    @JsonProperty("locker_availability")
    private LockerAvailabilityDto lockerAvailabilityStatus;

    @JsonProperty("functions")
    private List<String> functions;
}