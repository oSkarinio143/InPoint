package pl.oskarinio.inpoint.engine;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.oskarinio.inpoint.dto.PointResult;
import pl.oskarinio.inpoint.integration.model.ParcelLocker;
import pl.oskarinio.inpoint.engine.model.RankingContext;
import pl.oskarinio.inpoint.engine.model.ScoreComponent;
import pl.oskarinio.inpoint.dto.PointRequest;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RankingService {

    //Wartosc dobrana aby wynik punktowy spadal odpowiednio w miare spadku odleglosci (subiektywna ocena)

    private final DistanceCalculatorService distanceCalculatorService;
    private final ScoreCalculatorService scoreCalculatorService;

    private List<ScoreComponent> scoreComponents;

    @PostConstruct
    private void initializeScoreComponents(){
        this.scoreComponents = List.of(
                new ScoreComponent(PointRequest::getDistanceWeight,
                        (locker, request) -> scoreCalculatorService.countDistanceScore(request, locker)),
                new ScoreComponent(PointRequest::getLocation247Weight,
                        (locker, request) -> scoreCalculatorService.countLocation247Score(locker)),
                new ScoreComponent(PointRequest::getIsNextWeight,
                        (locker, request) -> scoreCalculatorService.countIsNextScore(locker)),
                new ScoreComponent(PointRequest::getEasyAccessZoneWeight,
                        (locker, request) -> scoreCalculatorService.countEasyAccessScore(locker)),
                new ScoreComponent(PointRequest::getPaymentAvailableWeight,
                        (locker, request) -> scoreCalculatorService.countPaymentScore(locker)),
                new ScoreComponent(PointRequest::getPrintInStoreWeight,
                        (locker, request) -> scoreCalculatorService.countPrintInStoreScore(locker)),
                new ScoreComponent(PointRequest::getLocationTypeWeight,
                        (locker, request) -> scoreCalculatorService.countIndoorScore(locker)),
                new ScoreComponent(PointRequest::getAirIndexLevelWeight,
                        (locker, request) -> scoreCalculatorService.countAirIndexScore(locker)),
                new ScoreComponent(PointRequest::getTypeWeight,
                        (locker, request) -> scoreCalculatorService.countTypeScore(request, locker)),
                new ScoreComponent(PointRequest::getAvailabilityStatusWeight,
                        (locker, request) -> scoreCalculatorService.countAvailabilityStatusScore(locker))
        );
    }

    public List<PointResult> rankPoints(List<ParcelLocker> lockers, PointRequest request) {
        double totalWeight = calculateTotalWeight(request);
        return lockers.parallelStream()
                .map(locker -> calculateScoreSinglePoint(request, locker, totalWeight))
                .sorted(Comparator.comparingDouble(PointResult::getAgreementPercentage).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    private PointResult calculateScoreSinglePoint(PointRequest request, ParcelLocker locker, double totalWeight) {
        double distance = distanceCalculatorService.calculateDistance(request, locker);
        RankingContext rankingContext = getRankingContext(request, locker, totalWeight, distance);
        double agreementPercentage = getAgreementPercentage(rankingContext);
        return getPointResult(rankingContext, agreementPercentage, distance);
    }

    private RankingContext getRankingContext(PointRequest request, ParcelLocker locker, double totalWeight, double distance){
        return new RankingContext(
                request,
                locker,
                totalWeight);
    }

    private double getAgreementPercentage(RankingContext rankingContext){
        if (rankingContext.totalWeight() == 0){
            return 100;
        }
        return calculateAgreementPercentage(rankingContext);
    }

    private double calculateAgreementPercentage(RankingContext rankingContext){
        double earnedScore = calculateTotalEarnedScore(rankingContext);
        return calculateAgreementPercentageFromScore(earnedScore, rankingContext.totalWeight());
    }

    private double calculateTotalEarnedScore(RankingContext rankingContext){
        PointRequest request = rankingContext.pointRequest();
        ParcelLocker locker = rankingContext.parcelLocker();

        return scoreComponents.stream()
                .mapToDouble(component -> component.weightProvider().applyAsDouble(request) * component.scoreProvider().applyAsDouble(locker, request))
                .sum();
    }

    private double calculateAgreementPercentageFromScore(double earnedScore, double totalWeight){
        double score = (earnedScore / totalWeight) * 100.0;
        return Math.round(score * 100.0) / 100.0;
    }

    private double calculateTotalWeight(PointRequest request) {
        return scoreComponents.stream()
                .mapToDouble(component -> component.weightProvider().applyAsDouble(request))
                .sum();
    }

    private PointResult getPointResult(RankingContext rankingContext, double agreementPercentage, double distance){
        ParcelLocker locker = rankingContext.parcelLocker();
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
                locker.getLocationType(),
                locker.getAirIndexLevel(),
                locker.getType(),
                locker.getLockerAvailabilityStatus().status(),
                locker.getFunctions()
        );
    }
}