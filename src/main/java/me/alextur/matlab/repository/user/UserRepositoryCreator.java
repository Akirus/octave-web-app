package me.alextur.matlab.repository.user;

import me.alextur.matlab.model.user.DefinedRole;
import me.alextur.matlab.model.user.Role;
import me.alextur.matlab.model.user.User;
import org.hibernate.mapping.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Alex Turchynovich
 */
@Component
public class UserRepositoryCreator {

    private RoleRepository roleRepository;
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder;

    public UserRepositoryCreator(@Autowired RoleRepository pRoleRepository,
                                 @Autowired UserRepository pUserRepository,
                                 @Autowired BCryptPasswordEncoder pPasswordEncoder) {
        roleRepository = pRoleRepository;
        userRepository = pUserRepository;
        this.passwordEncoder = pPasswordEncoder;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        Set<Role> roles = createDefinedRoles();
        createAdmin(roles);
    }

    @Transactional()
    protected Set<Role> createDefinedRoles(){
        Set<Role> roles = new HashSet<>();

        for(DefinedRole definedRole : DefinedRole.values()){
            Role role = roleRepository.findOne(definedRole.getRoleName());

            if(role == null){
                role = new Role(definedRole.getRoleName());
                roleRepository.save(role);
            }
            roles.add(role);
        }

        roleRepository.flush();

        return roles;
    }

    @Transactional()
    protected void createAdmin(Set<Role> pRoles){
        User admin = userRepository.findByUsername("Admin");
        if(admin == null){
            admin = new User();
            admin.setUsername("Admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRoles(pRoles);
            admin.setEnabled(true);

            userRepository.saveAndFlush(admin);
        }

    }



}
