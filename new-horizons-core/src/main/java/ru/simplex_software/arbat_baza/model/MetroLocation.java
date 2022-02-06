package ru.simplex_software.arbat_baza.model;

import ru.simplex_software.zkutils.entity.LongIdPersistentEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Расположение рядом со станцией метро..
 */
@Entity
public class MetroLocation extends LongIdPersistentEntity{
    @ManyToOne
    MetroStation metroStation;


    /**расстояние до метро не важно чем добираться.*/
    private Integer anyTime;

    /**расстояние в минутах до метро пешком ?*/
    private boolean wtimeSelected;



    public MetroStation getMetroStation() {
        return metroStation;
    }

    public void setMetroStation(MetroStation metroStation) {
        this.metroStation = metroStation;
    }

    public Integer getAnyTime() {
        return anyTime;
    }

    public void setAnyTime(Integer anyTime) {
        this.anyTime = anyTime;
    }

    public boolean isWtimeSelected() {
        return wtimeSelected;
    }

    public void setWtimeSelected(boolean wtimeSelected) {
        this.wtimeSelected = wtimeSelected;
    }
}
