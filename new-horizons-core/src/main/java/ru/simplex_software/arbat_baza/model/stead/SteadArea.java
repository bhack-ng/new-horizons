package ru.simplex_software.arbat_baza.model.stead;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplex_software.arbat_baza.AbstractArea;

import javax.persistence.Entity;

/**
 * .
 */
@Entity
public class SteadArea extends AbstractArea {
    private static final Logger LOG = LoggerFactory.getLogger(SteadArea.class);
    /**Площадь участка.*/
    private Double lendArea;

    public Double getLendArea() {
        return lendArea;
    }

    public void setLendArea(Double lendArea) {
        this.lendArea = lendArea;
    }


}
