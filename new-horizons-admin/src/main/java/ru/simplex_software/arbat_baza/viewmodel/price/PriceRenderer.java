package ru.simplex_software.arbat_baza.viewmodel.price;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplex_software.arbat_baza.model.price.CommercePrice;
import ru.simplex_software.arbat_baza.model.price.LiveRentPrice;
import ru.simplex_software.arbat_baza.model.price.SimplePrice;

/**
 * .
 */

public class PriceRenderer {
    private static final Logger LOG = LoggerFactory.getLogger(PriceRenderer.class);

    public PriceRenderer() {
    }

    public String toLocalizedString(SimplePrice price){
        return "abstract";
    }

    public CommercePriceRenderer getRenderer(CommercePrice price){
        return new CommercePriceRenderer();
    }
    public PriceRenderer         getRenderer(SimplePrice price){
        return new SimplePriceRenderer();
    }
    public PriceRenderer         getRenderer(LiveRentPrice price){
        return new SimplePriceRenderer();
    }

}
