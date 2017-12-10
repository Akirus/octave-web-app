package me.alextur.matlab.service.document;

import me.alextur.matlab.model.Document;
import me.alextur.matlab.repository.document.DocumentRepository;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;


/**
 * @author Alex Turchynovich
 */
@Component
public class DocumentsService {

    private final DocumentRepository documentRepository;

    public DocumentsService(DocumentRepository pDocumentRepository) {
        documentRepository = pDocumentRepository;
    }

    public Document updateDocument(Document pDocument){
        return documentRepository.saveAndFlush(pDocument);
    }

    public Document createDocument(Document pResult) {
        pResult.setCreationDate(DateTime.now());

        return documentRepository.saveAndFlush(pResult);
    }

    public void deleteDocument(Document pResult) {
        documentRepository.delete(pResult);
    }
}
