package me.alextur.matlab.model;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alex Turchynovich
 */
@Entity(name = "Document")
public class Document extends TreeEntity{


    private String content;

    private DateTime creationDate;

    private String fileName;

    @Lob
    public String getContent() {
        return content;
    }

    public void setContent(String pContent) {
        content = pContent;
    }

    public DateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(DateTime pCreationDate) {
        creationDate = pCreationDate;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String pFileName) {
        fileName = pFileName;
    }
}
