package org.darccona.model;

/**
 * Класс модели данных для регистрации нового пользователя
 */
public class RegModel {
    private String name;
    private String email;
    private String password1;
    private String password2;

    public RegModel() { }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
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