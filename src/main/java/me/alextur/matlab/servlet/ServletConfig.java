package me.alextur.matlab.servlet;

import me.alextur.matlab.repository.media.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Alex Turchynovich
 */
@Configuration
public class ServletConfig {

    private MediaRepository mediaRepository;

    public ServletConfig(@Autowired MediaRepository pMediaRepository) {
        mediaRepository = pMediaRepository;
    }

    @Bean
    public ServletRegistrationBean adminServletRegistrationBean() {
        return new ServletRegistrationBean(new MediaServlet(mediaRepository), "/mediastream/*");
    }

}
