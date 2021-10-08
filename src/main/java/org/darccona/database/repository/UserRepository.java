package org.darccona.database.repository;

import org.darccona.database.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

//    UserEntity findById(long id);
    UserEntity findByName(String name);
}