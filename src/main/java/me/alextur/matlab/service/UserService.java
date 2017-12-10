package me.alextur.matlab.service;

import me.alextur.matlab.model.user.AccessToken;
import me.alextur.matlab.model.user.DefinedRole;
import me.alextur.matlab.model.user.Role;
import me.alextur.matlab.model.user.User;
import me.alextur.matlab.repository.user.AccessTokenRepository;
import me.alextur.matlab.repository.user.RoleRepository;
import me.alextur.matlab.repository.user.UserRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.core.Response;
import java.util.*;

/**
 * @author Alex Turchynovich
 */
@Component
public class UserService implements UserDetailsService {

    private final AccessTokenRepository accessTokenRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserService(@Autowired AccessTokenRepository pAccessTokenRepository,
                       @Autowired UserRepository pUserRepository,
                       @Autowired BCryptPasswordEncoder pPasswordEncoder,
                       @Autowired RoleRepository pRoleRepository) {
        accessTokenRepository = pAccessTokenRepository;
        userRepository = pUserRepository;
        passwordEncoder = pPasswordEncoder;
        roleRepository = pRoleRepository;
    }

    public Role findRole(DefinedRole pDefinedRole){
        return roleRepository.findOne(pDefinedRole.getRoleName());
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


    @Transactional
    public boolean register(User pUser, String password, DefinedRole pDefinedRole){

        if(userRepository.existsByUsername(pUser.getUsername())) {
            return false;
        }

        pUser.setEnabled(false);
        pUser.setPassword(passwordEncoder.encode(password));

        Role role = findRole(pDefinedRole);

        pUser.setRoles(new HashSet<>(Collections.singletonList(role)));

        userRepository.saveAndFlush(pUser);

        return true;
    }

    public User updatePassword(User pUser, String pPassword) {
        pUser.setPassword(passwordEncoder.encode(pPassword));
        return userRepository.saveAndFlush(pUser);
    }

    public User updateUser(User pUser) {
        return userRepository.saveAndFlush(pUser);
    }
}
