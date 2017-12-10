package me.alextur.matlab.rest.endpoints.user.model;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author Alex Turchynovich
 */
public class UpdateRequest {

    @Email
    private String email;

    @Length(max = 255)
    private String password;

    @Length(max = 255)
    private String firstName;

    @Length(max = 255)
    private String lastName;

    public String getEmail() {
        return email;
    }

    public void setEmail(String pEmail) {
        email = pEmail;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String pPassword) {
        password = pPassword;
    }
}
