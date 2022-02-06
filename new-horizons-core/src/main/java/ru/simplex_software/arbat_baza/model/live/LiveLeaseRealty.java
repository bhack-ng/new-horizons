package ru.simplex_software.arbat_baza.model.live;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplex_software.arbat_baza.model.Competitors;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.arbat_baza.model.price.LiveRentPrice;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;

/**
 * .
 */

@Entity
public class LiveLeaseRealty extends RealtyObject {
    private static final Logger LOG = LoggerFactory.getLogger(LiveLeaseRealty.class);

    public LiveLeaseRealty() {
        setArea(new LiveArea());
        setPrice(new LiveRentPrice());
        setOptions(new LiveLeaseOptions());
        setBuilding(new DwellingHouse());
    }

    @OneToOne(cascade = CascadeType.ALL)
    private NameOfBuilding nameOfBuilding;

    @OneToOne(cascade = CascadeType.ALL)
    private Competitors competitors = new Competitors();

    @Enumerated(value = EnumType.STRING)
    private OwnerStatus ownerStatus=OwnerStatus.NATURAL_PERON;




    public Competitors getCompetitors() {
        return competitors;
    }

    public void setCompetitors(Competitors competitors) {
        this.competitors = competitors;
    }

    public OwnerStatus getOwnerStatus() {
        return ownerStatus;
    }

    public void setOwnerStatus(OwnerStatus ownerStatus) {
        this.ownerStatus = ownerStatus;
    }

    public NameOfBuilding getNameOfBuilding() {
        return nameOfBuilding;
    }

    public void setNameOfBuilding(NameOfBuilding nameOfBuilding) {
        this.nameOfBuilding = nameOfBuilding;
    }
}
