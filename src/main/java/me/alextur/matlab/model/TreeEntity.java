package me.alextur.matlab.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import me.alextur.matlab.model.user.Role;
import me.alextur.matlab.model.user.StudentGroup;
import me.alextur.matlab.model.user.User;


import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class TreeEntity {

    private Long id;

    private String name;

    private Set<Role> allowedRoles;
    private Set<StudentGroup> allowedGroups;

    private TreeEntity parent;

    private List<TreeEntity> children;

    private Map<String, Object> additionalData = new HashMap<>();

    @Transient
    public Map<String, Object> getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(Map<String, Object> pAdditionalData) {
        additionalData = pAdditionalData;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    @OneToOne
    @JsonIgnore
    public TreeEntity getParent() {
        return parent;
    }

    public void setParent(TreeEntity parent) {
        this.parent = parent;
    }

    @Transient
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tree_role",  joinColumns = @JoinColumn(name = "tree_entity_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonIgnore
    public Set<Role> getAllowedRoles() {
        return allowedRoles;
    }

    public void setAllowedRoles(Set<Role> allowedRoles) {
        this.allowedRoles = allowedRoles;
    }

    @Transient
    public String[] getAllowedRolesNames(){
        Set<Role> roles = getAllowedRoles();
        if(roles == null || roles.isEmpty()){
            return new String[0];
        }
        return roles.stream()
                .map(Role::getName)
                .toArray(String[]::new);
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tree_group",
            joinColumns = @JoinColumn(name = "tree_entity_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id"))
    @JsonIgnore
    public Set<StudentGroup> getAllowedGroups() {
        return allowedGroups;
    }

    public void setAllowedGroups(Set<StudentGroup> allowedGroups) {
        this.allowedGroups = allowedGroups;
    }
}
