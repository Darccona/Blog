package org.darccona.test.model;

import org.darccona.model.BoolModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class TestBool {

    @Autowired
    private BoolModel model1;
    @Autowired
    private BoolModel model2;
    @Autowired
    private BoolModel model3;
    @Autowired
    private BoolModel model4;

    @Test
    public void testOutput() {
        Assert.assertEquals(model1.getLogin(), false);
        Assert.assertEquals(model2.getMyRecord(), true);
        Assert.assertEquals(model3.getUserRecord(), true);
        Assert.assertEquals(model4.getLogin(), true);
        Assert.assertEquals(model4.getMyRecord(), false);
        Assert.assertEquals(model4.getUserRecord(), false);

        Assert.assertEquals(model4.getEmpty(), false);
        model4.setEmpty();
        Assert.assertEquals(model4.getEmpty(), true);

        Assert.assertEquals(model4.getInputStart(), false);
        model4.setInputStart();
        Assert.assertEquals(model4.getInputStart(), true);
    }

    @Configuration
    static class Config {
        @Bean
        BoolModel model1() {
            return new BoolModel("login");
        }

        @Bean
        BoolModel model2() {
            return new BoolModel("myRecord");
        }

        @Bean
        BoolModel model3() {
            return new BoolModel("userRecord");
        }

        @Bean
        BoolModel model4() {
            return new BoolModel("");
        }
    }
}
