package pl.oskarinio.inpoint.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LockerAvailabilityDto(
        @JsonProperty("status")
        ParcelLockerAvailabilityStatus status
) {}
