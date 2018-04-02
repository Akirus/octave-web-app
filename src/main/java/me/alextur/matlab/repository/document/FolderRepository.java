package me.alextur.matlab.repository.document;

import me.alextur.matlab.model.Folder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface FolderRepository extends TreeEntityRepository<Folder> {

}
