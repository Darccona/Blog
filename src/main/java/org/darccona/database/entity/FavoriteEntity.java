package org.darccona.database.entity;

import javax.persistence.*;

/**
 * Класс объекта избранные записи пользователя
 */
@Entity
public class FavoriteEntity {

    /**
     * номер избранной записи
     */
    @Id
    @Column(name = "RECORD")
    private long record;

    public FavoriteEntity() { }
    public FavoriteEntity(long id) {
        record = id;
    }

    public long getRecord() {
        return record;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user", nullable = false)
    private UserEntity user;

    public void setUser(UserEntity user) {
        this.user = user;
    }
    public UserEntity getUser() {
        return user;
    }
}
