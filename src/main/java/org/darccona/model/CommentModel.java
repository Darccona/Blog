package org.darccona.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс модели комментария
 */
public class CommentModel {

    /**
     * Класс модели ответа на комментарий
     */
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
}
