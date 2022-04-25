package org.darccona.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Класс модели данных о записи
 */
public class RecordModel {
    private String[] text;
    private String username;
    private String nameBlog;
    private Date date;
    private int like;
    private int comm;
    private Boolean likeBool;
    private Boolean favBool;

    long id;
    private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy HH:mm");

    public RecordModel(String[] text, String username, String nameBlog, Date date, int like, int comm, Boolean likeBool, Boolean favBool, long id) {
        this.text = text;
        this.username = username;
        this.nameBlog = nameBlog;
        this.date = date;
        this.like = like;
        this.comm = comm;
        this.likeBool = likeBool;
        this.favBool = favBool;
        this.id = id;
    }

    public void setText(String[] text) {
        this.text = text;
    }
    public String[] getText() {
        return text;
    }

    public boolean getBoolText() {
        return (text != null);
    }
    public boolean getBoolImage() {
        return false;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }

    public String getNameBlog() {
        return nameBlog;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    public Date getDateSort() {
        return date;
    }
    public String getDate() {
        return format.format(date);
    }

    public void setLike(int like) {
        this.like = like;
    }
    public int getLike() {
        return like;
    }

    public void setComm(int comm) {
        this.comm = comm;
    }
    public int getComm() {
        return comm;
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
}
