package me.alextur.matlab.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Alex Turchynovich
 */
public class Document {

    public Document() {
        this.additionalData = new HashMap<>();
    }

    private String name;

    private String content;

    private String id;

    public String getId() {
        return id;
    }

    private Map<String, Object> additionalData;

    public Map<String, Object> getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(Map<String, Object> pAdditionalData) {
        additionalData = pAdditionalData;
    }

    public void setId(String pId) {
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
}
