package me.alextur.matlab.repository.document;

import me.alextur.matlab.model.TreeEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TreeRepository extends TreeEntityRepository<TreeEntity> {

    List<TreeEntity> findByParentIsNullOrderByName();

    @Query("select t from TreeEntity t where t.parent.id = ?1 order by t.name")
    List<TreeEntity> getChildren(Long parentId);

}
