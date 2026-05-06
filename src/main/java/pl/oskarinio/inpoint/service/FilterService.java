package pl.oskarinio.inpoint.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.oskarinio.inpoint.model.api.ParcelLocker;
import pl.oskarinio.inpoint.model.PointRequest;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class FilterService {

    private final ParcelLockerFetchingService parcelLockerFetchingService;
    private final DistanceCalculatorService distanceCalculatorService;

    public List<ParcelLocker> getNearbyLockers(PointRequest request) {
        return parcelLockerFetchingService.getAllLockersAsList().parallelStream()
                .filter(locker -> locker.getLocation() != null)
                .filter(locker -> distanceCalculatorService.calculateDistance(request, locker) <= request.getMaxDistance())
                .collect(Collectors.toList());
    }
}
