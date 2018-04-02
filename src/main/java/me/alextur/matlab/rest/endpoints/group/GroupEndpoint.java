package me.alextur.matlab.rest.endpoints.group;

import me.alextur.matlab.model.Document;
import me.alextur.matlab.model.Folder;
import me.alextur.matlab.model.TreeEntity;
import me.alextur.matlab.model.user.StudentGroup;
import me.alextur.matlab.model.user.User;
import me.alextur.matlab.repository.user.GroupRepository;
import me.alextur.matlab.rest.BaseEndpoint;
import me.alextur.matlab.rest.auth.PermitAll;
import me.alextur.matlab.rest.auth.PermitRoles;
import me.alextur.matlab.rest.endpoints.documents.model.ChangeDocumentRequest;
import me.alextur.matlab.rest.endpoints.group.model.ChangeGroupRequest;
import me.alextur.matlab.service.group.GroupService;
import me.alextur.matlab.service.user.UserListFilter;
import me.alextur.matlab.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.acl.Group;

@Component
@Produces(MediaType.APPLICATION_JSON)
@Path("groups")
public class GroupEndpoint extends BaseEndpoint {

    private UserService userService;
    private GroupService groupService;
    private GroupRepository groupRepository;

    public GroupEndpoint(@Autowired UserService pUserService,
                         @Autowired GroupService pGroupService,
                         @Autowired GroupRepository pGroupRepository) {
        this.userService = pUserService;
        this.groupService = pGroupService;
        this.groupRepository = pGroupRepository;
    }

    @GET
    @Path("list")
    @PermitAll
    @Transactional
    public Object list(){
        return groupRepository.findAll();
    }

    @PUT
    @Path("group")
    @PermitRoles(roles = {"Admin", "Teacher"})
    @Transactional
    public Response createGroup(@Valid ChangeGroupRequest pRequest) {
        StudentGroup result = new StudentGroup();

        if(pRequest.getName() != null){
            result.setName(pRequest.getName());
        }

        result = groupService.createGroup(result);

        return Response.ok(result).build();
    }

    @POST
    @Path("{groupId}/update")
    @PermitRoles(roles = {"Admin", "Teacher"})
    @Transactional
    public Response edit(@PathParam("groupId") Long id, ChangeGroupRequest groupRequest) {
        StudentGroup result = groupRepository.findOne(id);
        if(result == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if(groupRequest.getName() != null){
            result.setName(groupRequest.getName());
        }

        result = groupRepository.saveAndFlush(result);

        return Response.ok(result).build();
    }

    @GET
    @Path("{groupId}/details")
    @PermitRoles(roles = {"Admin", "Teacher"})
    @Transactional
    public Object details(@PathParam("groupId") long groupId){
        StudentGroup group = groupRepository.findById(groupId);
        if(group == null){
            return badRequestFail("Group not found!");
        }

        return group;
    }


}
