package com.amboucheba.seriesTemporellesTpWeb.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "SeriesTemporelles") // create the table in public schema
public class SerieTemporelle implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titre")
    @NotBlank(message = "Field 'titre' is required")
    @Size( min = 0, max = 255, message = "Titre length must be between 0 and 255")
    private String titre;

    @Column(name = "description")
    @NotBlank(message = "Field 'description' is required")
    @Size( min = 0, max = 255, message = "Description length must be between 0 and 255")
    private String description;

//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "owner", nullable = false)
    @ManyToOne
    private User owner;

//    @OneToMany(mappedBy = "serieTemporelle", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private Set<Evenement> evenements;

    public SerieTemporelle(Long id, String titre, String description, User owner) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.owner = owner;
    }

    public SerieTemporelle(  String titre, String description, User owner) {
        this.titre = titre;
        this.description = description;
        this.owner = owner;
    }

    public SerieTemporelle(String titre, String description) {
        this.titre = titre;
        this.description = description;
    }

    public SerieTemporelle() {
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SerieTemporelle that = (SerieTemporelle) o;
        return Objects.equals(id, that.id) &&
                titre.equals(that.titre) &&
                description.equals(that.description) &&
                Objects.equals(owner, that.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
