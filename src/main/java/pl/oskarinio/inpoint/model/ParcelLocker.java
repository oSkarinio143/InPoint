package pl.oskarinio.inpoint.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) // Ignoruje setki innych pól z API InPost
public class ParcelLocker {

    private String name;
    private String status;
    private List<String> type;

    // Zagnieżdżony obiekt lokalizacji w JSON
    private LocationData location;

    // Zagnieżdżony obiekt adresu w JSON
    private AddressData address;

    @JsonProperty("location_247")
    private boolean location247;

    @JsonProperty("is_next")
    private boolean isNext;

    @JsonProperty("easy_access_zone")
    private boolean easyAccessZone;

    @JsonProperty("payment_available")
    private boolean paymentAvailable;

    @JsonProperty("print_in_store")
    private Boolean printInStore; // Używamy Boolean, bo w JSON może być null

    // Podklasy pomocnicze do mapowania struktury JSON
    @Data
    public static class LocationData {
        private double latitude;
        private double longitude;
    }

    @Data
    public static class AddressData {
        private String line1;
        private String line2;
    }
}