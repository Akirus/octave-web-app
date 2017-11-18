package me.alextur.octave.rest;

import me.alextur.octave.model.DataException;
import me.alextur.octave.model.DocumentRepository;
import me.alextur.octave.model.beans.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author Alex Turchynovich
 */
@Component
@Produces(MediaType.APPLICATION_JSON)
@Path(DocumentLinkManager.ROOT_PATH)
public class DocumentsEndpoint extends BaseEndpoint{

    private DocumentRepository documentRepository;
    private DocumentLinkManager documentLinkManager;

    public DocumentsEndpoint(@Autowired DocumentRepository pDocumentRepository,
                             @Autowired DocumentLinkManager pDocumentLinkManager) {
        documentRepository = pDocumentRepository;
        documentLinkManager = pDocumentLinkManager;
    }

    @GET
    @Path(DocumentLinkManager.ALL_PATH)
    public Response all(@DefaultValue("false") @QueryParam("withContent") boolean withContent) {
        try {
            List<Document> documents = documentRepository.getAll(withContent);
            for(Document document : documents){
                populateAddinationalData(document);
            }

            return Response.ok(documents).build();
        } catch (DataException pE) {
            getLogger().error("unable to get all documents", pE);
            return genericFail();
        }
    }

    @GET
    @Path("{id}")
    public Response single(@PathParam("id") String id) {
        try {
            Document result = documentRepository.get(id);
            populateAddinationalData(result);
            if(result == null){
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(result).build();
        } catch (DataException pE) {
            getLogger().error("unable to get document", pE);
            return genericFail();
        }
    }

    private void populateAddinationalData(Document pDocument) {
        pDocument.getAdditionalData().put("link",
                documentLinkManager.getLink(pDocument.getId()));
    }

}
