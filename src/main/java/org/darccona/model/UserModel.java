package org.darccona.model;

public class UserModel {
    private String name;
    private String description;

    public UserModel(String name) {
        this.name = name;
    }

    public UserModel(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
}
