package org.darccona.database.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class NoticeEntity {

    @Id

    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @Column(name = "DATE")
    private Date date;

    @Column(name = "AUTHOR")
    private String author;

    @Column(name = "RECORD")
    private long record;

    @Column(name = "COMM")
    private String comm;

    public NoticeEntity() { }
    public NoticeEntity(String author, long record, String comm) {
        this.date = new Date();
        this.author = author;
        this.record = record;
        this.comm = comm;
    }

    public long getId() {
        return id;
    }

    public int getType() {
        if ((comm == null) && (record == -1)) {
            return 2;
        } else if (comm == null) {
            return 1;
        } else {
            return 3;
        }
    }
    public String getText() {
        if ((comm == null) && (record == -1)) {
            return "Пользователь " + author + " подписался на Вас.";
        } else if (comm == null) {
            return "Пользователю " + author + " понравилась Ваша запись:";
        } else {
            return "Пользователь " + author + " оставил комментарий под Вашей записью:";
        }
    }
    public String getAuthor() {
        return author;
    }
    public Date getDate() {
        return date;
    }
    public long getRecord() {
        return record;
    }
    public String getComm() {
        return comm;
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
