package me.alextur.matlab.repository.document;


import me.alextur.matlab.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Alex Turchynovich
 */
@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    boolean existsByFileName(String pFileName);

}
