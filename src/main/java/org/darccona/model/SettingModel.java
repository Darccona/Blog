package org.darccona.model;

public class SettingModel {
    private String description;
    private String nameBlog;
    private boolean closed;

    public SettingModel(String description, String nameBlog, boolean closed) {
        this.description = description;
        this.nameBlog = nameBlog;
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

    public void setClosed(boolean closed) {
        this.closed = closed;
    }
    public boolean getClosed() {
        return closed;
    }
}
