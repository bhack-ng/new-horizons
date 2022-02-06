package ru.simplex_software.arbat_baza.model.live;

import ru.simplex_software.zkutils.entity.LongIdPersistentEntity;

import javax.persistence.Entity;

/**
 * Name of Building. Input field "Тип дома".
 */
@Entity
public class NameOfBuilding extends LongIdPersistentEntity{
    private String buildingName;

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }
}
