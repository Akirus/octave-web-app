package me.alextur.matlab.rest.endpoints.user;

import me.alextur.matlab.model.media.Media;
import me.alextur.matlab.model.user.AccessToken;
import me.alextur.matlab.model.user.DefinedRole;
import me.alextur.matlab.model.user.StudentGroup;
import me.alextur.matlab.model.user.User;
import me.alextur.matlab.repository.user.GroupRepository;
import me.alextur.matlab.repository.user.UserRepository;
import me.alextur.matlab.rest.BaseEndpoint;
import me.alextur.matlab.rest.auth.PermitAll;
import me.alextur.matlab.rest.auth.PermitRoles;
import me.alextur.matlab.service.media.CreateMediaRequest;
import me.alextur.matlab.rest.endpoints.user.model.LoginRequest;
import me.alextur.matlab.rest.endpoints.user.model.RegisterRequest;
import me.alextur.matlab.rest.endpoints.user.model.UpdateRequest;
import me.alextur.matlab.service.media.MediaService;
import me.alextur.matlab.service.user.UserListFilter;
import me.alextur.matlab.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alex Turchynovich
 */
@Component
@Produces(MediaType.APPLICATION_JSON)
@Path("user")
public class UserEndpoint extends BaseEndpoint {

    private UserService userService;
    private AuthenticationManager authManager;
    private Validator validator;
    private UserRepository userRepository;
    private GroupRepository groupRepository;
    private MediaService mediaService;

    public UserEndpoint(@Autowired UserService pUserService,
                        @Autowired AuthenticationManager pAuthManager,
                        @Autowired Validator pValidator,
                        @Autowired UserRepository pUserRepository,
                        @Autowired GroupRepository groupRepository,
                        @Autowired MediaService pMediaService) {
        this.userService = pUserService;
        this.authManager = pAuthManager;
        this.validator = pValidator;
        this.userRepository = pUserRepository;
        this.groupRepository = groupRepository;
        this.mediaService = pMediaService;
    }

    @POST
    @PermitAll
    @Path("login")
    @Transactional
    public Object login(LoginRequest pLoginRequest){

        String username = pLoginRequest.getUsername().toLowerCase();

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, pLoginRequest.getPassword());
        Authentication authentication = this.authManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof User)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        AccessToken accessToken = userService.createAccessToken((User) principal);

        if(accessToken == null){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        Map<String, Object> result = new HashMap<>();
        result.put("token", accessToken.getToken());
        result.put("expires", accessToken.getExpiry());

        return result;
    }

    @POST
    @PermitAll
    @Path("register")
    @Transactional
    public Object register(@Valid RegisterRequest pRegisterRequest){

        User user = new User();

        String username = pRegisterRequest.getUsername().toLowerCase();

        user.setUsername(username);
        user.setEmail(pRegisterRequest.getEmail());
        user.setFirstName(pRegisterRequest.getFirstName());
        user.setLastName(pRegisterRequest.getLastName());

        if(pRegisterRequest.getRole().equals(DefinedRole.User)){
            user.setStudentGroup(groupRepository.findById(pRegisterRequest.getGroupId()));
        }

        boolean registered = userService.register(user, pRegisterRequest.getPassword(), pRegisterRequest.getRole());

        if(registered){
            return Response.ok().build();
        }
        else{
            return genericFail();
        }
    }

    @GET
    @Path("details")
    @Transactional
    public Object details(){
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof User)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        return (User) authentication.getPrincipal();
    }

    @GET
    @Path("details/{userId}")
    @Transactional
    public Object details(@PathParam("userId") long userId){
        if(!UserService.userHasAccessToAnotherUser(userId)){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        User user = userRepository.findById(userId);
        if(user == null){
            return badRequestFail("User not found!");
        }

        return user;
    }

    @POST
    @Path("update/{userId}")
    public Object update(@PathParam("userId") long userId, @Valid UpdateRequest pUpdateRequest){
        if(!UserService.userHasAccessToAnotherUser(userId)){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        User user = userRepository.findById(userId);

        if(pUpdateRequest != null) {
            if (pUpdateRequest.getFirstName() != null) {
                user.setFirstName(pUpdateRequest.getFirstName());
            }
            if (pUpdateRequest.getLastName() != null) {
                user.setLastName(pUpdateRequest.getLastName());
            }
            if (pUpdateRequest.getEmail() != null){
                user.setEmail(pUpdateRequest.getEmail());
            }

            if(pUpdateRequest.getGroupId() != null){
                StudentGroup group = groupRepository.findById(pUpdateRequest.getGroupId());
                if(group != null){
                    user.setStudentGroup(group);
                }
            }

            user = this.userService.updateUser(user);

            if (pUpdateRequest.getPassword() != null) {
                user = this.userService.updatePassword(user, pUpdateRequest.getPassword());
            }
        }

        return user;
    }

    @POST
    @Path("update/avatar/{userId}")
    @Transactional
    public Object setAvatar(@PathParam("userId") long userId, CreateMediaRequest pCreateMediaRequest){
        if(!UserService.userHasAccessToAnotherUser(userId)){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        User user = userRepository.findById(userId);

        if(pCreateMediaRequest != null) {
            Media avatar = mediaService.createMedia(pCreateMediaRequest);
            if (avatar != null){
                user.setAvatar(avatar);
            }

            userRepository.saveAndFlush(user);
        }

        return user;
    }


    @POST
    @Path("update")
    public Object update(@Valid UpdateRequest pUpdateRequest){
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof User)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        User user = (User) authentication.getPrincipal();

        return update(user.getId(), pUpdateRequest);
    }

    @GET
    @Path("list/{filter}")
    @Transactional
    public Object list(@PathParam("filter") UserListFilter filter){
        return userService.getList(filter);
    }

    @POST
    @Path("approve/{userId}")
    @PermitRoles(roles = {"Admin", "Teacher"})
    public Object approve(@PathParam("userId") long userId){
        boolean success = userService.approveUser(userId);

        return Response.ok().build();
    }

    @POST
    @Path("reject/{userId}")
    @PermitRoles(roles = {"Admin", "Teacher"})
    public Object reject(@PathParam("userId") long userId){
        boolean success = userService.rejectUser(userId);

        return Response.ok().build();
    }

}
