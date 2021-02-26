package com.amboucheba.etudeDeCasWeb.Models.Inputs;


import javax.persistence.Entity;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


public class AuthInput {

    @Email
    private String email;
    @NotEmpty
    @Size(min = 6, max = 50, message = "Password length should be between 6 and 50")
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
