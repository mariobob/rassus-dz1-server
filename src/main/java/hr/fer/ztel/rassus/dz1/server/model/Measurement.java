package hr.fer.ztel.rassus.dz1.server.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Measurement {

    private final int temperature;
    private final int pressure;
    private final int humidity;
    private final Integer co;
    private final Integer no2;
    private final Integer so2;

}
