package org.darccona.test.database.entity;

import org.darccona.database.entity.UserEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class TestUser {

    @Autowired
    private UserEntity userEntity;

    @Test
    public void testName() {
        Assert.assertEquals(userEntity.getName(), "name");
        userEntity.setName("new name");
        Assert.assertEquals(userEntity.getName(), "new name");
    }

    @Test
    public void testEmail() {
        Assert.assertEquals(userEntity.getEmail(), "email");
        userEntity.setEmail("new email");
        Assert.assertEquals(userEntity.getEmail(), "new email");
    }

    @Test
    public void testPassword() {
        Assert.assertEquals(userEntity.getPassword(), "password");
        userEntity.setPassword("new password");
        Assert.assertEquals(userEntity.getPassword(), "new password");
    }

    @Test
    public void testRole() {
        Assert.assertEquals(userEntity.getRole(), "USER");
        userEntity.setRole("ADMIN");
        Assert.assertEquals(userEntity.getRole(), "ADMIN");
    }

    @Test
    public void testNameBlog() {
        Assert.assertEquals(userEntity.getNameBlog(), "Какой-то блог");
        userEntity.setNameBlog("новое имя блога");
        Assert.assertEquals(userEntity.getNameBlog(), "новое имя блога");
    }

    @Test
    public void testDescription() {
        Assert.assertEquals(userEntity.getDescription(), "");
        userEntity.setDescription("новое описание блога");
        Assert.assertEquals(userEntity.getDescription(), "новое описание блога");
    }

    @Test
    public void testClosed() {
        Assert.assertEquals(userEntity.getClosed(), false);
        userEntity.setClosed(true);
        Assert.assertEquals(userEntity.getClosed(), true);
        userEntity.setClosed(false);
        Assert.assertEquals(userEntity.getClosed(), false);
    }

    @Test
    public void testConfirmed() {
        Assert.assertEquals(userEntity.getConfirmed(), false);
        userEntity.setConfirmed(true);
        Assert.assertEquals(userEntity.getConfirmed(), true);
        userEntity.setConfirmed(false);
        Assert.assertEquals(userEntity.getConfirmed(), false);
    }

    @Configuration
    static class Config {
        @Bean
        UserEntity userEntity() {
            return new UserEntity("name", "email", "password");
        }
    }
}
