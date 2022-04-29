package org.darccona.test.database.entity;

import org.darccona.database.entity.RecordEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
public class TestRecord {

    @Autowired
    private RecordEntity recordEntity;

    private Date date;

    @Test
    public void testText() {
        Assert.assertEquals(recordEntity.getText(), "text");
        recordEntity.setText("new text");
        Assert.assertEquals(recordEntity.getText(), "new text");
    }

    @Test
    public void testDate() {
        date = new Date();
        recordEntity.setDate(date);
        Assert.assertEquals(recordEntity.getDate(), date);
    }

    @Test
    public void testLike() {
        Assert.assertEquals(recordEntity.getLike(), 0);
        recordEntity.setLike();
        Assert.assertEquals(recordEntity.getLike(), 1);
        recordEntity.removeLike();
        Assert.assertEquals(recordEntity.getLike(), 0);
    }

    @Test
    public void testComm() {
        Assert.assertEquals(recordEntity.getComm(), 0);
        recordEntity.setComm();
        Assert.assertEquals(recordEntity.getComm(), 1);
        recordEntity.removeComm();
        Assert.assertEquals(recordEntity.getComm(), 0);
        recordEntity.setComm();
        recordEntity.setComm();
        recordEntity.setComm();
        recordEntity.removeComm(2);
        Assert.assertEquals(recordEntity.getComm(), 1);
    }

    @Configuration
    static class Config {
        @Bean
        RecordEntity recordEntity() {
            return new RecordEntity("text");
        }
    }
}
