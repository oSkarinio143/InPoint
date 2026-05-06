package pl.oskarinio.inpoint.model.record;

import pl.oskarinio.inpoint.model.ParcelLocker;
import pl.oskarinio.inpoint.model.PointRequest;

import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;

public record RankingRule(Predicate<ParcelLocker> feature, ToDoubleFunction<PointRequest> weightGetter) {

}
