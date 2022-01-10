package org.darccona.database.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class CommentEntity {

    @Id

    @Column(name = "ID")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "TEXT")
    private String text;

    @Column(name = "DATE")
    private Date date;

    public CommentEntity() {}
    public CommentEntity(String name, String text) {
        this.name = name;
        this.text = text;
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

    public void setDate(Date date) {
        this.date = date;
    }
    public Date getDate() {
        return date;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "record", nullable = false)
    private RecordEntity record;

    public void setRecord(RecordEntity record) {
        this.record = record;
    }
    public RecordEntity getRecord() {
        return record;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<CommReplyEntity> commReply = new HashSet<>();

    public void setCommReply(Set<CommReplyEntity> comment) {
        this.commReply = comment;
    }
    public Set<CommReplyEntity> getCommReply() {
        return commReply;
    }
    public void removeCommReply(CommReplyEntity comment) {
        getCommReply().remove(comment);
    }
    public int getNumReply() {
        return commReply.size();
    }
}
