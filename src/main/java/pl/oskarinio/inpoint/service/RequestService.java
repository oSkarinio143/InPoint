package pl.oskarinio.inpoint.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.oskarinio.inpoint.model.ParcelLocker;
import pl.oskarinio.inpoint.model.PointRequest;
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

        List<PointResult> responseList = rankingService.rankPoints(filtered, pointRequest);
//        List<List<String>> airLevels = responseList.stream()
//                .map(PointResult::getFunctions)
//                .distinct()                          // Zostaw tylko unikalne nazwy (np. raz "GOOD", raz "POOR")
//                .toList();                           // Zbierz do listy
//
//        System.out.println("Dostępne statusy powietrza: " + airLevels);
//
//        responseList.forEach(v ->
//                System.out.println(v.getFunctions().size()));
//
//        List<Map<String, Object>> uniqueAvailabilities = responseList.stream()
//                .map(PointResult::getLockerAvailability) // Pobieramy pole Map<String, Object>
//                .distinct()                               // Dzięki Map.equals() zostaną tylko unikalne zestawy danych
//                .toList();
//
//        System.out.println("Unikalne struktury dostępności: " + uniqueAvailabilities);

        return responseList;
    }
}
