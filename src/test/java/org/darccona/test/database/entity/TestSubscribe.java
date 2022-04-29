package org.darccona.test.database.entity;

import org.darccona.database.entity.SubscribeEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class TestSubscribe {
    @Autowired
    private SubscribeEntity subEntity;

    @Test
    public void testName() {
        Assert.assertEquals(subEntity.getName(), "name");
    }

    @Configuration
    static class Config {
        @Bean
        SubscribeEntity subEntity() {
            return new SubscribeEntity("name");
        }
    }
}
