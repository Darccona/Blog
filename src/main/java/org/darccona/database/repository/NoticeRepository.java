package org.darccona.database.repository;

import org.darccona.database.entity.NoticeEntity;
import org.darccona.database.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NoticeRepository extends CrudRepository<NoticeEntity, Long> {

    NoticeEntity findById(long id);
    List<NoticeEntity> findByUser(UserEntity user);

}
