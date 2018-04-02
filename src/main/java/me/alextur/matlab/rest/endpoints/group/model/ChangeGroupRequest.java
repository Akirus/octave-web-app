package me.alextur.matlab.rest.endpoints.group.model;

import org.hibernate.validator.constraints.NotEmpty;

public class ChangeGroupRequest {
    private String name;

    @NotEmpty
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
