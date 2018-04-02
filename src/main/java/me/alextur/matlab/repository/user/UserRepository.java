package me.alextur.matlab.repository.user;

import me.alextur.matlab.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findById(Long id);

    List<User> findAllByEnabled(boolean pEnabled);

    boolean existsByUsername(String username);
}
