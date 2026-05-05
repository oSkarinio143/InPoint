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
    private final RankingService rankingService;

    public PointRankingResponse handleRequest(PointRankingRequest pointRankingRequest) {
        log.info("Ranking process started for coordinates: [{}, {}]",
                pointRankingRequest.getLatitude(), pointRankingRequest.getLongitude());
        List<ParcelLocker> filtered = filterService.getNearbyLockers(pointRankingRequest);

        ParcelLocker p = filtered.get(0);
        System.out.println(p.getStatus());
        System.out.println(p.getType());
        System.out.println(p.getName());
        System.out.println(p.getAddress());
        System.out.println(p.isLocation247());
        System.out.println(p.isNext());
        System.out.println(p.isEasyAccessZone());
        System.out.println(p.isPaymentAvailable());
        System.out.println(p.isPaymentAvailable());
        System.out.println(p.getLocationType());
        log.info("Filtering completed. Found {} candidates.", filtered.size());

        List<PointRankingResponse> responseList = rankingService.handleRanking(filtered, pointRankingRequest);
        responseList.forEach(v ->
                System.out.println(v));

        return filtered.isEmpty() ? null : filterService.mapParcelToResponse(filtered.get(0));
    }
}
