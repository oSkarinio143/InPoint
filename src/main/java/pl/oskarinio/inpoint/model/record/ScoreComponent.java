package pl.oskarinio.inpoint.model.record;

import pl.oskarinio.inpoint.model.ParcelLocker;
import pl.oskarinio.inpoint.model.request.PointRequest;

import java.util.function.ToDoubleBiFunction;
import java.util.function.ToDoubleFunction;

public record ScoreComponent(
        ToDoubleFunction<PointRequest> weightProvider,
        ToDoubleBiFunction<ParcelLocker, PointRequest> scoreProvider
) {}

