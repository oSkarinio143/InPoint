package pl.oskarinio.inpoint.model;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
public class PointRankingRequest {

    private Status status;

    @NotNull(message = "Latitude is required")
    @Min(-90)
    @Max(90)
    private Double latitude;

    @NotNull(message = "Longitude is required")
    @Min(-180)
    @Max(180)
    private Double longitude;

    @NotNull(message = "Max distance is required")
    @Min(0)
    @Max(15)
    private Double maxDistance;

    @Min(0)
    @Max(1)
    private double distanceWeight;

    @Min(0)
    @Max(1)
    private double location247Weight;

    @Min(0)
    @Max(1)
    private double isNextWeight;

    @Min(0)
    @Max(1)
    private double easyAccessZoneWeight;

    @Min(0)
    @Max(1)
    private double paymentAvailableWeight;

    @Min(0)
    @Max(1)
    private double printInStoreWeight;

    @Min(0)
    @Max(1)
    private double locationTypeWeight;
}
