package org.darccona.model;

public class BoolModel {
    private boolean login = true;
    private boolean empty = false;

    private boolean myRecord = false;
    private boolean userRecord = false;

    private boolean inputStart = false;

    public BoolModel(String div) {
        switch (div) {
            case "login": this.login = false; break;

            case "myRecord": this.myRecord = true; break;
            case "userRecord": this.userRecord = true; break;
        }
    }

    public void setEmpty() {
        empty = true;
    }
    public void setInputStart() {
        inputStart = true;
    }

    public boolean getLogin() {
        return login;
    }
    public boolean getEmpty() {
        return empty;
    }

    public boolean getMyRecord() {
        return myRecord;
    }
    public boolean getUserRecord() {
        return userRecord;
    }

    public boolean getInputStart() {
        return inputStart;
    }
}

