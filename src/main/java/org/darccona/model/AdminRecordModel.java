package org.darccona.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Класс модели всех данных о записи
 */
public class AdminRecordModel {
    private long id;
    private String[] text;
    private Date date;
    private String userName;
    private String userNameBlog;
    private List<UserModel> likeUser;
    private List<UserModel> favUser;
    private List<CommentModel> comments;

    private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy HH:mm");

    public AdminRecordModel(long id, String userName, String userNameBlog, String[] text, Date date) {
        this.id = id;
        this.userName = userName;
        this.userNameBlog = userNameBlog;
        this.text = text;
        this.date = date;
    }

    public long getId() {
        return id;
    }
    public String getUsername() {
        return userName;
    }
    public String getNameBlog() {
        return userNameBlog;
    }
    public String getLink() {
        return "/blog/admin/record?id=" + id;
    }
    public boolean getBoolText() {
        return (text != null);
    }
    public String[] getText() {
        return text;
    }
    public String getDate() {
        return format.format(date);
    }

    public void setFavUser(List<UserModel> favUser) {
        this.favUser = favUser;
    }
    public int getNumFav() {
        return favUser.size();
    }
    public List<UserModel> getFavUser() {
        return favUser;
    }

    public void setLikeUser(List<UserModel> likeUser) {
        this.likeUser = likeUser;
    }
    public int getNumLike() {
        return likeUser.size();
    }
    public List<UserModel> getLikeUser() {
        return likeUser;
    }

    public void setComments(List<CommentModel> comments) {
        this.comments = comments;
    }
    public List<CommentModel> getComments() {
        return comments;
    }
}
