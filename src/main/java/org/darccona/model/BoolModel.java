package org.darccona.model;

public class BoolModel {
    private boolean sub = false;

    private boolean rec = false;
    private boolean like = false;
    private boolean fav = false;

    private boolean myRecord = false;
    private boolean userRecord = false;

    public BoolModel(String div) {
        switch (div) {
            case "sub": this.sub = true; break;
            case "rec": this.rec = true; break;
            case "like": this.like = true; break;
            case "fav": this.fav = true; break;
            case "myRecord": this.myRecord = true; break;
            case "userRecord": this.userRecord = true; break;
        }
    }

    public boolean getSub() {
        return sub;
    }

    public boolean getRec() {
        return rec;
    }
    public boolean getLike() {
        return like;
    }
    public boolean getFav() {
        return fav;
    }

    public boolean getMyRecord() {
        return myRecord;
    }
    public boolean getUserRecord() {
        return userRecord;
    }
}

