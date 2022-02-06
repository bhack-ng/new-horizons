package ru.simplex_software.arbat_baza.model.privateHouses;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplex_software.arbat_baza.model.AbstractBuilding;
import ru.simplex_software.arbat_baza.model.AbstractOptions;
import ru.simplex_software.arbat_baza.model.DirectionRoad;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.arbat_baza.model.live.OwnerStatus;
import ru.simplex_software.arbat_baza.model.stead.SteadArea;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * .
 */
@MappedSuperclass
public class PrivateHouse extends RealtyObject {

    private static final Logger LOG = LoggerFactory.getLogger(PrivateHouseRent.class);

    @Enumerated(value = EnumType.STRING)
    WallsType wallsType;

    @ManyToOne
    private DirectionRoad directionRoad;

    @Enumerated(value = EnumType.STRING)
    private OwnerStatus propertyRight = OwnerStatus.NATURAL_PERON;

    private Integer distanceToCity;


    public PrivateHouse() {
        setArea(new SteadArea());
        setBuilding(new AbstractBuilding());
        setOptions(new AbstractOptions());

    }

    public Integer getDistanceToCity() {
        return distanceToCity;
    }

    public void setDistanceToCity(Integer distanceToCity) {
        this.distanceToCity = distanceToCity;
    }

    public OwnerStatus getPropertyRight() {
        return propertyRight;
    }

    public void setPropertyRight(OwnerStatus propertyRight) {
        this.propertyRight = propertyRight;
    }

    public DirectionRoad getDirectionRoad() {
        return directionRoad;
    }

    public void setDirectionRoad(DirectionRoad directionRoad) {
        this.directionRoad = directionRoad;
    }

    public WallsType getWallsType() {
        return wallsType;
    }

    public void setWallsType(WallsType wallsType) {
        this.wallsType = wallsType;
    }
}
