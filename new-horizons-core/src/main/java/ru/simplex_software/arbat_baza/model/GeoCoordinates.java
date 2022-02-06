package ru.simplex_software.arbat_baza.model;

import ru.simplex_software.zkutils.entity.LongIdPersistentEntity;

import javax.persistence.Entity;

/**
 * .
 */
@Entity
public class GeoCoordinates extends LongIdPersistentEntity{
    public GeoCoordinates() {
    }

    public GeoCoordinates(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Широта.
     */
    private Double latitude;
    /**
     * Долгота.
     */
    private Double longitude;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "latitude=" + latitude + ", longitude=" + longitude + "";
    }
}
