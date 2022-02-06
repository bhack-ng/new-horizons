package ru.simplex_software.arbat_baza.model.stead;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplex_software.arbat_baza.model.price.LiveRentPrice;

import javax.persistence.Entity;

/**
 * помнить , что при экспорте тегу PropertyRight
 * присвоить значение "Посредник"
 */
@Entity
public class SteadRent extends Stead {

    private static final Logger LOG = LoggerFactory.getLogger(SteadRent.class);

    public SteadRent() {
        super();
        setPrice(new LiveRentPrice());
    }
}
