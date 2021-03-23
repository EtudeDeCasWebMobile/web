package com.amboucheba.etudeDeCasWeb.Models.Inputs;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


public class AuthInput {

    @Email(message = "Email address is not valid")
    private String email;
    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 6, max = 50, message = "Password length must be between 6 and 50")
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
