package me.alextur.matlab.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import me.alextur.matlab.model.media.Media;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

/**
 * @author Alex Turchynovich
 */
@Entity
@Table(name = "OctaveUser")
public class User implements UserDetails{

    private Long id;
    private String username;
    private String password;
    private Set<Role> roles;
    private String firstName;
    private String lastName;
    private String email;

    private boolean isEnabled;

    private StudentGroup studentGroup;

    private Media avatar;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",  joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    @Transient
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    @Transient
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @Transient
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @Transient
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @Column(columnDefinition = "boolean default false")
    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean pEnabled) {
        isEnabled = pEnabled;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String pFirstName) {
        firstName = pFirstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String pLastName) {
        lastName = pLastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String pEmail) {
        email = pEmail;
    }

    @ManyToOne
    @JoinColumn(name = "group_id")
    @JsonIgnore
    public StudentGroup getStudentGroup() {
        return studentGroup;
    }

    public void setStudentGroup(StudentGroup studentGroup) {
        this.studentGroup = studentGroup;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avatar_id")
    @JsonIgnore
    public Media getAvatar() {
        return avatar;
    }

    public void setAvatar(Media pAvatar) {
        avatar = pAvatar;
    }

    @Transient
    public Long getAvatarId(){
        Media avatar = getAvatar();
        if (avatar != null) {
            return getAvatar().getId();
        }
        return null;
    }

    @Transient
    public String getStudentGroupName(){
        StudentGroup group = getStudentGroup();
        if(group != null){
            return group.getName();
        }

        return "";
    }

    @Transient
    public Long getStudentGroupId(){
        StudentGroup group = getStudentGroup();
        if(group != null){
            return group.getId();
        }

        return 0L;
    }


}
