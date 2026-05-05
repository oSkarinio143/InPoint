package pl.oskarinio.inpoint.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Status {
    @JsonProperty("Operating")
    OPERATING,
    @JsonProperty("Created")
    CREATED
}
