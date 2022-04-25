package org.darccona.database.repository;

import org.darccona.database.entity.LinkEmailEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * @see org.darccona.database.entity.LinkEmailEntity
 */
public interface LinkEmailRepository extends CrudRepository<LinkEmailEntity, Long> {
    LinkEmailEntity findByLink(String link);
    LinkEmailEntity findByName(String name);

}
