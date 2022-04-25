package org.darccona.database.repository;

import org.darccona.database.entity.CommReplyEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * @see org.darccona.database.entity.CommReplyEntity
 */
public interface CommReplyRepository extends CrudRepository<CommReplyEntity, Long> {
    CommReplyEntity findById(long id);
}
