package ru.simplex_software.arbat_baza.model.privateHouses;

import ru.simplex_software.arbat_baza.model.price.SimplePrice;

import javax.persistence.Entity;

/**
 * .
 */
@Entity
public class PrivateHouseSale extends PrivateHouse {
    public PrivateHouseSale() {
        super();
        setPrice(new SimplePrice());
    }

}