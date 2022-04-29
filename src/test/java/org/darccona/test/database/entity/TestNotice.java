package org.darccona.test.database.entity;

import org.darccona.database.entity.NoticeEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class TestNotice {

    @Autowired
    private NoticeEntity notice1;
    @Autowired
    private NoticeEntity notice2;
    @Autowired
    private NoticeEntity notice3;
    @Autowired
    private NoticeEntity notice4;

    @Test
    public void testOutput() {
        Assert.assertEquals(notice4.getType(), 4);

        Assert.assertEquals(notice4.getAuthor(), "author");
        Assert.assertEquals(notice4.getRecord(), 1);
        Assert.assertEquals(notice4.getComm(), "comm");
    }

    @Test
    public void testType1() {
        Assert.assertEquals(notice1.getType(), 1);
        Assert.assertEquals(notice1.getText(), "Пользователь author подписался на Вас.");
    }

    @Test
    public void testType2() {
        Assert.assertEquals(notice2.getType(), 2);
        Assert.assertEquals(notice2.getText(), "Пользователю author понравилась Ваша запись:");
    }

    @Test
    public void testType3() {
        Assert.assertEquals(notice3.getType(), 3);
        Assert.assertEquals(notice3.getText(), "Пользователь author оставил комментарий под Вашей записью:");
    }

    @Test
    public void testType4() {
        Assert.assertEquals(notice4.getType(), 4);
        Assert.assertEquals(notice4.getText(), "Пользователь author ответил на Ваш комментарий:");
    }

    @Configuration
    static class Config {
        @Bean
        NoticeEntity notice1() {
            return new NoticeEntity("author", -1, null, 1);
        }

        @Bean
        NoticeEntity notice2() {
            return new NoticeEntity("author", 1, null, 2);
        }

        @Bean
        NoticeEntity notice3() {
            return new NoticeEntity("author", 1, "comm", 3);
        }

        @Bean
        NoticeEntity notice4() {
            return new NoticeEntity("author", 1, "comm", 4);
        }
    }

}
