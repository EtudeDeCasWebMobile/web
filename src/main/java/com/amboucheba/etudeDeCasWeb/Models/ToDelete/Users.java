package com.amboucheba.etudeDeCasWeb.Models.ToDelete;


import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "users_") // create the table in public schema
public class Users implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true)
    @NotBlank(message = "Field 'username' is required")
    @Size( min = 0, max = 255, message = "Username length must be between 6 and 255")
    private String username;

    @Column(name = "password")
    @NotBlank(message = "Field 'password' is required")
    @Size( min = 0, max = 255, message = "Password length must be between 6 and 255")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    public Users(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public Users(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Users() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        Users users = (Users) o;
        return username.equals(users.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
