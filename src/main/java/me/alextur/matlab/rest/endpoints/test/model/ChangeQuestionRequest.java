package me.alextur.matlab.rest.endpoints.test.model;

import me.alextur.matlab.model.test.TestVariant;

import java.util.List;
import java.util.Set;

/**
 * @author Alex Turchynovich
 */
public class ChangeQuestionRequest {
    private Long id;
    private List<String> variantsIds;
    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long pId) {
        id = pId;
    }

    public List<String> getVariantsIds() {
        return variantsIds;
    }

    public void setVariantsIds(List<String> pVariantsIds) {
        variantsIds = pVariantsIds;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String pContent) {
        content = pContent;
    }
}
