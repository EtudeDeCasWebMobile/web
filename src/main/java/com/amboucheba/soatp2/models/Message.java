package com.amboucheba.soatp2.models;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "messages") // create the table in public schema
public class Message implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    @NotBlank(message = "Field 'username' is required")
    @Size( min = 6, max = 255, message = "Username length must be between 6 and 255")
    private String username;

    @Column(name = "text")
    @NotBlank(message = "Field 'text' is required")
    @Size( min = 1, max = 255, message = "Text length must not exceed 255")
    private String text;

    @Column(name = "created_at")
    private Date created_at = new Date();

    public Message(Long id, String username, String text, Date created_at) {
        this.id = id;
        this.username = username;
        this.text = text;
        this.created_at = created_at;
    }

    public Message(String username, String text) {
        this.username = username;
        this.text = text;
    }

    public Message() {
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(id, message.id) &&
                username.equals(message.username) &&
                text.equals(message.text) &&
                Objects.equals(created_at, message.created_at);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, text, created_at);
    }
}
