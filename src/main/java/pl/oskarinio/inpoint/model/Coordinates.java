package pl.oskarinio.inpoint.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Coordinates {
    private double differenceLatitude;
    private double differenceLongitude;
    private double lockerLatitude;
    private double requestLatitude;
}
