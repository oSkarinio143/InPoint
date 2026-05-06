package pl.oskarinio.inpoint.model.record;

import pl.oskarinio.inpoint.model.ParcelLocker;
import pl.oskarinio.inpoint.model.request.PointRequest;

public record RankingContext(
    PointRequest pointRequest,
    ParcelLocker parcelLocker,
    double totalWeight
) {}
