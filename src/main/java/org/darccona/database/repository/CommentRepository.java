package org.darccona.database.repository;

import org.darccona.database.entity.CommentEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * @see org.darccona.database.entity.CommentEntity
 */
public interface CommentRepository extends CrudRepository<CommentEntity, Long> {
    CommentEntity findById(long id);
}
