package me.alextur.matlab.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import me.alextur.matlab.model.user.User;


import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class TreeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToOne
    @JsonIgnore
    private TreeEntity parent;

    @Transient
    private List<TreeEntity> children;

    public Long getId() {
        return id;
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

    public TreeEntity getParent() {
        return parent;
    }

    public void setParent(TreeEntity parent) {
        this.parent = parent;
    }

    public List<TreeEntity> getChildren() {
        return children;
    }

    public void setChildren(List<TreeEntity> children) {
        this.children = children;
    }

    @Transient
    public String getValue(){
        return name;
    }

    @Transient
    public String getType(){
        return this.getClass().getSimpleName();
    }
}
