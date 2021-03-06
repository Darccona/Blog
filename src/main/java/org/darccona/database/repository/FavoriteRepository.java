package org.darccona.database.repository;

import org.darccona.database.entity.FavoriteEntity;
import org.darccona.database.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FavoriteRepository extends CrudRepository<FavoriteEntity, Long> {

    List<FavoriteEntity> findByUser(UserEntity user);
    FavoriteEntity findByUserAndRecord(UserEntity user, long record);

}
