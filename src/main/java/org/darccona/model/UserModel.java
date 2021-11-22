package org.darccona.model;

public class UserModel {
    private String name;
    private String nameBlog;
    private String description;

    public UserModel(String name) {
        this.name = name;
    }

    public UserModel(String name, String nameBlog, String description) {
        this.name = name;
        this.nameBlog = nameBlog;
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setNameBlog(String nameBlog) {
        this.nameBlog = nameBlog;
    }
    public String getNameBlog() {
        return nameBlog;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
}
