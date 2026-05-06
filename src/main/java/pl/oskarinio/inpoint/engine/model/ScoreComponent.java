package pl.oskarinio.inpoint.engine.model;

import pl.oskarinio.inpoint.integration.model.ParcelLocker;
import pl.oskarinio.inpoint.dto.PointRequest;

import java.util.function.ToDoubleBiFunction;
import java.util.function.ToDoubleFunction;

public record ScoreComponent(
        ToDoubleFunction<PointRequest> weightProvider,
        ToDoubleBiFunction<ParcelLocker, PointRequest> scoreProvider
) {}

