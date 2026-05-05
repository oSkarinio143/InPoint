package pl.oskarinio.inpoint.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.oskarinio.inpoint.model.ParcelLocker;
import pl.oskarinio.inpoint.model.PointRankingRequest;
import pl.oskarinio.inpoint.model.PointRankingResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final ParcelLockerFetchingService parcelLockerFetchingService;
    public PointRankingResponse handleRanking(PointRankingRequest pointRankingRequest) {
//        List<ParcelLocker> parcelLockers = parcelLockerFetchingService.getNearbyLockers(pointRankingRequest.getLatitude(), pointRankingRequest.getLongitude(), 0.1);
//        System.out.println("zadaanie");
//        if(parcelLockers.size() == 0)
//            System.out.println("z pustego to i solomon nie naleje]");
//        parcelLockers.forEach(v -> System.out.println(v + "\n"));
        return new PointRankingResponse();
    }
}
