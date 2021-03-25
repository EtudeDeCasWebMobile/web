package com.amboucheba.etudeDeCasWeb.Models.Inputs;

import java.util.Objects;

public class PositionInput {


    private String latitude;
    private String longitude;

    public PositionInput(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public PositionInput() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PositionInput that = (PositionInput) o;
        return latitude.equals(that.latitude) &&
                longitude.equals(that.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }
}
