package pl.oskarinio.inpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.oskarinio.inpoint.dto.PointRequest;
import pl.oskarinio.inpoint.dto.PointResult;
import pl.oskarinio.inpoint.engine.FilterService;
import pl.oskarinio.inpoint.engine.RankingService;
import pl.oskarinio.inpoint.integration.model.ParcelLocker;

import java.util.List;

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
        List<PointResult> rankedPoints = rankingService.rankPoints(filtered, pointRequest);
        log.info("Ranking completed. Sending result to user");
        return rankedPoints;
    }
}
