package org.darccona.database.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Класс объекта смены пароля аккаунта
 */
@Entity
public class LinkEmailEntity {

    /**
     * имя пользователя, меняющего пароль
     */
    @Id
    @Column(name = "NAME")
    private String name;

    /**
     * ссылка для смены пароля
     */
    @Column(name = "LINK")
    private String link;

    /**
     * дата, когда ссылка была отправленна
     */
    @Column(name = "DATE")
    private Date date;

    public LinkEmailEntity() {}
    public LinkEmailEntity(String name, String link) {
        this.name = name;
        this.link = link;
        date = new Date();
    }

    public String getName() {
        return name;
    }

    public void setLink(String link) {
        this.link = link;
    }
    public String getLink() {
        return link;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    public Date getDate() {
        return date;
    }
}
