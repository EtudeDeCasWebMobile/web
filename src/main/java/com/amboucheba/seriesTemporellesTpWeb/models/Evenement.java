package com.amboucheba.seriesTemporellesTpWeb.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "Evenements") // create the table in public schema
public class Evenement implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    @NotBlank(message = "Field 'date' is required")
    private Date date;

    @Column(name = "valeur")
    @NotBlank(message = "Field 'valeur' is required")
    private Float valeur;

    @Column(name = "commentaire")
    @NotBlank(message = "Field 'commentaire' is required")
    @Size( min = 0, max = 255, message = "Commentaire length must be between 0 and 255")
    private Float commentaire;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_SerieTemporelle", nullable = false)
    private User serieTemporelle;

    public Evenement() {
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Float getValeur() {
        return valeur;
    }

    public void setValeur(Float valeur) {
        this.valeur = valeur;
    }

    public Float getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(Float commentaire) {
        this.commentaire = commentaire;
    }

    public User getSerieTemporelle() {
        return serieTemporelle;
    }

    public void setSerieTemporelle(User serieTemporelle) {
        this.serieTemporelle = serieTemporelle;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object o) {
        Evenement user = (Evenement) o;
        return user.id == this.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
