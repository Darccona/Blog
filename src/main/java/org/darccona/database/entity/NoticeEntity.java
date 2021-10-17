package org.darccona.database.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class NoticeEntity {

    @Id

    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @Column(name = "TYPE")
    private int type;

    @Column(name = "DATE")
    private Date date;

    @Column(name = "AUTHOR")
    private String author;

    @Column(name = "RECORD")
    private String record;

    @Column(name = "COMM")
    private String comm;

    public NoticeEntity() { }
    public NoticeEntity(int type, String author, String record, String comm) {
        this.type = type;
        this.date = new Date();
        this.author = author;
        this.record = record;
        this.comm = comm;
    }

    public long getId() {
        return id;
    }

    public int getType() {
        return type;
    }
    public Date getDate() {
        return date;
    }
    public String getText() {
        if (type == 1) {
            return "Пользователю " + author + " понравилась Ваша запись:";
        } else if (type == 2) {
            return "Пользователь " + author + " подписался на Вас.";
        } else {
            return "Пользователь " + author + " оставил комментарий под Вашей записью:";
        }
    }
    public String getRecord() {
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
