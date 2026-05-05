package pl.oskarinio.inpoint.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.oskarinio.inpoint.model.AddressDto;
import pl.oskarinio.inpoint.model.ParcelLocker;
import pl.oskarinio.inpoint.model.PointRankingRequest;
import pl.oskarinio.inpoint.model.PointRankingResponse;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class RankingService {

    private final ParcelLockerFetchingService parcelLockerFetchingService;
    private static final double PROXIMITY_THRESHOLD = 0.1;

    public PointRankingResponse handleRanking(PointRankingRequest pointRankingRequest) {
        log.info("Ranking process started for coordinates: [{}, {}]",
                pointRankingRequest.getLatitude(), pointRankingRequest.getLongitude());
        List<ParcelLocker> filtered = getNearbyLockers(pointRankingRequest);
        log.info("Filtering completed. Found {} candidates.", filtered.size());

        return filtered.isEmpty() ? null : mapParcelToResponse(filtered.get(0));
    }

    private List<ParcelLocker> getNearbyLockers(PointRankingRequest request) {
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

    private PointRankingResponse mapParcelToResponse(ParcelLocker locker) {
        return new PointRankingResponse(
                0.0,
                locker.getName(),
                formatAddress(locker.getAddress()),
                locker.getLocation().latitude(),
                locker.getLocation().longitude()
        );
    }

    private String formatAddress(AddressDto address) {
        if (address == null) {
            return "No address data";
        }

        return Stream.of(address.line1(), address.line2())
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .collect(Collectors.joining(" "));
    }
}