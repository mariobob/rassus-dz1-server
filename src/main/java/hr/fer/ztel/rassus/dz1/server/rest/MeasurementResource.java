package hr.fer.ztel.rassus.dz1.server.rest;

import com.google.gson.Gson;
import hr.fer.ztel.rassus.dz1.server.model.Measurement;
import hr.fer.ztel.rassus.dz1.server.model.Sensor;
import hr.fer.ztel.rassus.dz1.server.util.Memory;
import lombok.extern.log4j.Log4j2;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@Log4j2
@Path("measurements")
public class MeasurementResource {

    /**
     * Returns all measurements.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllMeasurements() {
        log.info("GET /measurementhost/rest/measurements");

        Map<Sensor, List<Measurement>> measurements = Memory.getAllMeasurements();
        log.debug("  Measurements: {}", measurements);

        Gson gson = new Gson();
        String json = gson.toJson(measurements);

        return Response.status(200).entity(json).build();
    }
}
