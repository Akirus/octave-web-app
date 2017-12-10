package me.alextur.matlab.model;

import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alex Turchynovich
 */
@Entity
public class Document {

    public Document() {
        this.additionalData = new HashMap<>();
    }

    private String name;

    @Lob
    private String content;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private DateTime creationDate;

    private String fileName;

    @Column(name = "sort_order",nullable = true)
    private Integer order;

    public Long getId() {
        return id;
    }

    @Transient
    private Map<String, Object> additionalData;

    @Transient
    public Map<String, Object> getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(Map<String, Object> pAdditionalData) {
        additionalData = pAdditionalData;
    }

    public void setId(Long pId) {
        id = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String pName) {
        name = pName;
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
