package org.darccona.database.entity;

import javax.persistence.*;
import java.util.Comparator;
import java.util.Date;

@Entity
public class RecordEntity {

    @Id

    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @Column(name = "TEXT")
    private String text;

    @Column(name = "DATE")
    private Date date;

    @Column(name = "LIKEUSER")
    private int like;

    @Column(name = "FAVUSER")
    private int fav;

    @Column(name = "COMMUSER")
    private int comm;

    public RecordEntity() { }
    public RecordEntity(String text) {
        this.text = text;
        this.date = new Date();
        this.like = 0;
        this.fav = 0;
        this.comm = 0;
    }

    public long getId() {
        return id;
    }

    public void setText(String text) {
        this.text = text;
    }
    public String getText() {
        return text;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    public Date getDate() {
        return date;
    }

    public void setLike() {
        this.like += 1;
    }
    public int getLike() {
        return like;
    }
    public void removeLike() {
        this.like -= 1;
    }

    public void setFav() {
        this.fav += 1;
    }
    public int getFav() {
        return fav;
    }
    public void removeFav() {
        this.fav -= 1;
    }

    public void setComm() {
        this.comm += 1;
    }
    public int getComm() {
        return comm;
    }
    public void removeComm() {
        this.comm -= 1;
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
