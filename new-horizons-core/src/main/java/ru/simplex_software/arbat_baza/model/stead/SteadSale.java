package ru.simplex_software.arbat_baza.model.stead;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplex_software.arbat_baza.model.price.SimplePrice;

import javax.persistence.Entity;

@Entity
public class SteadSale extends Stead {

    private static final Logger LOG = LoggerFactory.getLogger(SteadSale.class);

    public SteadSale() {
        super();
        setPrice(new SimplePrice());
    }
}
