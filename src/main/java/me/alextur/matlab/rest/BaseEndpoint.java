package me.alextur.matlab.rest;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;

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

    private Logger logger;
    protected Logger getLogger(){
        if (logger == null){
            logger = Logger.getLogger(getClass());
        }

        return logger;
    }



}
