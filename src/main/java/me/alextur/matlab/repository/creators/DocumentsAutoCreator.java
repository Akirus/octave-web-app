package me.alextur.matlab.repository.creators;

import me.alextur.matlab.model.Document;
import me.alextur.matlab.repository.document.DocumentRepository;
import me.alextur.matlab.repository.DocumentStorageConfig;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex Turchynovich
 */
@Component
public class DocumentsAutoCreator {

    private DocumentRepository documentRepository;
    private DocumentStorageConfig documentStorageConfig;

    private Logger logger = Logger.getLogger(DocumentsAutoCreator.class);

    public DocumentsAutoCreator(@Autowired DocumentRepository pDocumentRepository,
                                @Autowired DocumentStorageConfig pDocumentStorageConfig) {
        this.documentRepository = pDocumentRepository;
        this.documentStorageConfig = pDocumentStorageConfig;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        try {
            createDocuments();
        } catch (IOException pE) {
            logger.error("Unable to auto-create documents", pE);
        }
    }

    @Transactional()
    protected void createDocuments() throws IOException {
        String directory = documentStorageConfig.getDocumentsDirectory();

        List<Document> toCreate = new ArrayList<>();
        Files.list(Paths.get(directory)).forEach(pPath -> {
            String fileName = pPath.getFileName().toString();
            String name = fileName.substring(0, fileName.lastIndexOf('.'));

            if(!documentRepository.existsByFileName(name)){
                Document document = new Document();
                document.setName(name);
                document.setFileName(name);
                document.setCreationDate(DateTime.now());

                try {
                    document.setContent(new String(Files.readAllBytes(pPath)));
                } catch (IOException pE) {
                    logger.error("unable to read file", pE);
                }

                toCreate.add(document);
            }
        });

        if(toCreate.size() > 0) {
            documentRepository.save(toCreate);
            documentRepository.flush();
        }
    }

}
