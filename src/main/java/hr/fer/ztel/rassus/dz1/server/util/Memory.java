package hr.fer.ztel.rassus.dz1.server.util;

import hr.fer.ztel.rassus.dz1.server.model.Measurement;
import hr.fer.ztel.rassus.dz1.server.model.Sensor;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class keeps track of data for sensors and measurements.
 */
public class Memory {

    private static Map<Sensor, List<Measurement>> sensorMeasurementsMap = new HashMap<>();

    public static boolean registerSensor(Sensor sensor) {
        if (sensorMeasurementsMap.containsKey(sensor)) {
            return false;
        }

        sensorMeasurementsMap.put(sensor, new LinkedList<>());
        return true;
    }

    public static Collection<Sensor> getAllSensors() {
        return Collections.unmodifiableCollection(sensorMeasurementsMap.keySet());
    }

    public static Sensor getClosestSensor(Sensor sensor) {
        if (sensor == null || sensorMeasurementsMap.size() == 1 && sensorMeasurementsMap.containsKey(sensor)) {
            // If this is the only sensor in memory
            return null;
        }

        return sensorMeasurementsMap.keySet().stream()
                .filter(s -> !s.equals(sensor))
                .min(Comparator.comparingDouble(s -> Utility.distance(sensor.getLocation(), s.getLocation())))
                .orElse(null);
    }

    public static Sensor getSensorForName(String username) {
        return sensorMeasurementsMap.keySet().stream()
                .filter(s -> s.getUsername().equals(username))
                .findAny()
                .orElse(null);
    }

    public static boolean deregisterSensor(Sensor sensor) {
        return (sensorMeasurementsMap.remove(sensor) != null);
    }

    public static boolean storeMeasurement(Sensor sensor, Measurement measurement) {
        List<Measurement> measurements = sensorMeasurementsMap.get(sensor);
        if (measurements == null) {
            return false;
        }

        return measurements.add(measurement);
    }

    public static Collection<Measurement> getMeasurementsForSensor(Sensor sensor) {
        if (!sensorMeasurementsMap.containsKey(sensor)) return null;
        else return Collections.unmodifiableCollection(sensorMeasurementsMap.get(sensor));
    }

    public static Map<Sensor, List<Measurement>> getAllMeasurements() {
        return Collections.unmodifiableMap(sensorMeasurementsMap);
    }
}
