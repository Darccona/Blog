package org.darccona.model;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class NoticeModel {
    private boolean comm;

    private String text;
    private String string;
    private Date date;

    long id;
    private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy HH:mm");

    public NoticeModel(String text, String string, Date date, boolean bool, long id) {
        this.text = text;
        this.string = string;
        this.date = date;
        this.comm = bool;
        this.id = id;
    }

    public String getText() {
        return text;
    }
    public String getString() {
        return string;
    }
    public String getDate() {
        return format.format(date);
    }
    public Date getDateSort() {
        return date;
    }
    public boolean getComm() {
        return comm;
    }

    public void setId(long id) {
        this.id = id;
    }
    public long getId() {
        return id;
    }

//    public static Comparator<NoticeModel> COMPARE_BY_DATE = new Comparator<NoticeModel>() {
//        public int compare(NoticeModel other, NoticeModel one) {
//            return one.date.compareTo(other.date);
//        }
//    };
}
