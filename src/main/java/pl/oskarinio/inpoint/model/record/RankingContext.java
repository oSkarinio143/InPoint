package pl.oskarinio.inpoint.model.record;

import pl.oskarinio.inpoint.model.ParcelLocker;
import pl.oskarinio.inpoint.model.PointRequest;

public record RankingContext(
    PointRequest pointRequest,
    ParcelLocker parcelLocker,
    double totalWeight,
    double distance
) {}
