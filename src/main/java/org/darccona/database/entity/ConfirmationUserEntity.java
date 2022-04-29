package org.darccona.database.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Класс объекта подтверждения аккаунта
 */
@Entity
public class ConfirmationUserEntity {

    /**
     * имя пользоваателя, чей аккаунт подтверждают
     */
    @Id
    @Column(name = "NAME")
    private String name;

    /**
     * электронная почта пользователя
     */
    @Column(name = "EMAIL")
    private String email;

    /**
     * дата, когда ссылка была отправленна
     */
    @Column(name = "DATE")
    private Date date;

    /**
     * ссылка для подтверждения аккаунта
     */
    @Column(name = "LINK")
    private String link;

    /**
     * код для подтверждения аккаунта
     */
    @Column(name = "CODE")
    private String code;

    public ConfirmationUserEntity() {}
    public ConfirmationUserEntity(String name, String email, String link, String code) {
        this.name = name;
        this.email = email;
        this.link = link;
        this.code = code;
        date = new Date();
    }

    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }

    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public Date getDate() {
        return date;
    }
    public void setDate() {
        date = new Date();
    }
}
