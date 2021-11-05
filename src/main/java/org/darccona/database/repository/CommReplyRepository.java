package org.darccona.database.repository;

import org.darccona.database.entity.CommReplyEntity;
import org.springframework.data.repository.CrudRepository;

public interface CommReplyRepository extends CrudRepository<CommReplyEntity, Long> {
    CommReplyEntity findById(long id);
}
