package org.darccona.database.repository;

import org.darccona.database.entity.ConfirmationUserEntity;
import org.springframework.data.repository.CrudRepository;

public interface ConfirmationUserRepository extends CrudRepository<ConfirmationUserEntity, Long> {
    ConfirmationUserEntity findByName(String name);
    ConfirmationUserEntity findByLink(String link);

}
