package com.mir.lasalle_user_service.controller.form;

public class LasalleUserForm {

    private String email;
    private String password;
    private String name;

    public LasalleUserForm() {
    }

    public LasalleUserForm(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
