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

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    private Status status;

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
