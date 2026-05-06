package pl.oskarinio.inpoint.service;

import org.springframework.stereotype.Service;
import pl.oskarinio.inpoint.model.Coordinates;
import pl.oskarinio.inpoint.model.ParcelLocker;
import pl.oskarinio.inpoint.model.PointRequest;
import pl.oskarinio.inpoint.model.record.LocationDto;

@Service
public class DistanceCalculatorService {

    public double calculateDistance(PointRequest request, ParcelLocker locker) {
        Coordinates coordinates = calculateCoordinates(request, locker);
        return calculateHaversinePattern(coordinates);
    }

    private Coordinates calculateCoordinates(PointRequest request, ParcelLocker locker){
        LocationDto lockerLocation = locker.getLocation();
        double differenceLatitude = Math.toRadians(lockerLocation.latitude() - request.getLatitude());
        double differenceLongitude = Math.toRadians(lockerLocation.longitude() - request.getLongitude());
        return new Coordinates(
                differenceLatitude,
                differenceLongitude,
                lockerLocation.latitude(),
                request.getLatitude()
        );
    }

    private double calculateHaversinePattern(Coordinates coordinates){
        double earthRadius = 6371;
        double factor = Math.sin(coordinates.getDifferenceLatitude() / 2) * Math.sin(coordinates.getDifferenceLatitude() / 2) +
                Math.cos(Math.toRadians(coordinates.getLockerLatitude())) * Math.cos(Math.toRadians(coordinates.getRequestLatitude())) *
                        Math.sin(coordinates.getDifferenceLongitude() / 2) * Math.sin(coordinates.getDifferenceLongitude() / 2);
        return earthRadius * 2 * Math.atan2(Math.sqrt(factor), Math.sqrt(1 - factor));

    }
}
