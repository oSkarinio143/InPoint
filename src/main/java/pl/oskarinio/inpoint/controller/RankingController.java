package pl.oskarinio.inpoint.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.oskarinio.inpoint.model.PointRankingRequest;
import pl.oskarinio.inpoint.model.PointRankingResponse;

@RestController
@RequestMapping("inpoint/rank")
public class RankingController {

    @PostMapping("/points")
    public ResponseEntity<PointRankingResponse> findBestPoint(@Valid @RequestBody PointRankingRequest pointRankingRequest){
        return ResponseEntity.ok(new PointRankingResponse());
    }
}
