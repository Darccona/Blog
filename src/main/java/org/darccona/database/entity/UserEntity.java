package org.darccona.database.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class UserEntity {

    @Id

    @Column(name = "NAME")
    private String name;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "ROLE")
    private String role;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "CLOSED")
    private boolean closed;

    public UserEntity() {}
    public UserEntity(String name, String password) {
        this.name = name;
        this.password = password;
        this.role = "USER";
        this.description = "";
        this.closed = false;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }

    public void setRole(String role) {
        this.role = role;
    }
    public String getRole() {
        return role;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }
    public boolean getClosed() {
        return closed;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<RecordEntity> record = new HashSet<>();

    public void setRecord(Set<RecordEntity> record) {
        this.record = record;
    }
    public Set<RecordEntity> getRecord() {
        return record;
    }
    public void removeRecord(RecordEntity record) {
        getRecord().remove(record);
    }

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<NoticeEntity> notice = new HashSet<>();

    public void setNotice(Set<NoticeEntity> notice) {
        this.notice = notice;
    }
    public Set<NoticeEntity> getNotice() {
        return notice;
    }
    public void removeNotice(NoticeEntity notice) {
        getNotice().remove(notice);
    }
    public void removeAllNotice() {
        notice.clear();
    }

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<SubscribeEntity> subscribe = new HashSet<>();

    public void setSubscribe(Set<SubscribeEntity> subscribe) {
        this.subscribe = subscribe;
    }
    public Set<SubscribeEntity> getSubscribe() {
        return subscribe;
    }
    public void removeSubscribe(SubscribeEntity subscribe) {
        getSubscribe().remove(subscribe);
    }

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<LikeEntity> like = new HashSet<>();

    public void setLike(Set<LikeEntity> like) {
        this.like = like;
    }
    public Set<LikeEntity> getLike() {
        return like;
    }
    public void removeLike(LikeEntity like) {
        getLike().remove(like);
    }

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<FavoriteEntity> favorite = new HashSet<>();

    public void setFavorite(Set<FavoriteEntity> favorite) {
        this.favorite = favorite;
    }
    public Set<FavoriteEntity> getFavorite() {
        return favorite;
    }
    public void removeFavorite(FavoriteEntity favorite) {
        getFavorite().remove(favorite);
    }

}
