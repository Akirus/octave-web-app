package me.alextur.matlab.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Alex Turchynovich
 */
@Configuration
@PropertySource("classpath:fileDocuments.properties")
public class DocumentStorageConfig {

    @Value("${documentsDirectory}")
    private String documentsDirectory;

    public String getDocumentsDirectory() {
        return documentsDirectory;
    }
}
