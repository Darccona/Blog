package org.darccona.model;

/**
 * Класс модели одной строки
 */
public class StringModel {
    private String text;

    public StringModel() {
        this.text = "";
    }
    public StringModel(String text) {
        this.text = text;
    }

    public void setText(String text) {
        this.text = text;
    }
    public String getText() {
        return text;
    }
}