package pl.oskarinio.inpoint.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.oskarinio.inpoint.model.PointRequest;
import pl.oskarinio.inpoint.model.PointResult;
import pl.oskarinio.inpoint.service.RequestService;

import java.util.List;

@RestController
@RequestMapping("inpoint/rank")
@RequiredArgsConstructor
public class RankingController {

    private final RequestService requestService;

    @PostMapping("/points")
    public ResponseEntity<List<PointResult>> findBestPoint(@Valid @RequestBody PointRequest pointRequest){
        List<PointResult> response = requestService.handleRequest(pointRequest);
        System.out.println(response);
        return ResponseEntity.ok(response);
    }
}
