package org.darccona.test.database.entity;

import org.darccona.database.entity.CommentEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
public class TestComment {

    @Autowired
    private CommentEntity commEntity;

    private Date date;

    @Test
    public void testName() {
        Assert.assertEquals(commEntity.getName(), "name");
        commEntity.setName("new name");
        Assert.assertEquals(commEntity.getName(), "new name");
    }

    @Test
    public void testText() {
        Assert.assertEquals(commEntity.getText(), "text");
        commEntity.setText("new text");
        Assert.assertEquals(commEntity.getText(), "new text");
    }

    @Test
    public void testDate() {
        date = new Date();
        commEntity.setDate(date);
        Assert.assertEquals(commEntity.getDate(), date);
    }

    @Configuration
    static class Config {
        @Bean
        CommentEntity commEntity() {
            return new CommentEntity("name", "text");
        }
    }
}
