package ru.simplex_software.arbat_baza.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplex_software.zkutils.entity.LongIdPersistentEntity;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;


/**
 * Суперкласс Зданий.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class AbstractBuilding extends LongIdPersistentEntity{
    private static final Logger LOG = LoggerFactory.getLogger(AbstractBuilding.class);
    private Integer floor;//этаж (если подвал, то «-2», полуподвал - «-1»)
    private Integer floorTotal;//	количество этажей в здании

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Integer getFloorTotal() {
        return floorTotal;
    }

    public void setFloorTotal(Integer floorTotal) {
        this.floorTotal = floorTotal;
    }
}
