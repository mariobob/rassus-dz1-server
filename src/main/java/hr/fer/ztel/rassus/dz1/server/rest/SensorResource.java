package hr.fer.ztel.rassus.dz1.server.rest;

import com.google.gson.Gson;
import hr.fer.ztel.rassus.dz1.server.model.Measurement;
import hr.fer.ztel.rassus.dz1.server.model.Sensor;
import hr.fer.ztel.rassus.dz1.server.util.Memory;
import lombok.extern.log4j.Log4j2;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

@Log4j2
@Path("sensors")
public class SensorResource {
    // http://localhost:8080/measurements/rest/sensors/

    /**
     * Registers a sensor constructed from the specified json string.
     * Returns true if the sensor is successfully registered, false
     * if json is empty or if the given json is malformed.
     *
     * @param json sensor json
     * @return true if successful, false otherwise
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response registerSensor(String json) {
        log.info("POST /measurements/rest/sensors");

        if (json == null || json.isEmpty()) {
            return Response.status(400).entity(false).build();
        }
        log.debug(json);

        try {
            Gson gson = new Gson();
            Sensor sensor = gson.fromJson(json, Sensor.class);

            boolean success = Memory.registerSensor(sensor);
            if (success) {
                log.info("  Successfully registered sensor: {}", sensor);
            } else {
                log.warn("  Sensor already exists: {}", sensor);
            }
            return Response.status(201).entity(success).build();
        } catch (Exception e) {
            log.warn("  Invalid sensor data for json: \n{}", json);
            return Response.status(400).entity(false).build();
        }
    }

    /**
     * Returns all sensors.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSensors() {
        log.info("GET /measurements/rest/sensors");

        Collection<Sensor> sensors = Memory.getAllSensors();
        log.debug("  Sensors: {}", sensors);

        Gson gson = new Gson();
        String json = gson.toJson(sensors);

        return Response.status(200).entity(json).build();
    }

    /**
     * Returns a sensor with the specified username.
     */
    @GET
    @Path("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSensor(@PathParam("username") String username) {
        log.info("GET /measurements/rest/sensors/{}", username);

        Sensor sensor = Memory.getSensorForName(username);
        if (sensor == null) {
            log.warn("  Sensor not found: {}", username);
            return Response.status(404).entity(null).build();
        }

        log.debug("  Sensor: {}", sensor);

        Gson gson = new Gson();
        String json = gson.toJson(sensor);

        return Response.status(200).entity(json).build();
    }

    /**
     * Deregisters the specified sensor from memory.
     */
    @DELETE
    @Path("/{username}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deregisterSensor(@PathParam("username") String username) {
        log.info("DELETE /measurements/rest/sensors/{}", username);
        Sensor sensor = Memory.getSensorForName(username);

        if (sensor == null) {
            log.warn("  Sensor not found: {}", username);
            return Response.status(404).entity(false).build();
        }

        boolean success = Memory.deregisterSensor(sensor);
        if (success) {
            log.info("  Successfully deregistered sensor: {}", sensor.getUsername());
        } else {
            log.warn("  Failed to register sensor: {}", sensor);
        }
        return Response.status(500).entity(success).build();
    }

    /**
     * Returns the closest sensor to the specified sensor.
     */
    @GET
    @Path("/{username}/closest")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClosestSensor(@PathParam("username") String username) {
        log.info("GET /measurements/rest/sensors/{}/closest", username);

        Sensor thisSensor = Memory.getSensorForName(username);
        if (thisSensor == null) {
            log.warn("  This sensor is currently not registered: {}", username);
            return Response.status(404).entity(null).build();
        }

        Sensor closestSensor = Memory.getClosestSensor(thisSensor);
        log.info("  Closest sensor: {}", closestSensor);

        return Response.status(200).entity(closestSensor).build();
    }

    /**
     * Returns all measurements for the specified sensor.
     */
    @GET
    @Path("/{username}/measurements")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMeasurementsForSensor(@PathParam("username") String username) {
        log.info("GET /measurements/rest/sensors/{}/measurements", username);

        Sensor sensor = Memory.getSensorForName(username);
        if (sensor == null) {
            log.warn("  Sensor not found: {}", username);
            return Response.status(404).entity(null).build();
        }

        Collection<Measurement> measurements = Memory.getMeasurementsForSensor(sensor);
        log.debug("  Measurements: {}", measurements);

        Gson gson = new Gson();
        String json = gson.toJson(measurements);

        return Response.status(200).entity(json).build();
    }

    /**
     * Publishes a measurement for the specified sensor.
     */
    @POST
    @Path("/{username}/measurements")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response postMeasurement(@PathParam("username") String username, String json) {
        log.info("POST /measurements/rest/sensors/{}/measurements", username);
        log.debug(json);

        Sensor sensor = Memory.getSensorForName(username);
        if (sensor == null) {
            log.warn("  Sensor not found: {}", username);
            return Response.status(404).entity(false).build();
        }

        try {
            Gson gson = new Gson();
            Measurement measurement = gson.fromJson(json, Measurement.class);

            boolean success = Memory.storeMeasurement(sensor, measurement);
            log.info("  {} stored measurement for sensor {}: {}",
                    (success ? "Successfully" : "Unsuccessfully"), sensor.getUsername(), measurement);
            return Response.status(201).entity(success).build();
        } catch (Exception e) {
            log.warn("  Invalid measurement data for json: \n{}", json);
            return Response.status(400).entity(false).build();
        }
    }
}
