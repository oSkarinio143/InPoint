package pl.oskarinio.inpoint.service;

import org.springframework.stereotype.Service;
import pl.oskarinio.inpoint.model.DistanceCalculationData;
import pl.oskarinio.inpoint.model.ParcelLocker;
import pl.oskarinio.inpoint.model.PointRequest;
import pl.oskarinio.inpoint.model.record.LocationDto;

@Service
public class DistanceCalculatorService {

    private static final double EARTH_RADIUS = 6371.0;

    public double calculateDistance(PointRequest request, ParcelLocker locker) {
        DistanceCalculationData distanceCalculationData = getDistanceCalculationData(request, locker);
        return calculateHaversinePattern(distanceCalculationData);
    }

    private DistanceCalculationData getDistanceCalculationData(PointRequest request, ParcelLocker locker){
        LocationDto lockerLocation = locker.getLocation();
        double differenceLatitude = Math.toRadians(lockerLocation.latitude() - request.getLatitude());
        double differenceLongitude = Math.toRadians(lockerLocation.longitude() - request.getLongitude());
        return new DistanceCalculationData(
                differenceLatitude,
                differenceLongitude,
                lockerLocation.latitude(),
                request.getLatitude()
        );
    }

    private double calculateHaversinePattern(DistanceCalculationData distanceCalculationData){
        double factor = Math.sin(distanceCalculationData.getDifferenceLatitude() / 2) * Math.sin(distanceCalculationData.getDifferenceLatitude() / 2) +
                Math.cos(Math.toRadians(distanceCalculationData.getLockerLatitude())) * Math.cos(Math.toRadians(distanceCalculationData.getRequestLatitude())) *
                        Math.sin(distanceCalculationData.getDifferenceLongitude() / 2) * Math.sin(distanceCalculationData.getDifferenceLongitude() / 2);
        return EARTH_RADIUS * 2 * Math.atan2(Math.sqrt(factor), Math.sqrt(1 - factor));

    }
}
