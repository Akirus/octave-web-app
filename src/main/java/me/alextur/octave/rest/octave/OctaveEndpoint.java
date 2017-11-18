package me.alextur.octave.rest.octave;

import me.alextur.octave.octave.ExecutionRequest;
import me.alextur.octave.octave.ExecutionResult;
import me.alextur.octave.octave.OctaveExecutor;
import me.alextur.octave.rest.BaseEndpoint;
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

    private OctaveExecutor executor;

    public OctaveEndpoint(@Autowired OctaveExecutor pExecutor) {
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
