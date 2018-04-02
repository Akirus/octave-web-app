package me.alextur.matlab.rest.endpoints.documents.model;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author Alex Turchynovich
 */
public class ChangeDocumentRequest extends ChangeTreeEntityRequest{
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String pContent) {
        content = pContent;
    }

}
