package com.amboucheba.etudeDeCasWeb.Models.Outputs;
import com.amboucheba.etudeDeCasWeb.Models.Entities.Collection;
import com.amboucheba.etudeDeCasWeb.Models.Entities.Location;

import java.util.List;
import java.util.Objects;
public class Locations {
    private List<Location> locations;

    public Locations(List<Location> locations) {
        this.locations = locations;
    }

    public Locations() {
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public List<Location> getLocations() {
        return locations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Locations locations1 = (Locations) o;
        return Objects.equals(locations, locations1.locations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locations);
    }
}
