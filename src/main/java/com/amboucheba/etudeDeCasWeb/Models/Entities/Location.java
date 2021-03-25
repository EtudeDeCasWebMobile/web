package com.amboucheba.etudeDeCasWeb.Models.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "locations")
public class Location {

    // Long to UUID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    //@NotBlank(message = "Field 'title' is required")
    @Size( min = 0, max = 255, message = "Username length must be between 6 and 255")
    private String title;

    @Column(name = "description")
    @Size( min = 0, max = 255, message = "Username length must be between 6 and 255")
    private String description;
    
    @Column(name = "latitude")
    private String latitude;
    
    @Column(name = "longitude")
    private String longitude;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @ManyToMany(mappedBy = "locations")
    @JsonBackReference
    List<Collection> collections;

    public Location(Long id,  @Size(min = 0, max = 255, message = "Username length must be between 6 and 255") String title, @Size(min = 0, max = 255, message = "Username length must be between 6 and 255") String description, User owner, String latitude, String Longitude) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.owner = owner;
        this.latitude = latitude;
        this.longitude = Longitude;
    }

    public Location(@Size(min = 0, max = 255, message = "Username length must be between 6 and 255") String title, @Size(min = 0, max = 255, message = "Username length must be between 6 and 255") String description, String latitude, String longitude, User owner, List<Collection> collections) {
        this.title = title;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.owner = owner;
        this.collections = collections;
    }

    public Location(String latitude, String longitude, User owner) {
        this.latitude = latitude;
        this.longitude = longitude;
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
    

    public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
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
        return id.equals(location.id) &&
                owner.equals(location.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, owner);
    }
}
