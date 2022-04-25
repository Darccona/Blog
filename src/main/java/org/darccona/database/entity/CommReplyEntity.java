package org.darccona.database.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Класс объекта ответа на комментарии
 */
@Entity
public class CommReplyEntity {

    /**
     * номер комментария
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    /**
     * имя пользователя, оставившего комментарий
     */
    @Column(name = "NAME")
    private String name;

    /**
     * текст комментария
     */
    @Column(name = "TEXT")
    private String text;

    /**
     * номер комментария, на который отвечают
     */
    @Column(name = "COMMID")
    private long commId;

    /**
     * дата, когда был оставлен комментарий
     */
    @Column(name = "DATE")
    private Date date;

    public CommReplyEntity() {}
    public CommReplyEntity(String name, String text, long commId) {
        this.name = name;
        this.text = text;
        this.commId = commId;
        date = new Date();
    }

    public long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setText(String text) {
        this.text = text;
    }
    public String getText() {
        return text;
    }

    public void setCommId(long id) {
        commId = id;
    }
    public long getCommId() {
        return commId;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    public Date getDate() {
        return date;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "comment", nullable = false)
    private CommentEntity comment;

    public void setComment(CommentEntity comment) {
        this.comment = comment;
    }
    public CommentEntity getComment() {
        return comment;
    }
}
