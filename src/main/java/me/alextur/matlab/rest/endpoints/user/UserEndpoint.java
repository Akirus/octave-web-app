package me.alextur.matlab.rest.endpoints.user;

import me.alextur.matlab.model.user.AccessToken;
import me.alextur.matlab.model.user.User;
import me.alextur.matlab.rest.BaseEndpoint;
import me.alextur.matlab.rest.auth.PermitAll;
import me.alextur.matlab.rest.auth.PermitRoles;
import me.alextur.matlab.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Alex Turchynovich
 */
@Component
@Produces(MediaType.APPLICATION_JSON)
@Path("user")
public class UserEndpoint extends BaseEndpoint {

    private UserService userService;
    private AuthenticationManager authManager;

    public UserEndpoint(@Autowired UserService pUserService,
                        @Autowired AuthenticationManager pAuthManager) {
        userService = pUserService;
        authManager = pAuthManager;
    }

    @POST
    @PermitAll
    @Path("login")
    @Transactional
    public Object login(LoginRequest pLoginRequest){
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(pLoginRequest.getUsername(), pLoginRequest.getPassword());
        Authentication authentication = this.authManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof User)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        AccessToken accessToken = this.userService.createAccessToken((User) principal);

        Map<String, Object> result = new HashMap<>();
        result.put("token", accessToken.getToken());
        result.put("expires", accessToken.getExpiry());

        return result;
    }

    @GET
    @Path("details")
    @Transactional
    public Object details(){
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof User)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        User user = (User) authentication.getPrincipal();

        return user;
    }

    @POST
    @Path("update")
    public Object update(UpdateRequest pUpdateRequest){
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof User)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        User user = (User) authentication.getPrincipal();

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

            user = this.userService.updateUser(user);

            if (pUpdateRequest.getPassword() != null) {
                user = this.userService.updatePassword(user, pUpdateRequest.getPassword());
            }
        }

        return user;
    }


}
