package pl.oskarinio.inpoint.model.record;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.oskarinio.inpoint.model.ParcelLocker;
import pl.oskarinio.inpoint.model.PointRequest;

public record RankingContext(
    PointRequest request,
    ParcelLocker locker,
    double totalWeight,
    double distance
) {}
