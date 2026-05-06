package pl.oskarinio.inpoint.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.oskarinio.inpoint.model.AddressDto;
import pl.oskarinio.inpoint.model.ParcelLocker;
import pl.oskarinio.inpoint.model.PointRequest;
import pl.oskarinio.inpoint.model.PointResult;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class FilterService {

    private static final double PROXIMITY_THRESHOLD = 0.1;

    private final ParcelLockerFetchingService parcelLockerFetchingService;

    public List<ParcelLocker> getNearbyLockers(PointRequest request) {
        return parcelLockerFetchingService.getAllLockersAsList().parallelStream()
                .filter(locker -> isWithinRange(locker, request.getLatitude(), request.getLongitude()))
                .collect(Collectors.toList());
    }

    private boolean isWithinRange(ParcelLocker locker, double userLatitude, double userLongitude) {
        if (locker.getLocation() == null) {
            return false;
        }

        double latitudeDifference = Math.abs(locker.getLocation().latitude() - userLatitude);
        double longitudeDifference = Math.abs(locker.getLocation().longitude() - userLongitude);

        return latitudeDifference <= PROXIMITY_THRESHOLD && longitudeDifference <= PROXIMITY_THRESHOLD;
    }
}
