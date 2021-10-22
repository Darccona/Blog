package org.darccona.database.entity;

import javax.persistence.*;

@Entity
public class LikeEntity {
    @Id

    @Column(name = "RECORD")
    private long record;

    public LikeEntity() { }
    public LikeEntity(long id) {
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
