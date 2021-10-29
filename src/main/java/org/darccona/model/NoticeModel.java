package org.darccona.model;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class NoticeModel {
    private Boolean like = false;
    private Boolean sub = false;
    private Boolean comm = false;

    private String text;
    private String record;
    private String commText;
    private Date date;

    long id;
    private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy HH:mm");

    public NoticeModel(String text, String record, String comm, Date date, int type, long id) {
        this.text = text;
        this.record = record;
        this.commText = comm;
        this.date = date;
        this.id = id;

        switch (type) {
            case 1: this.like = true; break;
            case 2: this.sub = true; break;
            case 3: this.comm = true; break;
        }
    }

    public String getText() {
        return text;
    }
    public String getRecord() {
        return record;
    }
    public String getCommText() {
        return commText;
    }
    public String getDate() {
        return format.format(date);
    }

    public Boolean getLike() {
        return like;
    }
    public Boolean getSub() {
        return sub;
    }
    public Boolean getComm() {
        return comm;
    }

    public void setId(long id) {
        this.id = id;
    }
    public long getId() {
        return id;
    }

    public static Comparator<NoticeModel> COMPARE_BY_DATE = new Comparator<NoticeModel>() {
        public int compare(NoticeModel other, NoticeModel one) {
            return one.date.compareTo(other.date);
        }
    };
}
