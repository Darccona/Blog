package org.darccona.database.repository;

import org.darccona.database.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * @see org.darccona.database.entity.UserEntity
 */
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    UserEntity findByName(String name);
}