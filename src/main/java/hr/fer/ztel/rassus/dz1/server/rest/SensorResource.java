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

@Log4j2
@Path("sensor")
public class SensorResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerSensor(String json) {
        log.info("POST /measurements/sensor: {}", json);
        if (json == null) {
            return Response.status(200).entity(false).build();
        }

        try {
            Gson gson = new Gson();
            Sensor sensor = gson.fromJson(json, Sensor.class);

            boolean success = Memory.registerSensor(sensor);
            log.info("{} registered sensor {}", (success ? "Successfully" : "Unsuccessfully"), sensor.getUsername());
            return Response.status(201).entity(success).build();
        } catch (Exception e) {
            log.info("Invalid data for sensor: {}", json);
            return Response.status(400).entity(false).build();
        }
    }

    @DELETE
    @Path("/{username}")
    public Response deregisterSensor(@PathParam("username") String username) {
        log.info("DELETE /measurements/sensor/{}", username);
        Sensor sensor = Memory.getSensorForName(username);

        if (sensor == null) {
            log.info("Sensor with name {} not found", username);
            return Response.status(404).build();
        }

        boolean success = Memory.deregisterSensor(sensor);
        if (success) {
            log.info("Successfully deregistered sensor {}", sensor);
            return Response.status(200).build();
        }

        log.info("Problem encountered while deregistering sensor {}", sensor);
        return Response.status(500).build();
    }

    @GET
    @Path("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClosestSensor(@PathParam("username") String username) {
        log.info("GET /measurements/sensor/{}", username);

        Sensor thisSensor = Memory.getSensorForName(username);
        Sensor closestSensor = Memory.getClosestSensor(thisSensor);
        log.info("Closest sensor to {} is {}", thisSensor, closestSensor);

        return Response.status(200).entity(closestSensor).build();
    }

    @POST
    @Path("/{username}/measure")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postMeasurement(@PathParam("username") String username, String json) {
        log.info("POST /measurements/sensor/{}/measure {}", username, json);

        Sensor sensor = Memory.getSensorForName(username);
        if (sensor == null) {
            log.info("Sensor with name {} not found", username);
            return Response.status(404).entity(false).build();
        }

        try {
            Gson gson = new Gson();
            Measurement measurement = gson.fromJson(json, Measurement.class);

            boolean success = Memory.storeMeasurement(username, measurement);
            log.info("{} stored measurement {} for sensor {}",
                    (success ? "Successfully" : "Unsuccessfully"), measurement, sensor);
            return Response.status(201).entity(success).build();
        } catch (Exception e) {
            log.info("Invalid data for measurement: {}", json);
            return Response.status(400).entity(false).build();
        }
    }
}
