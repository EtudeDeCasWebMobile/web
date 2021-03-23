package com.amboucheba.etudeDeCasWeb.Models.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique = true)
    @Email
    @NotEmpty(message = "Field 'email' is required")
    private String email;

    @Column(name = "password")
    @NotBlank(message = "Field 'password' is required")
    @Size( min = 6, max = 255, message = "Password length must be between 6 and 255")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @OneToOne
    @JoinColumn(name = "position")
    private Location position;

    public User(@Email @NotEmpty(message = "Field 'email' is required") String email,  @NotBlank(message = "Field 'password' is required") @Size(min = 0, max = 255, message = "Password length must be between 6 and 255") String password) {
        this.email = email;
        this.password = password;
    }

    public User(Long id, @Email @NotEmpty(message = "Field 'email' is required") String email,  @NotBlank(message = "Field 'password' is required") @Size(min = 0, max = 255, message = "Password length must be between 6 and 255") String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public User() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Location getPosition() {
        return position;
    }

    public void setPosition(Location position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}
