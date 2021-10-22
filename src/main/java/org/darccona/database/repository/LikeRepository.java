package org.darccona.database.repository;

import org.darccona.database.entity.LikeEntity;
import org.darccona.database.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LikeRepository extends CrudRepository<LikeEntity, Long> {

    List<LikeEntity> findByUser(UserEntity user);
    LikeEntity findByUserAndRecord(UserEntity user, long record);
//    LikeEntity findByRecord(long record);
}
