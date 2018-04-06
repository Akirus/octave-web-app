package me.alextur.matlab.rest.endpoints.documents.model;

import java.util.HashSet;
import java.util.Set;

public class ChangeTreeEntityRequest {

    private String name;

    private Long parentId;

    private Set<Long> allowedGroupIds;

    private Set<String> allowedRoles;

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

    public Set<Long> getAllowedGroupIds() {
        return allowedGroupIds;
    }

    public void setAllowedGroupIds(Set<Long> allowedGroupIds) {
        this.allowedGroupIds = allowedGroupIds;
    }

    public Set<String> getAllowedRoles() {
        return allowedRoles;
    }

    public void setAllowedRoles(Set<String> allowedRoles) {
        this.allowedRoles = allowedRoles;
    }
}
