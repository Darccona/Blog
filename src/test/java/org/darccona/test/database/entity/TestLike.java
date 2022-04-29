package org.darccona.test.database.entity;

import org.darccona.database.entity.LikeEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class TestLike {

    @Autowired
    private LikeEntity likeEntity;

    @Test
    public void testRecord() {
        Assert.assertEquals(likeEntity.getRecord(), 1);
    }

    @Configuration
    static class Config {
        @Bean
        LikeEntity likeEntity() {
            return new LikeEntity(1);
        }
    }
}
