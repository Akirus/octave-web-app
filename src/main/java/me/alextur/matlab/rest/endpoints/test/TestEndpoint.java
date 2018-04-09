package me.alextur.matlab.rest.endpoints.test;

import me.alextur.matlab.model.Document;
import me.alextur.matlab.model.TreeEntity;
import me.alextur.matlab.model.test.Test;
import me.alextur.matlab.model.user.User;
import me.alextur.matlab.repository.document.FolderRepository;
import me.alextur.matlab.repository.document.TreeRepository;
import me.alextur.matlab.repository.test.TestRepository;
import me.alextur.matlab.rest.BaseEndpoint;
import me.alextur.matlab.rest.auth.PermitRoles;
import me.alextur.matlab.rest.endpoints.documents.DocumentLinkManager;
import me.alextur.matlab.rest.endpoints.documents.model.ChangeDocumentRequest;
import me.alextur.matlab.rest.endpoints.test.model.ChangeTestRequest;
import me.alextur.matlab.service.document.DocumentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Alex Turchynovich
 */
@Component
@Produces(MediaType.APPLICATION_JSON)
@Path("tests")
public class TestEndpoint extends BaseEndpoint {


    private final DocumentsService documentsService;
    private final TreeRepository treeRepository;
    private final TestRepository testRepository;

    public TestEndpoint(@Autowired DocumentsService pDocumentsService,
                        @Autowired TreeRepository pTreeRepository,
                        @Autowired TestRepository pTestRepository) {
        documentsService = pDocumentsService;
        treeRepository = pTreeRepository;
        testRepository = pTestRepository;
    }

    private void updateTest(ChangeTestRequest pUpdateDocumentRequest, Test result){
        if(pUpdateDocumentRequest.getIntroductionText() != null){
            result.setIntroductionText(pUpdateDocumentRequest.getIntroductionText());
        }
        if(pUpdateDocumentRequest.getName() != null){
            result.setName(pUpdateDocumentRequest.getName());
        }
        if(pUpdateDocumentRequest.getParentId() != null){
            if(pUpdateDocumentRequest.getParentId() == -1){
                result.setParent(null);
            }
            else {
                TreeEntity parent = treeRepository.findById(pUpdateDocumentRequest.getParentId());
                if (parent == null) {
                    badRequestFail("Could not find parent with id: " + pUpdateDocumentRequest.getParentId());
                }
                result.setParent(parent);
            }
        }

        documentsService.populateVisibility(pUpdateDocumentRequest, result);
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

        Test result = testRepository.findOne(id);
        if(result == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if(!documentsService.hasAccessTo(user, result)){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        documentsService.populateAdditionalData(result);

        return Response.ok(result).build();
    }


    @PUT
    @Path("create")
    @PermitRoles(roles = {"Admin", "Teacher"})
    @Transactional
    public Response create(ChangeTestRequest pUpdateDocumentRequest) {
        Test result = new Test();

        updateTest(pUpdateDocumentRequest, result);
        result = testRepository.saveAndFlush(result);

        documentsService.populateAdditionalData(result);

        return Response.ok(result).build();
    }

    @POST
    @Path("{id}/update")
    @PermitRoles(roles = {"Admin", "Teacher"})
    @Transactional
    public Response edit(@PathParam("id") Long id, ChangeTestRequest pChangeTestRequest) {
        Test result = testRepository.findById(id);
        if(result == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        updateTest(pChangeTestRequest, result);
        documentsService.populateVisibility(pChangeTestRequest, result);

        result = testRepository.saveAndFlush(result);

        documentsService.populateAdditionalData(result);

        return Response.ok(result).build();
    }


}
