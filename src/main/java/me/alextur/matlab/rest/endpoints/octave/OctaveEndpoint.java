package me.alextur.matlab.rest.endpoints.octave;

import me.alextur.matlab.rest.BaseEndpoint;
import me.alextur.matlab.service.octave.OctaveExecutorService;
import me.alextur.matlab.service.octave.ExecutionRequest;
import me.alextur.matlab.service.octave.ExecutionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 * @author Alex Turchynovich
 */
@Component
@Produces(MediaType.APPLICATION_JSON)
@Path("octave")
public class OctaveEndpoint extends BaseEndpoint {

    private OctaveExecutorService executor;

    public OctaveEndpoint(@Autowired OctaveExecutorService pExecutor) {
        executor = pExecutor;
    }

    @POST
    @Path("execute")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response execute(ExecutionRequest pRequest){
        ExecutionResult result = executor.execute(pRequest);

        return Response.ok(result).build();
    }

}
