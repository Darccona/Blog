package org.darccona.model;

import java.text.SimpleDateFormat;
import java.util.*;

public class AdminUserModel {

    static class Record {
        private long id;
        private String[] text;
        private Date date;
        private int numComm;
        private int numLike;
        private int numFav;

        private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy HH:mm");

        Record(long id, String[] text, Date date, int comm, int like, int fav) {
            this.id = id;
            this.text = text;
            this.date = date;
            this.numComm = comm;
            this.numLike = like;
            this.numFav = fav;
        }

        public long getId() {
            return id;
        }

        public Boolean getBoolText() {
            return (text != null);
        }

        public String[] getText() {
            return text;
        }

        public String getDate() {
            return format.format(date);
        }

        public int getNumComm() {
            return numComm;
        }

        public int getNumLike() {
            return numLike;
        }

        public int getNumFav() {
            return numFav;
        }

        public static Comparator<AdminUserModel.Record> COMPARE_BY_DATE = new Comparator<AdminUserModel.Record>() {
            public int compare(AdminUserModel.Record other, AdminUserModel.Record one) {
                return one.date.compareTo(other.date);
            }
        };
    }

    private String link;
    private String userName;
    private String userNameBlog;
    private String userDescription;
    private List<UserModel> userSub;
    private List<Record> record;
    private boolean userAdmin;

    public AdminUserModel(String name) {
        link = "/blog/admin/blogUser?name=" + name;
        record = new ArrayList<>();
    }

    public void setUser(String name, String nameBlog, String description, boolean admin) {
        userName = name;
        userNameBlog = nameBlog;
        userDescription = description;
        userAdmin = admin;
    }

    public void setUserSub(List<UserModel> userSub) {
        this.userSub = userSub;
    }

    public void setRecord(long id, String[] text, Date date, int comm, int like, int fav) {
        record.add(new Record(id, text, date, comm, like, fav));
    }

    public void sortRecord() {
        if (record != null) {
            Collections.sort(record, Record.COMPARE_BY_DATE);
        }
    }

    public String getLink() {
        return link;
    }
    public String getName() {
        return userName;
    }
    public String getNameBlog() {
        return userNameBlog;
    }
    public String getDescription() {
        return userDescription;
    }
    public Boolean getAdmin() {
        return userAdmin;
    }

    public int getNumSub() {
        return userSub.size();
    }
    public List<UserModel> getUserSub() {
        return userSub;
    }
    public List<Record> getRecord() {
        return record;
    }
}
