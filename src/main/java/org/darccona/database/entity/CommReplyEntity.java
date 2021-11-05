package org.darccona.database.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class CommReplyEntity {
    @Id

    @Column(name = "ID")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "TEXT")
    private String text;

    @Column(name = "COMMID")
    private long commId;

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
