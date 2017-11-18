package me.alextur.octave.model;

import me.alextur.octave.model.beans.Document;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Alex Turchynovich
 */
@Component
public class DocumentFileConverter {

    public String idFromFileName(String fileName){
        if(fileName.endsWith(".md")){
            fileName = fileName.substring(0, fileName.lastIndexOf(".md"));
        }

        int dashPos = fileName.indexOf('-');

        if(dashPos > -1) {
            return fileName.substring(0, dashPos);
        }
        else{
            return fileName;
        }
    }

    public String documentNameFromFileName(String fileName){
        if(fileName.endsWith(".md")){
            fileName = fileName.substring(0, fileName.lastIndexOf(".md"));
        }

        int dashPos = fileName.indexOf('-');

        if(dashPos > -1) {
            return fileName.substring(dashPos + 1);
        }
        else{
            return fileName;
        }
    }

    public String documentToFileName(Document doc){
        return String.format("%s-%s.md", doc.getId(), doc.getName());
    }

    public Document convert(Path pPath, boolean withContent) throws IOException {
        Document result = new Document();

        String fileName = pPath.getFileName().toString();

        String id = idFromFileName(fileName);
        String name = documentNameFromFileName(fileName);

        result.setId(id);
        result.setName(name);

        if(withContent) {
            List<String> lines = Files.readAllLines(pPath);
            String content = String.join(System.lineSeparator(), lines);
            result.setContent(content);
        }

        return result;
    }

}
