package me.alextur.matlab.rest.endpoints.user.model;

/**
 * @author Alex Turchynovich
 */
public class LoginRequest {

    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String pUsername) {
        username = pUsername;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String pPassword) {
        password = pPassword;
    }
}
