package pl.oskarinio.inpoint.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.oskarinio.inpoint.model.ParcelLocker;
import pl.oskarinio.inpoint.model.request.ParcelLockerAvailabilityStatus;
import pl.oskarinio.inpoint.model.request.ParcelLockerType;
import pl.oskarinio.inpoint.model.request.PointRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScoreCalculatorService {

    private static final double K_SENSITIVITY = 0.067;

    private final DistanceCalculatorService distanceCalculatorService;

    public double countDistanceScore(PointRequest request, ParcelLocker locker){
        double distance = distanceCalculatorService.calculateDistance(request, locker);
        double maxDistance = request.getMaxDistance();
        if (distance <= maxDistance) {
            return (maxDistance - distance) / (maxDistance * (1 + K_SENSITIVITY * distance));
        }
        return 0;
    }

    public double countLocation247Score(ParcelLocker locker) {
        return locker.isLocation247() ? 1.0 : 0.0;
    }

    public double countIsNextScore(ParcelLocker locker) {
        return locker.isNext() ? 1.0 : 0.0;
    }

    public double countEasyAccessScore(ParcelLocker locker) {
        return locker.isEasyAccessZone() ? 1.0 : 0.0;
    }

    public double countPaymentScore(ParcelLocker locker) {
        return locker.isPaymentAvailable() ? 1.0 : 0.0;
    }

    public double countIndoorScore(ParcelLocker locker) {
        return "Indoor".equalsIgnoreCase(locker.getLocationType()) ? 1.0 : 0.0;
    }

    public double countPrintInStoreScore(ParcelLocker locker) {
        return Boolean.TRUE.equals(locker.getPrintInStore()) ? 1.0 : 0.0;
    }

    public double countAirIndexScore(ParcelLocker locker) {
        String level = locker.getAirIndexLevel();
        if (level == null) {
            return 0.0;
        }

        return switch (level.toUpperCase()) {
            case "VERY_GOOD" -> 1.0;
            case "GOOD" -> 0.8;
            case "SATISFACTORY" -> 0.6;
            case "MODERATE" -> 0.4;
            case "BAD" -> 0.2;
            case "VERY_BAD" -> 0;
            default -> 0.0;
        };
    }

    public double countTypeScore(PointRequest request, ParcelLocker locker) {
        ParcelLockerType preferredType = request.getType();
        List<ParcelLockerType> lockerTypes = locker.getType();
        if (lockerTypes == null || lockerTypes.isEmpty()) {
            return 0.0;
        }

        if (preferredType == null || lockerTypes.contains(preferredType)) {
            return 1.0;
        }
        return 0.0;
    }

    public double countAvailabilityStatusScore(ParcelLocker locker){
        ParcelLockerAvailabilityStatus status = locker.getLockerAvailabilityStatus().status();
        if (status == null) {
            return 0.0;
        }

        return switch (status) {
            case NORMAL -> 1.0;
            case LOW -> 0.3;
            case VERY_LOW -> 0.1;
            case NO_DATA -> 0;
        };
    }
}