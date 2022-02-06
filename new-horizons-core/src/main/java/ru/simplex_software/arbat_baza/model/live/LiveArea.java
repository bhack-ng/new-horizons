package ru.simplex_software.arbat_baza.model.live;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplex_software.arbat_baza.AbstractArea;

import javax.persistence.Entity;

/**
 * Площадь в кв. метрах.
 */
@Entity
public class LiveArea extends AbstractArea {
    private static final Logger LOG = LoggerFactory.getLogger(LiveArea.class);
    /**кухня.*/
    private Double kitchen;
    /**жилая.*/
    private Double living;

    /**Число спален.*/
    private Integer bedroomCount;

    /**Число совмещённых санузлов.*/
    private Integer fullBathroomCount;
    /**Число раздельных санузлов.*/
    private Integer splitBathroomCount;

    /**Число санузлов.*/
    private Integer wetUnitCount;
    /**Число пассажирских лифтов .*/
    private Integer lift_p;

    /**число грузовых лифтов .*/
    private Integer lift_g;
    /**Число гардеробных.*/
    private Integer cloakroomCount;

    /**Число балконов
     * значение -1 означает , что в фиде написано, что балконы есть
     * но не указано колличество балконов.
     * */
    private Integer balconyCount;

    /**Число лоджий.
     * может принимать значение -1 в случае , если в фиде указано
     * что балконы имеются (значение "yes")
     * но не указано их колличество.
     */
    private Integer recessedBalconyCount;

    public Double getKitchen() {
        return kitchen;
    }

    public void setKitchen(Double kitchen) {
        this.kitchen = kitchen;
    }

    public Double getLiving() {
        return living;
    }

    public void setLiving(Double living) {
        this.living = living;
    }

    public Integer getBedroomCount() {
        return bedroomCount;
    }

    public void setBedroomCount(Integer bedroomCount) {
        this.bedroomCount = bedroomCount;
    }

    public Integer getWetUnitCount() {
        return wetUnitCount;
    }

    public void setWetUnitCount(Integer wetUnitCount) {
        this.wetUnitCount = wetUnitCount;
    }

    public Integer getFullBathroomCount() {
        return fullBathroomCount;
    }

    public void setFullBathroomCount(Integer fullBathroomCount) {
        this.fullBathroomCount = fullBathroomCount;
    }

    public Integer getSplitBathroomCount() {
        return splitBathroomCount;
    }

    public void setSplitBathroomCount(Integer splitBathroomCount) {
        this.splitBathroomCount = splitBathroomCount;
    }

    public Integer getCloakroomCount() {
        return cloakroomCount;
    }

    public void setCloakroomCount(Integer cloakroomCount) {
        this.cloakroomCount = cloakroomCount;
    }

    public Integer getBalconyCount() {
        return balconyCount;
    }

    public void setBalconyCount(Integer balconyCount) {
        this.balconyCount = balconyCount;
    }

    public Integer getRecessedBalconyCount() {
        return recessedBalconyCount;
    }

    public void setRecessedBalconyCount(Integer recessedBalconyCount) {
        this.recessedBalconyCount = recessedBalconyCount;
    }

    public Integer getLift_p() {
        return lift_p;
    }

    public void setLift_p(Integer lift_p) {
        this.lift_p = lift_p;
    }

    public Integer getLift_g() {
        return lift_g;
    }

    public void setLift_g(Integer lift_g) {
        this.lift_g = lift_g;
    }
}
