package me.alextur.matlab.rest;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;

import javax.annotation.Nullable;
import javax.ws.rs.core.Response;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alex Turchynovich
 */
public class BaseEndpoint {

    protected Response genericFail(){
        return Response.serverError().build();
    }

    protected Response badRequestFail(@Nullable String message){
        if(message == null){
            message = "Bad Request.";
        }

        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("errorMessage", message);

        return Response.status(Response.Status.BAD_REQUEST).entity(responseMap).build();
    }

    protected Response badRequestFail(){
        return badRequestFail(null);
    }

    private Logger logger;
    protected Logger getLogger(){
        if (logger == null){
            logger = Logger.getLogger(getClass());
        }

        return logger;
    }



}
