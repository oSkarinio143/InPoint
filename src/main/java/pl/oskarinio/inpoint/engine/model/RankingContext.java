package pl.oskarinio.inpoint.engine.model;

import pl.oskarinio.inpoint.integration.model.ParcelLocker;
import pl.oskarinio.inpoint.dto.PointRequest;

public record RankingContext(
    PointRequest pointRequest,
    ParcelLocker parcelLocker,
    double totalWeight
) {}
