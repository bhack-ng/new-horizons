package ru.simplex_software.arbat_baza.model.price;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;

/**
 * .
 */
@Entity
public class RentPrice extends SimplePrice {
    private static final Logger LOG = LoggerFactory.getLogger(RentPrice.class);
}
