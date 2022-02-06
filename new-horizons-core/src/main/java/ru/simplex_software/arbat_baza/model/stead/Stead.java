package ru.simplex_software.arbat_baza.model.stead;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplex_software.arbat_baza.model.AbstractBuilding;
import ru.simplex_software.arbat_baza.model.AbstractOptions;
import ru.simplex_software.arbat_baza.model.DirectionRoad;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.arbat_baza.model.live.OwnerStatus;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class Stead extends RealtyObject {
    private static final Logger LOG = LoggerFactory.getLogger(Stead.class);
    @Enumerated(value = EnumType.STRING)
    ObjectType objectType;
    private Integer distanceToCity;
    @Enumerated(value = EnumType.STRING)
    private OwnerStatus propertyRight = OwnerStatus.NATURAL_PERON;
    @ManyToOne
    private DirectionRoad directionRoad;

    public Stead() {
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

    public ObjectType getObjectType() {
        return objectType;
    }

    public void setObjectType(ObjectType objectType) {
        this.objectType = objectType;
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
}
