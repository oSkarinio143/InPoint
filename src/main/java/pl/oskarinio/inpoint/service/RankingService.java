package pl.oskarinio.inpoint.service;

import org.springframework.stereotype.Service;
import pl.oskarinio.inpoint.model.*;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

@Service
public class RankingService {

    private static final double K_SENSITIVITY = 0.067;

    // Reguły mapujące cechy paczkomatu na wagi z requestu
    private static final List<RankingRule> RULES = List.of(
            new RankingRule(ParcelLocker::isLocation247, PointRankingRequest::getLocation247Weight),
            new RankingRule(ParcelLocker::isNext, PointRankingRequest::getIsNextWeight),
            new RankingRule(ParcelLocker::isEasyAccessZone, PointRankingRequest::getEasyAccessZoneWeight),
            new RankingRule(ParcelLocker::isPaymentAvailable, PointRankingRequest::getPaymentAvailableWeight),
            new RankingRule(l -> l.getPrintInStore() != null && l.getPrintInStore(), PointRankingRequest::getPrintInStoreWeight),
            new RankingRule(l -> "Indoor".equalsIgnoreCase(l.getLocationType()), PointRankingRequest::getLocationTypeWeight)
    );

    public List<PointRankingResponse> handleRanking(List<ParcelLocker> lockers, PointRankingRequest request) {
        double maxPotential = calculateMaxPotential(request);
        if (maxPotential <= 0) return Collections.emptyList();

        return lockers.parallelStream()
                .map(locker -> calculateMatch(locker, request, maxPotential))
                .sorted(Comparator.comparingDouble(PointRankingResponse::getTotalScore).reversed())
                .collect(Collectors.toList());
    }

    private PointRankingResponse calculateMatch(ParcelLocker locker, PointRankingRequest request, double maxPotential) {
        double distance = calculateHaversine(
                request.getLatitude(), request.getLongitude(),
                locker.getLocation().latitude(), locker.getLocation().longitude()
        );

        // Nasz sprawiedliwy mnożnik S: (R - d) / (R * (1 + k * d))
        double R = request.getMaxDistance();
        double proximityMultiplier = Math.max(0, (R - distance) / (R * (1 + K_SENSITIVITY * distance)));

        // Sumujemy potencjał paczkomatu (Baza dystansu + cechy)
        double currentPotential = request.getDistanceWeight();
        for (RankingRule rule : RULES) {
            if (rule.feature().test(locker)) {
                currentPotential += rule.weight().applyAsDouble(request);
            }
        }

        // Wynik %: (Potencjał * Mnożnik / MaxPotencjał) * 100
        double score = ((currentPotential * proximityMultiplier) / maxPotential) * 100;

        return new PointRankingResponse(
                Math.round(score * 100.0) / 100.0,
                locker.getName(),
                locker.getAddress().line1() + " " + locker.getAddress().line2(),
                locker.getLocation().latitude(),
                locker.getLocation().longitude()
        );
    }

    private double calculateMaxPotential(PointRankingRequest r) {
        return r.getDistanceWeight() + RULES.stream()
                .mapToDouble(rule -> rule.weight().applyAsDouble(r))
                .sum();
    }

    private double calculateHaversine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return 6371 * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    private record RankingRule(
            Predicate<ParcelLocker> feature,
            ToDoubleFunction<PointRankingRequest> weight
    ) {}
}