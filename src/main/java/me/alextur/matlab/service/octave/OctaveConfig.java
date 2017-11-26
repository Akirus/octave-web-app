package me.alextur.matlab.service.octave;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Alex Turchynovich
 */
@Configuration()
@PropertySource("classpath:octave.properties")
public class OctaveConfig {

    @Value("${timeout}")
    private long processTimeout;

    @Value("${command}")
    private String command;

    public long getProcessTimeout() {
        return processTimeout;
    }

    public String getCommand() {
        return command;
    }
}
