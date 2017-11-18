package me.alextur.octave.rest;

import org.apache.log4j.Logger;

import javax.ws.rs.core.Response;

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
