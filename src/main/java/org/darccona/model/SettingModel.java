package org.darccona.model;

public class SettingModel {
    private String description;
    private boolean closed;

    public SettingModel(String description, boolean closed) {
        this.description = description;
        this.closed = closed;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }
    public boolean getClosed() {
        return closed;
    }
}
