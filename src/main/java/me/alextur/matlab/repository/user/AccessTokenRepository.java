package me.alextur.matlab.repository.user;

import me.alextur.matlab.model.user.AccessToken;
import me.alextur.matlab.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Alex Turchynovich
 */
public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {

    AccessToken findFirstByToken(String token);

}
