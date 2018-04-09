package me.alextur.matlab.repository.test;

import me.alextur.matlab.model.test.TestQuestion;
import me.alextur.matlab.model.test.TestVariant;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Alex Turchynovich
 */
public interface TestVariantRepository extends JpaRepository<TestVariant, Long> {
    
}
