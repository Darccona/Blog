package org.darccona.test.model;

import org.darccona.model.AdminRecordModel;
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
public class TestAdminRecord {

    @Autowired
    private AdminRecordModel model;

    static private String[] text = new String[]{"1", "2"};
    static private Date date = new Date();
    private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy HH:mm");

    @Test
    public void testOutput() {
        Assert.assertEquals(model.getId(), 1);
        Assert.assertEquals(model.getUsername(), "name");
        Assert.assertEquals(model.getNameBlog(), "name blog");
        Assert.assertEquals(model.getBoolText(), true);
        Assert.assertArrayEquals(model.getText(), text);
        Assert.assertEquals(model.getLink(), "/blog/admin/record?id=1");
        Assert.assertEquals(model.getDate(), format.format(date));
    }

    @Configuration
    static class Config {
        @Bean
        AdminRecordModel model() {
            return new AdminRecordModel(1, "name", "name blog", text, date);
        }
    }
}
