package org.darccona.database.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Класс объекта записи
 */
@Entity
public class RecordEntity {

    /**
     * номер записи
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    /**
     * текст записи
     */
    @Column(name = "TEXT", length = 16383)
    private String text;

    /**
     * дата выкладывания записи
     */
    @Column(name = "DATE")
    private Date date;

    /**
     * количество пользователей, которым понравилась запись
     */
    @Column(name = "LIKEUSER")
    private int like;

    /**
     * количество комментарий под записью
     */
    @Column(name = "COMMUSER")
    private int comm;

    public RecordEntity() { }
    public RecordEntity(String text) {
        this.text = text;
        this.date = new Date();
        this.like = 0;
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

    public void setComm() {
        this.comm += 1;
    }
    public int getComm() {
        return comm;
    }
    public void removeComm() {
        this.comm -= 1;
    }
    public void removeComm(int i) {
        this.comm -= i;
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

    @JsonIgnore
    @OneToMany(mappedBy = "record", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<CommentEntity> comment = new HashSet<>();

    public void setComment(Set<CommentEntity> comment) {
        this.comment = comment;
    }
    public Set<CommentEntity> getComment() {
        return comment;
    }
    public void removeComment(CommentEntity comment) {
        getComment().remove(comment);
    }
}
