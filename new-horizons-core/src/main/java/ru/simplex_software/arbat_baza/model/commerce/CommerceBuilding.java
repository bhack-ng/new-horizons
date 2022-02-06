package ru.simplex_software.arbat_baza.model.commerce;

import ru.simplex_software.arbat_baza.model.AbstractBuilding;
import ru.simplex_software.arbat_baza.model.BuildingClass;
import ru.simplex_software.arbat_baza.model.BuildingEntrance;
import ru.simplex_software.arbat_baza.model.BuildingLiveType;
import ru.simplex_software.arbat_baza.model.BuildingStatus;
import ru.simplex_software.arbat_baza.model.BuildingType;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * Информация о коммерческом здании.
 */
@Entity
public class CommerceBuilding extends AbstractBuilding {


    private BuildingLiveType liveType;


    private BuildingEntrance enter;
    /**
     * Название.
     */
    private String name_bc;

    private BuildingClass buildingClass;

    private BuildingStatus status_b;

    /**
     * площадь здания
     */
    private Integer area_b;


    /**
     * тип дома.
     */
    @Enumerated(EnumType.STRING)
    private BuildingType buildingType;
    /**
     * высота потолков.
     */
    private String ceiling;

    public BuildingType getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(BuildingType buildingType) {
        this.buildingType = buildingType;
    }

    public String getCeiling() {
        return ceiling;
    }

    public void setCeiling(String ceiling) {
        this.ceiling = ceiling;
    }

    public String getName_bc() {
        return name_bc;
    }

    public void setName_bc(String name_bc) {
        this.name_bc = name_bc;
    }

    public BuildingStatus getStatus_b() {
        return status_b;
    }

    public void setStatus_b(BuildingStatus status_b) {
        this.status_b = status_b;
    }

    public Integer getArea_b() {
        return area_b;
    }

    public void setArea_b(Integer area_b) {
        this.area_b = area_b;
    }

    public BuildingLiveType getLiveType() {
        return liveType;
    }

    public void setLiveType(BuildingLiveType liveType) {
        this.liveType = liveType;
    }

    public BuildingEntrance getEnter() {
        return enter;
    }

    public void setEnter(BuildingEntrance enter) {
        this.enter = enter;
    }

    public BuildingClass getBuildingClass() {
        return buildingClass;
    }

    public void setBuildingClass(BuildingClass buildingClass) {
        this.buildingClass = buildingClass;
    }
}
