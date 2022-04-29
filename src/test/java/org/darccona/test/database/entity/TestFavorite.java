package org.darccona.test.database.entity;

import org.darccona.database.entity.FavoriteEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class TestFavorite {

    @Autowired
    private FavoriteEntity favEntity;

    @Test
    public void testRecord() {
        Assert.assertEquals(favEntity.getRecord(), 1);
    }

    @Configuration
    static class Config {
        @Bean
        FavoriteEntity favEntity() {
            return new FavoriteEntity(1);
        }
    }
}
