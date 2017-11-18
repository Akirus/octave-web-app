package me.alextur.matlab.rest.config;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import me.alextur.matlab.rest.documents.DocumentsEndpoint;
import me.alextur.matlab.rest.octave.OctaveEndpoint;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

/**
 * @author Alex Turchynovich
 */
@Component
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        registerEndpoints();
    }

    private void registerEndpoints(){
        register(DocumentsEndpoint.class);
        register(OctaveEndpoint.class);
        configureSwagger();
    }

    private void configureSwagger() {
        register(ApiListingResource.class);
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.0");
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setHost("localhost:8080");
        beanConfig.setBasePath("/");
        beanConfig.setResourcePackage("me.alextur.matlab.rest.config,me.alextur.matlab.rest.documents,me.alextur.matlab.rest.octave");
        beanConfig.setPrettyPrint(true);
        beanConfig.setScan(true);
    }

}
