package pl.oskarinio.inpoint.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.oskarinio.inpoint.model.*;
import pl.oskarinio.inpoint.model.record.RankingContext;
import pl.oskarinio.inpoint.model.record.RankingRule;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RankingService {

    //Wartosc dobrana aby wynik punktowy spadal odpowiednio w miare spadku odleglosci (subiektywna ocena)
    private static final double K_SENSITIVITY = 0.067;

    private final DistanceCalculatorService distanceCalculatorService;
    private static final List<RankingRule> RULES = List.of(
            new RankingRule(ParcelLocker::isLocation247, PointRequest::getLocation247Weight),
            new RankingRule(ParcelLocker::isNext, PointRequest::getIsNextWeight),
            new RankingRule(ParcelLocker::isEasyAccessZone, PointRequest::getEasyAccessZoneWeight),
            new RankingRule(ParcelLocker::isPaymentAvailable, PointRequest::getPaymentAvailableWeight),
            new RankingRule(locker -> Boolean.TRUE.equals(locker.getPrintInStore()), PointRequest::getPrintInStoreWeight),
            new RankingRule(locker -> "Indoor".equalsIgnoreCase(locker.getLocationType()), PointRequest::getLocationTypeWeight)
    );

    public List<PointResult> rankPoints(List<ParcelLocker> lockers, PointRequest request) {
        double totalWeight = calculateTotalWeight(request);
        return lockers.parallelStream()
                .map(locker -> calculateScoreSinglePoint(request, locker, totalWeight))
                .sorted(Comparator.comparingDouble(PointResult::getAgreementPercentage).reversed())
                .collect(Collectors.toList());
    }

    private PointResult calculateScoreSinglePoint(PointRequest request, ParcelLocker locker, double totalWeight) {
        double distance = distanceCalculatorService.calculateDistance(request, locker);
        RankingContext rankingContext = getRankingContext(request, locker, totalWeight, distance);
        double agreementPercentage = getAgreementPercentage(rankingContext);
        return getPointResult(rankingContext, agreementPercentage);
    }

    private RankingContext getRankingContext(PointRequest request, ParcelLocker locker, double totalWeight, double distance){
        return new RankingContext(request,
                locker,
                totalWeight,
                distance);
    }

    private double getAgreementPercentage(RankingContext rankingContext){
        if (rankingContext.totalWeight() == 0){
            return 100;
        }
        return calculateAgreementPercentage(rankingContext);
    }

    private double calculateAgreementPercentage(RankingContext rankingContext){
        PointRequest request = rankingContext.request();
        double maxDistance = request.getMaxDistance();
        double distanceScore = calculateDistanceScore(rankingContext.distance(), maxDistance);
        double earnedScore = calculateTotalEarnedScore(rankingContext, distanceScore);
        return calculateAgreementPercentageFromScore(earnedScore, rankingContext.totalWeight());
    }

    private double calculateDistanceScore(double distance, double maxDistance){
        if (distance <= maxDistance) {
            return (maxDistance - distance) / (maxDistance * (1 + K_SENSITIVITY * distance));
        }
        return 0;
    }

    private double calculateTotalEarnedScore(RankingContext rankingContext, double distanceScore){
        PointRequest request = rankingContext.request();
        double earnedPoints = request.getDistanceWeight() * distanceScore;
        earnedPoints += RULES.stream()
                .filter(rule -> rule.feature().test(rankingContext.locker()))
                .mapToDouble(rule -> rule.weightGetter().applyAsDouble(request))
                .sum();
        return earnedPoints;
    }

    private double calculateAgreementPercentageFromScore(double earnedScore, double totalWeight){
        double score = (earnedScore / totalWeight) * 100.0;
        return Math.round(score * 100.0) / 100.0;
    }

    private double calculateTotalWeight(PointRequest request) {
        double sum = request.getDistanceWeight();
        sum += RULES.stream()
                .mapToDouble(rule -> rule.weightGetter().applyAsDouble(request))
                .sum();
        return sum;
    }

    private PointResult getPointResult(RankingContext rankingContext, double agreementPercentage){
        ParcelLocker locker = rankingContext.locker();
        String resultAddress = locker.getAddress().line1() + " " + locker.getAddress().line2();
        double resultDistance = Math.round(rankingContext.distance() * 100.0) / 100.0;
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