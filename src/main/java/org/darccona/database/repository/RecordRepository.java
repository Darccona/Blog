package org.darccona.database.repository;

import org.darccona.database.entity.UserEntity;
import org.darccona.database.entity.RecordEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RecordRepository extends CrudRepository<RecordEntity, Long> {

    RecordEntity findById(long id);
    List<RecordEntity> findByUser(UserEntity user);

//    RecordEntity findByUserAndStanding(UserEntity user, boolean standing);
//    List<RecordEntity> findByUserAndNameContaining(UserEntity user, String title);
}