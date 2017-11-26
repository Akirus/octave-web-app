package me.alextur.matlab.model.user;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Philip Washington Sorst <philip@sorst.net>
 */
@Entity
public class AccessToken
{
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String token;

    @ManyToOne
    private User user;

    @Column
    private Date expiry;

    protected AccessToken()
    {
        /* Reflection instantiation */
    }

    public AccessToken(User user, String token)
    {
        this.user = user;
        this.token = token;
    }

    public AccessToken(User user, String token, Date expiry)
    {
        this(user, token);
        this.expiry = expiry;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long pId) {
        id = pId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String pToken) {
        token = pToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User pUser) {
        user = pUser;
    }

    public Date getExpiry() {
        return expiry;
    }

    public void setExpiry(Date pExpiry) {
        expiry = pExpiry;
    }

    public boolean isExpired() {
        return null != this.expiry && (new Date()).getTime() > this.expiry.getTime();
    }

}
