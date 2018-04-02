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

    public Document() {
        this.additionalData = new HashMap<>();
    }

    @Lob
//    @Type(type="org.hibernate.type.MaterializedClobType")
    private String content;

    private DateTime creationDate;

    private String fileName;

    @Column(name = "sort_order",nullable = true)
    private Integer order;


    @Transient
    private Map<String, Object> additionalData;

    @Transient
    public Map<String, Object> getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(Map<String, Object> pAdditionalData) {
        additionalData = pAdditionalData;
    }


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

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer pOrder) {
        order = pOrder;
    }
}
