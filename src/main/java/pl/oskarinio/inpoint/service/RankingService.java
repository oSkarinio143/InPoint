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

    private static final List<RankingRule> RULES = List.of(
            new RankingRule(ParcelLocker::isLocation247, PointRequest::getLocation247Weight),
            new RankingRule(ParcelLocker::isNext, PointRequest::getIsNextWeight),
            new RankingRule(ParcelLocker::isEasyAccessZone, PointRequest::getEasyAccessZoneWeight),
            new RankingRule(ParcelLocker::isPaymentAvailable, PointRequest::getPaymentAvailableWeight),
            new RankingRule(l -> Boolean.TRUE.equals(l.getPrintInStore()), PointRequest::getPrintInStoreWeight),
            new RankingRule(l -> "Indoor".equalsIgnoreCase(l.getLocationType()), PointRequest::getLocationTypeWeight)
    );

    public List<PointResult> handleRanking(List<ParcelLocker> lockers, PointRequest request) {
        double totalWeight = calculateTotalWeight(request);

        // Nawet jeśli totalWeight <= 0, chcemy policzyć dystans dla każdego punktu
        return lockers.parallelStream()
                .map(locker -> calculatePoint(locker, request, totalWeight))
                .sorted(Comparator.comparingDouble(PointResult::getAgreementPercentage).reversed())
                .collect(Collectors.toList());
    }

    private PointResult calculatePoint(ParcelLocker locker, PointRequest request, double totalWeight) {
        // 1. Obliczamy fizyczny dystans (Haversine)
        double distance = calculateHaversine(
                request.getLatitude(), request.getLongitude(),
                locker.getLocation().latitude(), locker.getLocation().longitude()
        );

        double agreementPercentage = 0.0;

        // 2. Liczymy scoring tylko, jeśli suma wag jest dodatnia
        if (totalWeight > 0) {
            double R = request.getMaxDistance();
            double proximityScore = 0;

            if (distance <= R) {
                proximityScore = (R - distance) / (R * (1 + K_SENSITIVITY * distance));
            }

            double earnedPoints = 0;
            // Punkty za dystans
            earnedPoints += request.getDistanceWeight() * proximityScore;

            // Punkty za cechy
            for (RankingRule rule : RULES) {
                if (rule.feature.test(locker)) {
                    earnedPoints += rule.weightGetter.applyAsDouble(request);
                }
            }

            double score = (earnedPoints / totalWeight) * 100.0;
            agreementPercentage = Math.round(score * 100.0) / 100.0;
        }

        // 3. Tworzymy wynik (zaokrąglamy dystans do 2 miejsc po przecinku)
        return new PointResult(
                agreementPercentage,
                locker.getName(),
                locker.getAddress().line1() + " " + locker.getAddress().line2(),
                Math.round(distance * 100.0) / 100.0, // Nowe pole: dystans w km
                locker.getLocation().latitude(),
                locker.getLocation().longitude(),
                locker.isLocation247(),
                locker.isNext(),
                locker.isEasyAccessZone(),
                locker.isPaymentAvailable(),
                locker.getPrintInStore(),
                locker.getLocationType()
        );
    }

    private double calculateTotalWeight(PointRequest r) {
        double sum = r.getDistanceWeight();
        for (RankingRule rule : RULES) {
            sum += rule.weightGetter.applyAsDouble(r);
        }
        return sum;
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
            ToDoubleFunction<PointRequest> weightGetter
    ) {}
}