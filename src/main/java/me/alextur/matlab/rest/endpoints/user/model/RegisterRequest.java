package me.alextur.matlab.rest.endpoints.user.model;

import me.alextur.matlab.model.user.DefinedRole;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;

/**
 * @author Alex Turchynovich
 */
public class RegisterRequest {

    @NotBlank
    @Length(max = 255)
    private String username;

    @Email
    @Length(max = 255)
    private String email;

    @Length(max = 255)
    @NotBlank
    private String password;

    @Length(max = 255)
    @NotBlank
    private String firstName;

    @NotBlank
    @Length(max = 255)
    private String lastName;

    private DefinedRole role = DefinedRole.User;


    public String getUsername() {
        return username;
    }

    public void setUsername(String pUsername) {
        username = pUsername;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String pEmail) {
        email = pEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String pPassword) {
        password = pPassword;
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

    public DefinedRole getRole() {
        return role;
    }

    public void setRole(DefinedRole pRole) {
        role = pRole;
    }
}
