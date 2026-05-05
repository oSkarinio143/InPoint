package pl.oskarinio.inpoint.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParcelLockersResponse {

    private List<ParcelLocker> items;

    @JsonProperty("total_pages") // Kluczowe: mapuje "total_pages" z JSON na zmienną w Javie
    private int totalPages;

    private int count;
}
