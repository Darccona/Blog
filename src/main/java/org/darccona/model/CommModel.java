package org.darccona.model;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class CommModel {
    private String name;
    private String text;
    private Date date;
    private int like;

    long id;
    private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy HH:mm");

    public CommModel(String name, String text, Date date, int like) {
        this.name = name;
        this.text = text;
        this.date = date;
        this.like = like;
    }

    public String getName() {
        return name;
    }
    public String getText() {
        return text;
    }
    public String getDate() {
        return format.format(date);
    }
    public int getLike() {
        return like;
    }

    public static Comparator<CommModel> COMPARE_BY_DATE_NEW = new Comparator<CommModel>() {
        public int compare(CommModel other, CommModel one) {
            return one.date.compareTo(other.date);
        }
    };

    public static Comparator<CommModel> COMPARE_BY_DATE_LAST = new Comparator<CommModel>() {
        public int compare(CommModel one, CommModel other) {
            return one.date.compareTo(other.date);
        }
    };
}
