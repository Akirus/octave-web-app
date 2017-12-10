package me.alextur.matlab.rest.endpoints.documents;

import me.alextur.matlab.repository.DataException;
import me.alextur.matlab.repository.document.DocumentRepository;
import me.alextur.matlab.model.Document;
import me.alextur.matlab.rest.BaseEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
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
public class DocumentsEndpoint extends BaseEndpoint {

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
        List<Document> documents = documentRepository.findAll();
        for(Document document : documents){
            populateAddinationalData(document);
        }

        return Response.ok(documents).build();
    }

    @GET
    @Path("{id}")
    @Transactional
    public Response single(@PathParam("id") Long id) {
        Document result = documentRepository.findOne(id);
        if(result == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        populateAddinationalData(result);

        return Response.ok(result).build();
    }

    private void populateAddinationalData(Document pDocument) {
        pDocument.getAdditionalData().put("link",
                documentLinkManager.getLink(pDocument.getId()));
    }

}
