package org.darccona.test.model;

import org.darccona.model.StringModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class TestString {

    @Autowired
    private StringModel model1;
    @Autowired
    private StringModel model2;

    @Test
    public void testName() {
        Assert.assertEquals(model1.getText(), "");
        Assert.assertEquals(model2.getText(), "text");

        model1.setText("new text");
        Assert.assertEquals(model1.getText(), "new text");
    }

    @Configuration
    static class Config {
        @Bean
        StringModel model1() {
            return new StringModel();
        }

        @Bean
        StringModel model2() {
            return new StringModel("text");
        }
    }
}
