package pl.oskarinio.inpoint.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LockerAvailabilityDto(
        @JsonProperty("status")
        ParcelLockerAvailabilityStatus status
) {}
