package com.amboucheba.etudeDeCasWeb.Models.Inputs;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Objects;

public class RegisterInput {

    @Email
    @NotEmpty(message = "Field 'email' is required")
    private String email;

    @NotBlank(message = "Field 'password' is required")
    @Size( min = 0, max = 255, message = "Password length must be between 6 and 255")
    private String password;

    public RegisterInput(@Email @NotEmpty(message = "Field 'email' is required") String email,  @NotBlank(message = "Field 'password' is required") @Size(min = 0, max = 255, message = "Password length must be between 6 and 255") String password) {
        this.email = email;
        this.password = password;
    }

    public RegisterInput() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegisterInput that = (RegisterInput) o;
        return email.equals(that.email) &&
                password.equals(that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password);
    }
}
