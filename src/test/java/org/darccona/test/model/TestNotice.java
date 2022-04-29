package org.darccona.test.model;

import org.darccona.model.NoticeModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
public class TestNotice {

    @Autowired
    private NoticeModel model;

    static private Date date = new Date();
    private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy HH:mm");

    @Test
    public void testOutput() {
        Assert.assertEquals(model.getText(), "text");
        Assert.assertEquals(model.getString(), "string");
        Assert.assertEquals(model.getDate(), format.format(date));
        Assert.assertEquals(model.getDateSort(), date);
        Assert.assertEquals(model.getComm(), true);

        Assert.assertEquals(model.getId(), 1);
        model.setId(2);
        Assert.assertEquals(model.getId(), 2);
    }

    @Configuration
    static class Config {
        @Bean
        NoticeModel model() {
            return new NoticeModel("text", "string", date, true, 1);
        }
    }
}
