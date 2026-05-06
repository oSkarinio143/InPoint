package pl.oskarinio.inpoint.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.oskarinio.inpoint.model.ParcelLocker;
import pl.oskarinio.inpoint.model.request.PointRequest;
import pl.oskarinio.inpoint.model.PointResult;

import java.util.List;
import java.util.Map;

import static reactor.netty.http.HttpConnectionLiveness.log;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final FilterService filterService;
    private final RankingService rankingService;

    public List<PointResult> handleRequest(PointRequest pointRequest) {
        log.info("Ranking process started for coordinates: [{}, {}]",
                pointRequest.getLatitude(), pointRequest.getLongitude());
        List<ParcelLocker> filtered = filterService.getNearbyLockers(pointRequest);
        log.info("Filtering completed. Found {} candidates.", filtered.size());

        return rankingService.rankPoints(filtered, pointRequest);
    }
}
