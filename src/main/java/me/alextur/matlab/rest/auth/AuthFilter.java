package me.alextur.matlab.rest.auth;

import me.alextur.matlab.service.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Alex Turchynovich
 */
@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        Method method = resourceInfo.getResourceMethod();

        boolean isPermitAll = method.isAnnotationPresent(PermitAll.class);
        if(!isPermitAll && !requestContext.getMethod().equals("OPTIONS")) {
            PermitRoles permitRoles = method.getAnnotation(PermitRoles.class);

            if(permitRoles != null) {
                if (!UserService.isUserInRole(permitRoles.roles())) {
                    requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
                }
            }
            else {
                if (!isUserLogined()) {
                    requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
                }
            }
        }
    }

    private boolean isUserLogined() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean anon = false;
        if(authentication != null){
            for(GrantedAuthority authority : authentication.getAuthorities()){
                if(authority.getAuthority().equals("ROLE_ANONYMOUS")) {
                    anon = true;
                    break;
                }
            }
        }

        return authentication != null && authentication.isAuthenticated() && !anon;
    }
}
