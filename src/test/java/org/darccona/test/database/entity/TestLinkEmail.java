package org.darccona.test.database.entity;

import org.darccona.database.entity.LinkEmailEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
public class TestLinkEmail {

    @Autowired
    private LinkEmailEntity linkEntity;

    private Date date;

    @Test
    public void testName() {
        Assert.assertEquals(linkEntity.getName(), "name");
    }

    @Test
    public void testLink() {
        Assert.assertEquals(linkEntity.getLink(), "link");
        linkEntity.setLink("new link");
        Assert.assertEquals(linkEntity.getLink(), "new link");
    }

    @Test
    public void testDate() {
        date = new Date();
        linkEntity.setDate(date);
        Assert.assertEquals(linkEntity.getDate(), date);
    }

    @Configuration
    static class Config {
        @Bean
        LinkEmailEntity linkEntity() {
            return new LinkEmailEntity("name", "link");
        }
    }
}
