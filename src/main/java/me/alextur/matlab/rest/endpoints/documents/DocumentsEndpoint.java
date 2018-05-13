package me.alextur.matlab.rest.endpoints.documents;

import me.alextur.matlab.model.Folder;
import me.alextur.matlab.model.TreeEntity;
import me.alextur.matlab.model.user.Role;
import me.alextur.matlab.model.user.StudentGroup;
import me.alextur.matlab.model.user.User;
import me.alextur.matlab.repository.document.DocumentRepository;
import me.alextur.matlab.model.Document;
import me.alextur.matlab.repository.document.TreeRepository;
import me.alextur.matlab.repository.user.GroupRepository;
import me.alextur.matlab.repository.user.RoleRepository;
import me.alextur.matlab.rest.BaseEndpoint;
import me.alextur.matlab.rest.auth.PermitRoles;
import me.alextur.matlab.rest.endpoints.documents.model.ChangeDocumentRequest;
import me.alextur.matlab.service.document.DocumentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.stream.Collectors;

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
    private GroupRepository groupRepository;
    private RoleRepository roleRepository;

    public DocumentsEndpoint(@Autowired DocumentRepository pDocumentRepository,
                             @Autowired DocumentLinkManager pDocumentLinkManager,
                             @Autowired DocumentsService pDocumentsService,
                             @Autowired TreeRepository pTreeRepository,
                             @Autowired GroupRepository groupRepository,
                             @Autowired RoleRepository roleRepository) {
        documentRepository = pDocumentRepository;
        documentLinkManager = pDocumentLinkManager;
        documentsService = pDocumentsService;
        treeRepository = pTreeRepository;
        this.groupRepository = groupRepository;
        this.roleRepository = roleRepository;
    }

    @GET
    @Path("tree")
    @Transactional
    public Response tree(@DefaultValue("false") @QueryParam("withContent") boolean withContent) {
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof User)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        User user = (User) authentication.getPrincipal();

        List<TreeEntity> documents = treeRepository.findByParentIsNullOrderByName();
        documents = documentsService.accessFilterEntries(user, documents);

        documentsService.expandTree(user, documents);

        return Response.ok(documents).build();
    }

    @GET
    @Path("{id}")
    @Transactional
    public Response single(@PathParam("id") Long id) {
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof User)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        User user = (User) authentication.getPrincipal();

        Document result = documentRepository.findOne(id);
        if(result == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if(!documentsService.hasAccessTo(user, result)){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        documentsService.populateAdditionalData(result);

        return Response.ok(result).build();
    }

    @GET
    @Path("{id}/visibility")
    @Transactional
    public Response visibility(@PathParam("id") Long id) {
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof User)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        User user = (User) authentication.getPrincipal();

        TreeEntity document = treeRepository.findOne(id);
        if(document == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if(!documentsService.hasAccessTo(user, document)){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        Map<String, Object> result = documentsService.getVisibility(document);

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
        documentsService.populateVisibility(pUpdateDocumentRequest, result);

        result = documentsService.updateDocument(result);

        documentsService.populateAdditionalData(result);

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

        documentsService.populateVisibility(pUpdateDocumentRequest, result);
        result = documentsService.createDocument(result, pUpdateDocumentRequest.getParentId());

        documentsService.populateAdditionalData(result);

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
        treeRepository.flush();
        documentRepository.flush();

        return Response.ok().build();
    }



}
