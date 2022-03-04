package org.darccona.model;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class CommentModel {

    static class Reply {
        private long id;
        private String name;
        private String text;
        private Date date;

        private String nameReply;

        Reply(long id, String name, String nameReply, String text, Date date) {
            this.id = id;
            this.name = name;
            this.nameReply = nameReply;
            this.text = text;
            this.date = date;
        }

        public long getId() {
            return id;
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
        public Date getDateSort() {
            return date;
        }

        public String getNameReply() {
            if (nameReply.equals("")) {
                return "";
            }
            return " (в ответ " + nameReply + ')';
        }

//        public static Comparator<Reply> COMPARE_BY_DATE_LAST = new Comparator<Reply>() {
//            public int compare(Reply one, Reply other) {
//                return one.date.compareTo(other.date);
//            }
//        };
    }

    private long id;
    private String name;
    private String text;
    private Date date;

    private List<Reply> reply;
    private static SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy HH:mm");

    public CommentModel(long id, String name, String text, Date date) {
        this.id = id;
        this.name = name;
        this.text = text;
        this.date = date;

        reply = new ArrayList<>();
    }

    public long getId() {
        return id;
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
    public Date getDateSort() {
        return date;
    }

    public void setReply(long id, String name, String nameReply, String text, Date date) {
        reply.add(new Reply(id, name, nameReply, text, date));
    }
    public void sortReply() {
        reply = reply.stream().sorted(Comparator.comparing(Reply::getDateSort)).collect(Collectors.toList());
//        Collections.sort(reply, Reply.COMPARE_BY_DATE_LAST);
    }
    public boolean getBoolReply() {
        return (reply.size() != 0);
    }
    public int getNumReply() {
        return reply.size();
    }
    public List<Reply> getReply() {
        return reply;
    }

//    public static Comparator<CommentModel> COMPARE_BY_DATE_NEW = new Comparator<CommentModel>() {
//        public int compare(CommentModel other, CommentModel one) {
//            return one.date.compareTo(other.date);
//        }
//    };
}
