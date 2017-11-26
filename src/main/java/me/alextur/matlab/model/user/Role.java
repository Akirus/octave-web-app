package me.alextur.matlab.model.user;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "role")
public class Role implements GrantedAuthority {
    private String name;
    private Set<User> users;

    @Id
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToMany(mappedBy = "roles")
    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Role(String pName) {
        name = pName;
    }

    public Role() {
    }

    @Override
    @Transient
    public String getAuthority() {
        return name;
    }
}
