package ru.simplex_software.arbat_baza.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplex_software.zkutils.entity.LongIdPersistentEntity;

import javax.persistence.Entity;

/**
 * .
 */
@Entity
public class DirectionRoad extends LongIdPersistentEntity{
    private static final Logger LOG = LoggerFactory.getLogger(DirectionRoad.class);

    private String cityName;
    private String regionName;
    private String roadName;
    private int avitoId;

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    public int getAvitoId() {
        return avitoId;
    }

    public void setAvitoId(int avitoId) {
        this.avitoId = avitoId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
