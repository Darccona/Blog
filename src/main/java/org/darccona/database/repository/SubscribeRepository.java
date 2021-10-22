package org.darccona.database.repository;

import org.darccona.database.entity.SubscribeEntity;
import org.darccona.database.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SubscribeRepository extends CrudRepository<SubscribeEntity, Long> {

    List<SubscribeEntity> findByUser(UserEntity user);
    SubscribeEntity findByUserAndName(UserEntity user, String name);

}
