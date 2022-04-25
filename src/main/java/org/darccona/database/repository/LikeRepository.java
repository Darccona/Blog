package org.darccona.database.repository;

import org.darccona.database.entity.LikeEntity;
import org.darccona.database.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @see org.darccona.database.entity.LikeEntity
 */
public interface LikeRepository extends CrudRepository<LikeEntity, Long> {

    List<LikeEntity> findByUser(UserEntity user);
    List<LikeEntity> findByRecord(long record);
    LikeEntity findByUserAndRecord(UserEntity user, long record);

}
