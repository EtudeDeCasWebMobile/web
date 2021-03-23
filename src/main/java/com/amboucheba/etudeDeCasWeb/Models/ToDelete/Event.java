package com.amboucheba.etudeDeCasWeb.Models.ToDelete;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "evenements") // create the table in public schema
public class Event implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    @NotNull(message = "Field 'date' is required")
    private Date date;

    @Column(name = "valeur")
    @NotNull(message = "Field 'valeur' is required")
    private Float valeur;

    @Column(name = "commentaire")
    @Size( min = 0, max = 255, message = "Commentaire length must be between 0 and 255")
    private String commentaire;

    @JoinColumn(name = "id_SerieTemporelle", nullable = false)
    @ManyToOne
    private SerieTemporelle serieTemporelle;

    public Event(Long id,  Date date,  Float valeur,  String commentaire, SerieTemporelle serieTemporelle) {
        this.id = id;
        this.date = date;
        this.valeur = valeur;
        this.commentaire = commentaire;
        this.serieTemporelle = serieTemporelle;
    }

    public Event() {
    }

    public Event(Date date, float valeur, String commentaire) {
        this.date = date;
        this.valeur = valeur;
        this.commentaire = commentaire;
    }

    public Event(Date date, float valeur, String commentaire, SerieTemporelle serieTemporelle) {
        this.date = date;
        this.valeur = valeur;
        this.commentaire = commentaire;
        this.serieTemporelle = serieTemporelle;
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

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public SerieTemporelle getSerieTemporelle() {
        return serieTemporelle;
    }

    public void setSerieTemporelle(SerieTemporelle serieTemporelle) {
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
        Event user = (Event) o;
        return user.id == this.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
