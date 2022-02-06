package ru.simplex_software.arbat_baza.model.live;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplex_software.arbat_baza.model.AbstractBuilding;
import ru.simplex_software.arbat_baza.model.BuildingType;

import javax.persistence.Entity;

/**
 * Здание (Building) в котором расположена жилая квартира.
 */
@Entity
public class DwellingHouse extends AbstractBuilding {
    private static final Logger LOG = LoggerFactory.getLogger(DwellingHouse.class);

    private BuildingType buildingType;
    private String seria;
    private String buildingName;


    public BuildingType getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(BuildingType buildingType) {
        this.buildingType = buildingType;
    }

    public String getSeria() {
        return seria;
    }

    public void setSeria(String seria) {
        this.seria = seria;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }
}
