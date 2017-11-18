package me.alextur.octave.model;

import me.alextur.octave.model.beans.Document;
import me.alextur.octave.model.config.DocumentStorageConfig;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Alex Turchynovich
 */
@Component
public class DocumentRepository {

    private Logger logger = Logger.getLogger(DocumentRepository.class);

    private DocumentFileConverter documentFileConverter;
    private DocumentStorageConfig config;

    public DocumentRepository(@Autowired DocumentFileConverter pDocumentFileConverter,
                              @Autowired DocumentStorageConfig pConfig) {
        this.documentFileConverter = pDocumentFileConverter;
        this.config = pConfig;
    }

    private Path getDirectory() throws IOException {
        Path result = Paths.get(config.getDocumentsDirectory());

        if(Files.notExists(result)){
            Files.createDirectories(result);
        }

        return result;
    }

    public List<Document> getAll(final boolean withContent) throws DataException{
        try {
            Path directory = getDirectory();

            return Files.list(directory)
                    .parallel()
                    .map((path) -> {
                        try {
                            return this.documentFileConverter.convert(path, withContent);
                        } catch (IOException pE) {
                            throw new RuntimeException(pE);
                        }
                    })
                    .collect(Collectors.toList());
        } catch (IOException | RuntimeException pE) {
            throw new DataException(pE);
        }
    }

    public Document get(String id) throws DataException{
        try {
            Path directory = getDirectory();

            List<Document> files = Files.list(directory)
                    .filter(pPath -> pPath.getFileName().toString().startsWith(id + "-"))
                    .map((path) -> {
                        try {
                            return this.documentFileConverter.convert(path, true);
                        } catch (IOException pE) {
                            throw new RuntimeException(pE);
                        }
                    })
                    .collect(Collectors.toList());

            if(files.size() > 0)
                return files.get(0);

            return null;
        } catch (IOException | RuntimeException pE) {
            throw new DataException(pE);
        }
    }

}
