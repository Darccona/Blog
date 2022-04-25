package org.darccona.database.repository;

import org.darccona.database.entity.RecordEntity;
import org.darccona.database.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @see org.darccona.database.entity.RecordEntity
 */
public interface RecordRepository extends CrudRepository<RecordEntity, Long> {

    RecordEntity findById(long id);
    List<RecordEntity> findByUser(UserEntity user);
}