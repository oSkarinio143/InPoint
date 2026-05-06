package pl.oskarinio.inpoint.integration.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LockerAvailabilityDto(
        @JsonProperty("status")
        ParcelLockerAvailabilityStatus status
) {}
