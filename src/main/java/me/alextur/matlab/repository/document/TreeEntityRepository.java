package me.alextur.matlab.repository.document;

import me.alextur.matlab.model.TreeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface TreeEntityRepository<T> extends JpaRepository<T, Long> {
    T findById(Long id);

}
