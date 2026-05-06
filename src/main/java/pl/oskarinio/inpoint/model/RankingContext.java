package pl.oskarinio.inpoint.model;

import pl.oskarinio.inpoint.model.api.ParcelLocker;
import pl.oskarinio.inpoint.model.PointRequest;

public record RankingContext(
    PointRequest pointRequest,
    ParcelLocker parcelLocker,
    double totalWeight
) {}
