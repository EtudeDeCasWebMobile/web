package com.amboucheba.etudeDeCasWeb.Models.Inputs;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

public class LocationInput {

    @NotBlank(message = "Field 'title' is required")
    @Size( min = 0, max = 255, message = "title length must be between 0 and 255")
    private String title;

    @NotBlank(message = "Field 'description' is required")
    @Size( min = 0, max = 255, message = "description length must be between 0 and 255")
    private String description;

    private String latitude;
    private String longitude;

    private List<String> tags;

    public LocationInput(@NotBlank(message = "Field 'title' is required") @Size(min = 0, max = 255, message = "title length must be between 0 and 255") String title, @Size(min = 0, max = 255, message = "description length must be between 0 and 255") String description, String latitude, String longitude, List<String> tags) {
        this.title = title;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.tags = tags;
    }

    public LocationInput(@NotBlank(message = "Field 'title' is required")  @Size( min = 0, max = 255, message = "title length must be between 6 and 255")String title, @Size( min = 0, max = 255, message = "description length must be between 6 and 255") String description) {
        this.title = title;
        this.description = description;
    }

    public LocationInput() {
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationInput that = (LocationInput) o;
        return title.equals(that.title) &&
                description.equals(that.description) &&
                latitude.equals(that.latitude) &&
                longitude.equals(that.longitude) &&
                Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, latitude, longitude, tags);
    }
}
