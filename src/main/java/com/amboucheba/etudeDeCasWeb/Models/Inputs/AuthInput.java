package com.amboucheba.etudeDeCasWeb.Models.Inputs;

public class AuthInput {

    private String email;
    private String password;

    public AuthInput(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public AuthInput() {
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
}
