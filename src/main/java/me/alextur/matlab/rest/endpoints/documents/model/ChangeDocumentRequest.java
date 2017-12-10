package me.alextur.matlab.rest.endpoints.documents.model;

/**
 * @author Alex Turchynovich
 */
public class ChangeDocumentRequest {

    private String content;
    private Integer order;
    private String name;

    public String getContent() {
        return content;
    }

    public void setContent(String pContent) {
        content = pContent;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer pOrder) {
        order = pOrder;
    }

    public String getName() {
        return name;
    }

    public void setName(String pName) {
        name = pName;
    }
}
