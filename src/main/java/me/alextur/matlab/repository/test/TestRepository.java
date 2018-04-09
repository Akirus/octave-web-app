package me.alextur.matlab.repository.test;

import me.alextur.matlab.model.test.Test;
import me.alextur.matlab.repository.document.TreeEntityRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Alex Turchynovich
 */
@Repository
@Transactional
public interface TestRepository extends TreeEntityRepository<Test> {

}
