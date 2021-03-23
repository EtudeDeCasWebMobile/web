package com.amboucheba.etudeDeCasWeb.Models.Inputs;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

public class LocationInput {



    @Column(name = "title")
    @NotBlank(message = "Field 'title' is required")
    @Size( min = 0, max = 255, message = "title length must be between 0 and 255")
    private String title;

    @Column(name = "description")
    @Size( min = 0, max = 255, message = "description length must be between 0 and 255")
    private String description;

    public LocationInput(@NotBlank(message = "Field 'title' is required")  @Size( min = 0, max = 255, message = "title length must be between 6 and 255")String title,@Size( min = 0, max = 255, message = "description length must be between 6 and 255") String description) {
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


    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        LocationInput that = (LocationInput) object;
        return java.util.Objects.equals(title, that.title) &&
                java.util.Objects.equals(description, that.description);
    }

    public int hashCode() {
        return Objects.hash(super.hashCode(), title, description);
    }
}