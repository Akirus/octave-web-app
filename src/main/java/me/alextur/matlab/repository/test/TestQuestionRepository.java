package me.alextur.matlab.repository.test;

import me.alextur.matlab.model.test.TestQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Alex Turchynovich
 */
public interface TestQuestionRepository extends JpaRepository<TestQuestion, Long> {

}
