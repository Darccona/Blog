package org.darccona.database.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Класс объекта уведомления
 */
@Entity
public class NoticeEntity {

    /**
     * номер уведомления
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    /**
     * дата отправки уведомления
     */
    @Column(name = "DATE")
    private Date date;

    /**
     * пользователь, вызвавший уведомления
     */
    @Column(name = "AUTHOR")
    private String author;

    /**
     * запись, связанная с уведомлением
     */
    @Column(name = "RECORD")
    private long record;

    /**
     * комментарий, связанный с уведомлением
     */
    @Column(name = "COMM")
    private String comm;

    /**
     * причина уведомления
     */
    @Column(name = "TYPE")
    private int type;

    public NoticeEntity() { }
    public NoticeEntity(String author, long record, String comm, int type) {
        this.date = new Date();
        this.author = author;
        this.record = record;
        this.comm = comm;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public String getText() {
        if (type == 1) {
            return "Пользователь " + author + " подписался на Вас.";
        } else if (type == 2) {
            return "Пользователю " + author + " понравилась Ваша запись:";
        } else if (type == 3) {
            return "Пользователь " + author + " оставил комментарий под Вашей записью:";
        } else {
            return "Пользователь " + author + " ответил на Ваш комментарий:";
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
    public int getType() {
        return type;
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
