package pl.oskarinio.inpoint.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.oskarinio.inpoint.model.PointRankingRequest;
import pl.oskarinio.inpoint.model.PointRankingResponse;
import pl.oskarinio.inpoint.service.RankingService;

@RestController
@RequestMapping("inpoint/rank")
@RequiredArgsConstructor
public class RankingController {

    private final RankingService rankingService;

    @PostMapping("/points")
    public ResponseEntity<PointRankingResponse> findBestPoint(@Valid @RequestBody PointRankingRequest pointRankingRequest){
        PointRankingResponse response = rankingService.handleRanking(pointRankingRequest);
        System.out.println(response);
        return ResponseEntity.ok(response);
    }
}
