package pl.oskarinio.inpoint.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.oskarinio.inpoint.model.ParcelLocker;
import pl.oskarinio.inpoint.model.PointRequest;
import pl.oskarinio.inpoint.model.PointResult;

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

        List<PointResult> responseList = rankingService.rankPoints(filtered, pointRequest);
        List<PointResult> finalList = responseList.stream()
                        .limit(10).toList();
        return finalList;
    }
}
