package me.alextur.matlab.rest.endpoints.media;

import me.alextur.matlab.model.media.Media;
import me.alextur.matlab.repository.media.MediaRepository;
import me.alextur.matlab.rest.BaseEndpoint;
import me.alextur.matlab.service.media.CreateMediaRequest;
import me.alextur.matlab.service.media.MediaService;
import me.alextur.matlab.servlet.MediaServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.binary.Base64;

/**
 * @author Alex Turchynovich
 */
@Component
@Produces(MediaType.APPLICATION_JSON)
@Path("media")
public class MediaEndpoint extends BaseEndpoint {

    private MediaService mediaService;

    public MediaEndpoint(@Autowired MediaService pMediaService) {
        mediaService = pMediaService;
    }

    @PUT
    @Path("new")
    @Transactional
    public Response createMedia(CreateMediaRequest pCreateMediaRequest) {
        Media media = mediaService.createMedia(pCreateMediaRequest);

        return Response.ok(media).build();
    }

}
