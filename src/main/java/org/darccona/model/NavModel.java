package org.darccona.model;

public class NavModel {
    String url;
    String name;
    boolean bool = false;

    public NavModel(String url, String name) {
        this.url = url;
        this.name = name;
    }

    public void setBool() {
        bool = true;
    }

    public String getUrl() {
        return url;
    }
    public String getName() {
        return name;
    }
    public boolean getBool() {
        return  bool;
    }
}