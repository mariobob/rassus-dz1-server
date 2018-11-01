package hr.fer.ztel.rassus.dz1.server.util;

import hr.fer.ztel.rassus.dz1.server.model.Measurement;
import hr.fer.ztel.rassus.dz1.server.model.Sensor;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class keeps track of data for sensors and measurements.
 */
public class Memory {

    private static Set<Sensor> sensorSet = new HashSet<>();
    private static Map<String, List<Measurement>> measurementMap = new HashMap<>();

    public static boolean registerSensor(Sensor sensor) {
        return sensorSet.add(sensor);
    }

    public static Sensor getClosestSensor(Sensor sensor) {
        if (sensorSet.size() == 1) {
            return null;
        }

        return sensorSet.stream()
                .filter(s -> !s.equals(sensor))
                .min(Comparator.comparingDouble(s -> Utility.distance(sensor.getLocation(), s.getLocation())))
                .orElse(null);
    }

    public static Sensor getSensorForName(String username) {
        return sensorSet.stream()
                .filter(s -> s.getUsername().equals(username))
                .findAny()
                .orElse(null);
    }

    public static boolean deregisterSensor(Sensor sensor) {
        return sensorSet.remove(sensor);
    }

    public static boolean storeMeasurement(String username, Measurement measurement) {
        List<Measurement> list = measurementMap.computeIfAbsent(username, k -> new LinkedList<>());
        list.add(measurement);

        return true;
    }
}
