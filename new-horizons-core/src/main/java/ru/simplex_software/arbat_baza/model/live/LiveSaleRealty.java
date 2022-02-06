package ru.simplex_software.arbat_baza.model.live;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.arbat_baza.model.price.LiveRentPrice;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * Продажа жилья.
 */
@Entity
public class LiveSaleRealty extends RealtyObject {
    private static final Logger LOG = LoggerFactory.getLogger(LiveSaleRealty.class);


    @Enumerated(value = EnumType.STRING)
    private BuildingTypeSale buildingTypeSale = BuildingTypeSale.SECONDHAND ;

    @Enumerated(value = EnumType.STRING)
    private  SaleType saleType=SaleType.FREE;

    public LiveSaleRealty() {
        setOptions(new LiveSaleOptions());

        setArea(new LiveArea());
        setPrice(new LiveRentPrice());
        setBuilding(new DwellingHouse());
    }

    public BuildingTypeSale getBuildingTypeSale() {
        return buildingTypeSale;
    }

    public void setBuildingTypeSale(BuildingTypeSale buildingTypeSale) {
        this.buildingTypeSale = buildingTypeSale;
    }


    public SaleType getSaleType() {
        return saleType;
    }

    public void setSaleType(SaleType saleType) {
        this.saleType = saleType;
    }
}
