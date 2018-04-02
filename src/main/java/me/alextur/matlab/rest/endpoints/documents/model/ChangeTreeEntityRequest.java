package me.alextur.matlab.rest.endpoints.documents.model;

public class ChangeTreeEntityRequest {

    private String name;

    private Long parentId;

    public String getName() {
        return name;
    }

    public void setName(String pName) {
        name = pName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

}
