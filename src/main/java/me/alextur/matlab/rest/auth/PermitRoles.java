package me.alextur.matlab.rest.auth;

import javax.ws.rs.NameBinding;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Alex Turchynovich
 */
@NameBinding
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface  PermitRoles {

    public String[] roles();



}
