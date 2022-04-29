package org.darccona.test.database.entity;

import org.darccona.database.entity.ConfirmationUserEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
public class TestConfirmationUser {

    @Autowired
    private ConfirmationUserEntity confirmationEntity;

    private Date date;

    @Test
    public void testName() {
        Assert.assertEquals(confirmationEntity.getName(), "name");
    }

    @Test
    public void testEmail() {
        Assert.assertEquals(confirmationEntity.getEmail(), "email");
    }

    @Test
    public void testLink() {
        Assert.assertEquals(confirmationEntity.getLink(), "link");
        confirmationEntity.setLink("new link");
        Assert.assertEquals(confirmationEntity.getLink(), "new link");
    }

    @Test
    public void testCode() {
        Assert.assertEquals(confirmationEntity.getCode(), "code");
        confirmationEntity.setCode("new code");
        Assert.assertEquals(confirmationEntity.getCode(), "new code");
    }

    @Configuration
    static class Config {
        @Bean
        ConfirmationUserEntity confirmationEntity() {
            return new ConfirmationUserEntity("name", "email", "link", "code");
        }
    }
}
