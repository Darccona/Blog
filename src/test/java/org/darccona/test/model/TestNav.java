package org.darccona.test.model;

import org.darccona.model.NavModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class TestNav {

    @Autowired
    private NavModel model;

    @Test
    public void testOutput() {
        Assert.assertEquals(model.getUrl(), "url");
        Assert.assertEquals(model.getName(), "name");

        Assert.assertEquals(model.getBool(), false);
        model.setBool();
        Assert.assertEquals(model.getBool(), true);
    }

    @Configuration
    static class Config {
        @Bean
        NavModel model() {
            return new NavModel("url", "name");
        }
    }
}
