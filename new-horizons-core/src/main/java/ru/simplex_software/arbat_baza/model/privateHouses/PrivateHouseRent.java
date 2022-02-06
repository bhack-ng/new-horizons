package ru.simplex_software.arbat_baza.model.privateHouses;

import ru.simplex_software.arbat_baza.model.price.LiveRentPrice;

import javax.persistence.Entity;


/**
 * .
 */
@Entity
public class PrivateHouseRent extends PrivateHouse {
    public PrivateHouseRent(){
        super();
        setPrice(new LiveRentPrice());
    }

}
