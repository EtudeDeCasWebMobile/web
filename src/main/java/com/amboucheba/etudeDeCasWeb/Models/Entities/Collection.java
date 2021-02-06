package com.amboucheba.etudeDeCasWeb.Models.Entities;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "collections")
public class Collection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tag", unique = true)
    @NotBlank(message = "Field 'tag' is required")
    @Size( min = 0, max = 50, message = "Tag length must be between 1 and 50")
    private String tag;

    @ManyToMany
    @JoinTable(
        name = "collection_locations",
        joinColumns = @JoinColumn(name = "collection_id"),
        inverseJoinColumns = @JoinColumn(name = "location_id"))
    List<Location> locations;

    public Collection(Long id, @NotBlank(message = "Field 'tag' is required") @Size(min = 0, max = 50, message = "Tag length must be between 1 and 50") String tag) {
        this.id = id;
        this.tag = tag;
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
        return id.equals(that.id) &&
                tag.equals(that.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tag);
    }
}

