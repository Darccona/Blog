package org.darccona.database.entity;

import javax.persistence.*;

/**
 * Класс объекта подписки пользователя
 */
@Entity
public class SubscribeEntity {

    /**
     * имя пользователя, на которого подписались
     */
    @Id
    @Column(name = "USERNAME")
    private String name;

    public SubscribeEntity() { }
    public SubscribeEntity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
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
