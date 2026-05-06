package pl.oskarinio.inpoint.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ParcelLockerType {
    @JsonProperty("parcel_locker")
    PARCEL_LOCKER,

    @JsonProperty("refrigerated_locker_machine")
    REFRIGERATED,

    @JsonProperty("parcel_locker_superpop")
    SUPERPOP,

    @JsonProperty("pok")
    POK,

    @JsonProperty("pop")
    POP,

    @JsonProperty("pudo_mini")
    PUDO_MINI;
}
