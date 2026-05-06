package pl.oskarinio.inpoint.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.oskarinio.inpoint.model.*;
import pl.oskarinio.inpoint.model.record.LocationDto;
import pl.oskarinio.inpoint.model.record.RankingRule;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RankingService {

    private static final double K_SENSITIVITY = 0.067;

    private final DistanceCalculatorService distanceCalculatorService;
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
        double distance = distanceCalculatorService.calculateDistance(request, locker);
        double agreementPercentage = getAgreementPercentage(request, locker, distance, totalWeight);
        return getPointResult(locker, agreementPercentage, distance);
    }

    private double getAgreementPercentage(PointRequest request, ParcelLocker locker, double distance, double totalWeight){
        double agreementPercentage = 0.0;
        if (totalWeight == 0){
            agreementPercentage = 100;
        }

        if (totalWeight > 0) {
            agreementPercentage = calculateAgreementPercentage(request, locker, distance, totalWeight);
        }
        return agreementPercentage;
    }
    private double calculateAgreementPercentage(PointRequest request, ParcelLocker locker, double distance, double totalWeight){
        double maxDistance = request.getMaxDistance();
        double proximityScore = 0;

        if (distance <= maxDistance) {
            proximityScore = (maxDistance - distance) / (maxDistance * (1 + K_SENSITIVITY * distance));
        }

        double earnedPoints = 0;

        // Punkty za dystans
        earnedPoints += request.getDistanceWeight() * proximityScore;

        // Punkty za cechy
        for (RankingRule rule : RULES) {
            if (rule.feature().test(locker)) {
                earnedPoints += rule.weightGetter().applyAsDouble(request);
            }
        }

        double score = (earnedPoints / totalWeight) * 100.0;
        return  Math.round(score * 100.0) / 100.0;
    }

    private double calculateTotalWeight(PointRequest request) {
        double sum = request.getDistanceWeight();
        sum += RULES.stream()
                .mapToDouble(rule -> rule.weightGetter().applyAsDouble(request))
                .sum();
        return sum;
    }

    private PointResult getPointResult(ParcelLocker locker, double agreementPercentage, double distance){
        String resultAddress = locker.getAddress().line1() + " " + locker.getAddress().line2();
        double resultDistance = Math.round(distance * 100.0) / 100.0;
        return new PointResult(
                agreementPercentage,
                locker.getName(),
                resultAddress,
                resultDistance,
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
}