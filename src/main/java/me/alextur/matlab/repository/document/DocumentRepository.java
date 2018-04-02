package me.alextur.matlab.repository.document;


import me.alextur.matlab.model.Document;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Alex Turchynovich
 */
@Repository
@Transactional
public interface DocumentRepository extends TreeEntityRepository<Document> {

    boolean existsByFileName(String pFileName);

}
