package com.amboucheba.seriesTemporellesTpWeb.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "partages") // create the table in public schema
public class Partage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_user")
    private String user;

    @Column(name = "id_SerieTemporelle")
    private String serieTemporelle;

    @Column(name = "type")
    private String type;

    public Partage(Long id, String user, String serieTemporelle, String type) {
        this.id = id;
        this.user = user;
        this.serieTemporelle = serieTemporelle;
        this.type = type;
    }

    public Partage(String user, String serieTemporelle, String type) {
        this.user = user;
        this.serieTemporelle = serieTemporelle;
        this.type = type;
    }

    public Partage() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSerieTemporelle() {
        return serieTemporelle;
    }

    public void setSerieTemporelle(String serieTemporelle) {
        this.serieTemporelle = serieTemporelle;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        Partage partage = (Partage) o;
        return partage.getId().equals(this.id) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
