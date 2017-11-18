package me.alextur.octave.rest.config;

import me.alextur.octave.rest.DocumentsEndpoint;
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
    }

}
