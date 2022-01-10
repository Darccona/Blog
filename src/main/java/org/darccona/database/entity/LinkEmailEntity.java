package org.darccona.database.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class LinkEmailEntity {

    @Id

    @Column(name = "NAME")
    private String name;

    @Column(name = "LINK")
    private String link;

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
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
