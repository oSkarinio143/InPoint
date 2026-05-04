package pl.oskarinio.inpoint.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointRankingResponse {

    private double totalScore;
    private String name;
    private String fullAddress;
    private double latitude;
    private double longitude;

}