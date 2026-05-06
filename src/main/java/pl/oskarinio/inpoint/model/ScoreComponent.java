package pl.oskarinio.inpoint.model;

import pl.oskarinio.inpoint.model.api.ParcelLocker;
import pl.oskarinio.inpoint.model.PointRequest;

import java.util.function.ToDoubleBiFunction;
import java.util.function.ToDoubleFunction;

public record ScoreComponent(
        ToDoubleFunction<PointRequest> weightProvider,
        ToDoubleBiFunction<ParcelLocker, PointRequest> scoreProvider
) {}

