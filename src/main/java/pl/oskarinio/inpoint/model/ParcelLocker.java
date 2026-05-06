package pl.oskarinio.inpoint.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import pl.oskarinio.inpoint.model.record.AddressDto;
import pl.oskarinio.inpoint.model.record.LocationDto;

import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParcelLocker {

    private String name;
    private Status status;

    private LocationDto location;

    private AddressDto address;

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
    private List<String> type;

    @JsonProperty("locker_availability")
    private Map<String, Object> lockerAvailability;

    @JsonProperty("functions")
    private List<String> functions;
}