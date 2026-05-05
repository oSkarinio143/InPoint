package pl.oskarinio.inpoint.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.oskarinio.inpoint.model.ParcelLocker;
import pl.oskarinio.inpoint.model.PointRankingRequest;
import pl.oskarinio.inpoint.model.PointRankingResponse;

import java.util.List;

import static reactor.netty.http.HttpConnectionLiveness.log;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final FilterService filterService;

    public PointRankingResponse handleRequest(PointRankingRequest pointRankingRequest) {
        log.info("Ranking process started for coordinates: [{}, {}]",
                pointRankingRequest.getLatitude(), pointRankingRequest.getLongitude());
        List<ParcelLocker> filtered = filterService.getNearbyLockers(pointRankingRequest);
        log.info("Filtering completed. Found {} candidates.", filtered.size());

        return filtered.isEmpty() ? null : filterService.mapParcelToResponse(filtered.get(0));
    }
}
