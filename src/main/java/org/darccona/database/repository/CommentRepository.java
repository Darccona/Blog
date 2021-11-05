package org.darccona.database.repository;

import org.darccona.database.entity.CommentEntity;
import org.darccona.database.entity.RecordEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<CommentEntity, Long> {
    CommentEntity findById(long id);
}
