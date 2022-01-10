package org.darccona.model;

public class PasswordModel {
    private String password1;
    private String password2;

    public PasswordModel() {
        password1 = "";
        password2 = "";
    }

    public void setPassword1(String password) {
        this.password1 = password;
    }
    public String getPassword1() {
        return password1;
    }

    public void setPassword2(String password) {
        this.password2 = password;
    }
    public String getPassword2() {
        return password2;
    }
}
