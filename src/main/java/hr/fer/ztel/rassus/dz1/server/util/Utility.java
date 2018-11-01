package hr.fer.ztel.rassus.dz1.server.util;

import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

public class Utility {

    /** Disable instantiation. */
    private Utility() {}

    public static double distance(MapLocation location1, MapLocation location2) {
        double lat1 = location1.getLatitude();
        double lon1 = location1.getLongitude();
        double lat2 = location2.getLatitude();
        double lon2 = location2.getLongitude();

        final double R = 6371.0;
        double dlat = lat2 - lat1;
        double dlon = lon2 - lon1;

        double a = pow(sin(dlat / 2), 2) + cos(lat1) * cos(lat2) * pow(sin(dlon / 2), 2);
        double c = 2 * atan2(sqrt(a), sqrt(1 - a));
        return R * c;
    }

}
