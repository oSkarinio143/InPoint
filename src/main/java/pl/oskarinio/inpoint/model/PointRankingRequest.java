package pl.oskarinio.inpoint.model;


import jakarta.validation.constraints.NotNull;

import java.util.List;

public class PointRankingRequest {

    @NotNull
    private double latitude;

    @NotNull
    private double longitude;

    private String status;
    private double location247Weight;
    private double isNextWeight;
    private double easyAccessZoneWeight;
    private double paymentAvailable;
    private double printInStore;
    private double locationType;


}
