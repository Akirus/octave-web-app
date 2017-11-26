package me.alextur.matlab.service;

import me.alextur.matlab.model.user.AccessToken;
import me.alextur.matlab.model.user.User;
import me.alextur.matlab.repository.user.AccessTokenRepository;
import me.alextur.matlab.repository.user.UserRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

/**
 * @author Alex Turchynovich
 */
@Component
public class UserService implements UserDetailsService {

    private final AccessTokenRepository accessTokenRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(@Autowired AccessTokenRepository pAccessTokenRepository,
                       @Autowired UserRepository pUserRepository,
                       @Autowired BCryptPasswordEncoder pPasswordEncoder) {
        accessTokenRepository = pAccessTokenRepository;
        userRepository = pUserRepository;
        passwordEncoder = pPasswordEncoder;
    }

    @Transactional
    public User findUserByAccessToken(String accessTokenString)
    {
        AccessToken accessToken = accessTokenRepository.findFirstByToken(accessTokenString);

        if (null == accessToken) {
            return null;
        }

        if (accessToken.isExpired()) {
            this.accessTokenRepository.delete(accessToken);
            return null;
        }

        return accessToken.getUser();
    }

    @Transactional
    public AccessToken createAccessToken(User user)
    {
        AccessToken accessToken = new AccessToken(user,
                UUID.randomUUID().toString(),
                new DateTime().plusDays(1).toDate());
        return accessTokenRepository.save(accessToken);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        return user;
    }

    public User updatePassword(User pUser, String pPassword) {
        pUser.setPassword(passwordEncoder.encode(pPassword));
        return userRepository.saveAndFlush(pUser);
    }

    public User updateUser(User pUser) {
        return userRepository.saveAndFlush(pUser);
    }
}
