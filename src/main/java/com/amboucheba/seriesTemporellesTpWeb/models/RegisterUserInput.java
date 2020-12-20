package com.amboucheba.seriesTemporellesTpWeb.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

public class RegisterUserInput {

    @NotBlank(message = "Field 'username' is required")
    @Size( min = 0, max = 255, message = "Username length must be between 6 and 255")
    private String username;

    @NotBlank(message = "Field 'password' is required")
    @Size( min = 0, max = 255, message = "Password length must be between 6 and 255")
    private String password;


    public RegisterUserInput(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public RegisterUserInput() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegisterUserInput that = (RegisterUserInput) o;
        return username.equals(that.username) &&
                password.equals(that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }
}
