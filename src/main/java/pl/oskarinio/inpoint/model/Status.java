package pl.oskarinio.inpoint.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Status {
    Operating,
    Created,
    Disabled,
    NonOperating,
    Overloaded
}
