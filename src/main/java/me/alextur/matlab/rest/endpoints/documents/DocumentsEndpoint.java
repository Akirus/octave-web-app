package me.alextur.matlab.rest.endpoints.documents;

import me.alextur.matlab.model.Folder;
import me.alextur.matlab.model.TreeEntity;
import me.alextur.matlab.repository.document.DocumentRepository;
import me.alextur.matlab.model.Document;
import me.alextur.matlab.repository.document.TreeRepository;
import me.alextur.matlab.rest.BaseEndpoint;
import me.alextur.matlab.rest.auth.PermitRoles;
import me.alextur.matlab.rest.endpoints.documents.model.ChangeDocumentRequest;
import me.alextur.matlab.service.document.DocumentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
    private TreeRepository treeRepository;
    private DocumentLinkManager documentLinkManager;
    private DocumentsService documentsService;

    public DocumentsEndpoint(@Autowired DocumentRepository pDocumentRepository,
                             @Autowired DocumentLinkManager pDocumentLinkManager,
                             @Autowired DocumentsService pDocumentsService,
                             @Autowired TreeRepository pTreeRepository) {
        documentRepository = pDocumentRepository;
        documentLinkManager = pDocumentLinkManager;
        documentsService = pDocumentsService;
        treeRepository = pTreeRepository;
    }

    @GET
    @Path(DocumentLinkManager.ALL_PATH)
    public Response all(@DefaultValue("false") @QueryParam("withContent") boolean withContent) {
        List<Document> documents = documentRepository.findAll(new Sort(Sort.Direction.ASC, "order"));
        for(Document document : documents){
            populateAddinationalData(document);
        }

        return Response.ok(documents).build();
    }

    @GET
    @Path("tree")
    public Response tree(@DefaultValue("false") @QueryParam("withContent") boolean withContent) {
        List<TreeEntity> documents = treeRepository.findByParentIsNullOrderByName();
        documentsService.expandTree(documents);

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

    @POST
    @Path("{id}/update")
    @PermitRoles(roles = {"Admin", "Teacher"})
    @Transactional
    public Response edit(@PathParam("id") Long id, ChangeDocumentRequest pUpdateDocumentRequest) {
        Document result = documentRepository.findOne(id);
        if(result == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if(pUpdateDocumentRequest.getContent() != null){
            result.setContent(pUpdateDocumentRequest.getContent());
        }
        if(pUpdateDocumentRequest.getName() != null){
            result.setName(pUpdateDocumentRequest.getName());
        }
        if(pUpdateDocumentRequest.getParentId() != null){
            Long parentId = pUpdateDocumentRequest.getParentId();
            if(parentId == -1){
                result.setParent(null);
            }
            else {
                TreeEntity parent = treeRepository.findOne(pUpdateDocumentRequest.getParentId());
                if (parent != null) {
                    result.setParent(parent);
                } else {
                    badRequestFail("Could not find parent with id: " + pUpdateDocumentRequest.getParentId());
                }
            }
        }

        result = documentsService.updateDocument(result);

        return Response.ok(result).build();
    }

    @PUT
    @Path("create")
    @PermitRoles(roles = {"Admin", "Teacher"})
    @Transactional
    public Response create(ChangeDocumentRequest pUpdateDocumentRequest) {
        Document result = new Document();

        if(pUpdateDocumentRequest.getContent() != null){
            result.setContent(pUpdateDocumentRequest.getContent());
        }
        if(pUpdateDocumentRequest.getName() != null){
            result.setName(pUpdateDocumentRequest.getName());
        }

        result = documentsService.createDocument(result, pUpdateDocumentRequest.getParentId());

        return Response.ok(result).build();
    }

    @PUT
    @Path("folder")
    @PermitRoles(roles = {"Admin", "Teacher"})
    @Transactional
    public Response createFolder(ChangeDocumentRequest pUpdateDocumentRequest) {
        Folder result = new Folder();

        if(pUpdateDocumentRequest.getName() != null){
            result.setName(pUpdateDocumentRequest.getName());
        }

        result = documentsService.createFolder(result, pUpdateDocumentRequest.getParentId());

        return Response.ok(result).build();
    }


    @POST
    @Path("{id}/delete")
    @PermitRoles(roles = {"Admin", "Teacher"})
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        TreeEntity result = treeRepository.findOne(id);
        if(result == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        treeRepository.delete(result);

        return Response.ok().build();
    }

    private void populateAddinationalData(Document pDocument) {
        pDocument.getAdditionalData().put("link",
                documentLinkManager.getLink(pDocument.getId()));
    }

}
