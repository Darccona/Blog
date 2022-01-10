package org.darccona.model;

public class SettingModel {
    private String description;
    private String nameBlog;
    private String code;
    private boolean closed;

    public SettingModel(String description, String code, String nameBlog, boolean closed) {
        this.description = description;
        this.nameBlog = nameBlog;
        this.code = code;
        this.closed = closed;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }

    public void setNameBlog(String nameBlog) {
        this.nameBlog = nameBlog;
    }
    public String getNameBlog() {
        return nameBlog;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }
    public boolean getClosed() {
        return closed;
    }
}
