package com.amboucheba.etudeDeCasWeb.Models.Entities;


import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "collections")
public class Collection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tag")
    @NotBlank(message = "Field 'tag' is required")
    @Size( min = 0, max = 50, message = "Tag length must be between 1 and 50")
    private String tag;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToMany
    @JoinTable(
        name = "collection_locations",
        joinColumns = @JoinColumn(name = "collection_id"),
        inverseJoinColumns = @JoinColumn(name = "location_id") )
    @JsonManagedReference
    List<Location> locations;

    public Collection(@NotBlank(message = "Field 'tag' is required") @Size(min = 0, max = 50, message = "Tag length must be between 1 and 50") String tag, User owner, List<Location> locations) {
        this.tag = tag;
        this.owner = owner;
        this.locations = locations;
    }

    public Collection(@NotBlank(message = "Field 'tag' is required") @Size(min = 0, max = 50, message = "Tag length must be between 1 and 50") String tag, User owner) {
        this.tag = tag;
        this.owner = owner;
        this.locations = new ArrayList<>();
    }

    public Collection() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Collection that = (Collection) o;
        return Objects.equals(id, that.id) &&
                tag.equals(that.tag) &&
                owner.equals(that.owner) &&
                Objects.equals(locations, that.locations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tag, owner, locations);
    }
}

