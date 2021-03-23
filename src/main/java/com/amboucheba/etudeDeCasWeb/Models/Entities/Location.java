package com.amboucheba.etudeDeCasWeb.Models.Entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "locations")
public class Location {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    @NotBlank(message = "Field 'title' is required")
    @Size( min = 0, max = 255, message = "title length must be between 6 and 255")
    private String title;

    @Column(name = "description")
    @Size( min = 0, max = 255, message = "description length must be between 6 and 255")
    private String description;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @ManyToMany(mappedBy = "locations")
    List<Collection> collections;

    public Location(Long id, @NotBlank(message = "Field 'title' is required") @Size(min = 0, max = 255, message = "Username length must be between 6 and 255") String title, @Size(min = 0, max = 255, message = "Username length must be between 6 and 255") String description, User owner) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.owner = owner;
    }

    public Location(@NotBlank(message = "Field 'title' is required") @Size(min = 0, max = 255, message = "title length must be between 6 and 255") String title, @Size(min = 0, max = 255, message = "description length must be between 6 and 255") String description, User owner) {
        this.title = title;
        this.description = description;
        this.owner = owner;
    }

    public Location() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<Collection> getCollections() {
        return collections;
    }

    public void setCollections(List<Collection> collections) {
        this.collections = collections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(id, location.id) &&
                Objects.equals(title, location.title) &&
                Objects.equals(description, location.description) &&
                Objects.equals(owner, location.owner) &&
                Objects.equals(collections, location.collections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, owner);
    }
}
