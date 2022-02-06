package ru.simplex_software.arbat_baza.viewmodel.price;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplex_software.arbat_baza.model.price.SimplePrice;

/**
 * Выводит в строку число и валюту .
 */

public class SimplePriceRenderer extends PriceRenderer{
    private static final Logger LOG = LoggerFactory.getLogger(SimplePriceRenderer.class);

    @Override
    public String toLocalizedString(SimplePrice price) {
        if(price.getValue()==null){
            return "-";
        }
        StringBuffer sb = new StringBuffer();
        sb.append(price.getValue());
        sb.append(" ");
        sb.append(price.getCurrency());
        return sb.toString();
    }
}
