package org.darccona.model;

public class UserModel {
    private String text;

    public UserModel(String text) {
        this.text = text;
    }

    public void setText(String text) {
        this.text = text;
    }
    public String getText() {
        return text;
    }
}
