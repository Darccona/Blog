package org.darccona.model;

import java.util.Comparator;
import java.util.Date;

public class RecordModel {
    private String text;
    private String username;
    private Date date;
    private int like;
    private Boolean likeBool;
    private Boolean favBool;
    long id;

    public RecordModel(String text, String username, Date date, int like, Boolean likeBool, Boolean favBool, long id) {
        this.text = text;
        this.username = username;
        this.date = date;
        this.like = like;
        this.likeBool = likeBool;
        this.favBool = favBool;
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }
    public String getText() {
        return text;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    public Date getDate() {
        return date;
    }

    public void setLike(int like) {
        this.like = like;
    }
    public int getLike() {
        return like;
    }

    public void setLikeBool(Boolean bool) {
        this.likeBool = bool;
    }
    public Boolean getLikeBool() {
        return likeBool;
    }

    public void setFavBool(Boolean bool) {
        this.favBool = bool;
    }
    public Boolean getFavBool() {
        return favBool;
    }

    public void setId(long id) {
        this.id = id;
    }
    public long getId() {
        return id;
    }

    public static Comparator<RecordModel> COMPARE_BY_DATE = new Comparator<RecordModel>() {
        public int compare(RecordModel other, RecordModel one) {
            return one.date.compareTo(other.date);
        }
    };
}
