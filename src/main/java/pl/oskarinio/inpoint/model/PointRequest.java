package pl.oskarinio.inpoint.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.oskarinio.inpoint.model.api.ParcelLockerStatus;
import pl.oskarinio.inpoint.model.api.ParcelLockerType;

@Data
@NoArgsConstructor
public class PointRequest {

    private ParcelLockerStatus status;

    @NotNull(message = "Latitude is required")
    @Min(value = -90, message = "Latitude must be greater than or equal to -90")
    @Max(value = 90, message = "Latitude must be less than or equal to 90")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    @Min(value = -180, message = "Longitude must be greater than or equal to -180")
    @Max(value = 180, message = "Longitude must be less than or equal to 180")
    private Double longitude;

    @NotNull(message = "Max distance is required")
    @Min(value = 0, message = "Max distance cannot be negative")
    private Double maxDistance;

    @Min(value = 0, message = "Distance weight must be at least 0")
    @Max(value = 1, message = "Distance weight cannot exceed 1")
    private double distanceWeight;

    @Min(value = 0, message = "24/7 location weight must be at least 0")
    @Max(value = 1, message = "24/7 location weight cannot exceed 1")
    private double location247Weight;

    @Min(value = 0, message = "IsNext weight must be at least 0")
    @Max(value = 1, message = "IsNext weight cannot exceed 1")
    private double isNextWeight;

    @Min(value = 0, message = "Easy access zone weight must be at least 0")
    @Max(value = 1, message = "Easy access zone weight cannot exceed 1")
    private double easyAccessZoneWeight;

    @Min(value = 0, message = "Payment available weight must be at least 0")
    @Max(value = 1, message = "Payment available weight cannot exceed 1")
    private double paymentAvailableWeight;

    @Min(value = 0, message = "Print in store weight must be at least 0")
    @Max(value = 1, message = "Print in store weight cannot exceed 1")
    private double printInStoreWeight;

    @Min(value = 0, message = "Location type weight must be at least 0")
    @Max(value = 1, message = "Location type weight cannot exceed 1")
    private double locationTypeWeight;

    @Min(value = 0, message = "Air index level weight must be at least 0")
    @Max(value = 1, message = "Air index level weight cannot exceed 1")
    private double airIndexLevelWeight;

    @Min(value = 0, message = "Type weight must be at least 0")
    @Max(value = 1, message = "Type weight cannot exceed 1")
    private double typeWeight;

    private ParcelLockerType type;

    @Min(value = 0, message = "Availability status weight must be at least 0")
    @Max(value = 1, message = "Availability status weight cannot exceed 1")
    private double availabilityStatusWeight;
}