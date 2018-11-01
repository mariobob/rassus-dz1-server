package hr.fer.ztel.rassus.dz1.server.model;

import hr.fer.ztel.rassus.dz1.server.util.MapLocation;
import lombok.Value;

@Value
public class Sensor {

    private final String username;
    private final String ipAddress;
    private final int port;
    private final MapLocation location;

}
