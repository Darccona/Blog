package org.darccona.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Класс модели уведомления
 */
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
}
