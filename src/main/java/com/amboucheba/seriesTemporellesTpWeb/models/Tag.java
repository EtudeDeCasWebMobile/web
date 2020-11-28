package com.amboucheba.seriesTemporellesTpWeb.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "tags") // create the table in public schema
public class Tag implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "id_Evenement")
    private String event;

    public Tag(Long id, String name, String event) {
        this.id = id;
        this.name = name;
        this.event = event;
    }

    public Tag(String name, String event) {
        this.name = name;
        this.event = event;
    }

    public Tag() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    @Override
    public boolean equals(Object o) {
        Tag tag = (Tag) o;
        return tag.getId().equals(this.id) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
