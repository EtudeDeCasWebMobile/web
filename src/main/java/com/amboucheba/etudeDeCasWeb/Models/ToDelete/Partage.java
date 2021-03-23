package com.amboucheba.etudeDeCasWeb.Models.ToDelete;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "partages") // create the table in public schema
public class Partage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(name = "id_user")
    @JoinColumn(name = "id_user", nullable = false)
    @ManyToOne
    private Users users;

//    @Column(name = "id_SerieTemporelle")
    @ManyToOne
    @JoinColumn(name = "id_SerieTemporelle", nullable = false)
    private SerieTemporelle serieTemporelle;

    @Column(name = "type")
    private String type;

    public Partage(Long id, Users users, SerieTemporelle serieTemporelle, String type) {
        this.id = id;
        this.users = users;
        this.serieTemporelle = serieTemporelle;
        this.type = type;
    }

    public Partage(Users users, SerieTemporelle serieTemporelle, String type) {
        this.users = users;
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

    public Users getUser() {
        return users;
    }

    public void setUser(Users users) {
        this.users = users;
    }

    public SerieTemporelle getSerieTemporelle() {
        return serieTemporelle;
    }

    public void setSerieTemporelle(SerieTemporelle serieTemporelle) {
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Partage partage = (Partage) o;
        return Objects.equals(id, partage.id) &&
                Objects.equals(users, partage.users) &&
                Objects.equals(serieTemporelle, partage.serieTemporelle) &&
                type.equals(partage.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
