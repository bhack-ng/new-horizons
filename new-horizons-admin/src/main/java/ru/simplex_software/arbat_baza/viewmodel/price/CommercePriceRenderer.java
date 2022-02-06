package ru.simplex_software.arbat_baza.viewmodel.price;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.Converter;
import org.zkoss.bind.impl.BindContextImpl;
import ru.simplex_software.arbat_baza.EnumConverter;
import ru.simplex_software.arbat_baza.model.price.CommercePrice;
import ru.simplex_software.arbat_baza.model.price.Period;
import ru.simplex_software.arbat_baza.model.price.SimplePrice;

import java.util.HashMap;

/**
 * .
 */

public class CommercePriceRenderer extends PriceRenderer {
    private static final Logger LOG = LoggerFactory.getLogger(CommercePriceRenderer.class);
    private Converter enumFromPriceConverter=new EnumConverter("ru.simplex_software.arbat_baza.model.price");

    public CommercePriceRenderer() {
        super();
    }

    public String toLocalizedString(SimplePrice price) {
        if(price.getValue()==null){
            return "-";
        }
        CommercePrice cPrice=(CommercePrice)price;
        StringBuffer sb = new StringBuffer();
        sb.append(cPrice.getValue());
        sb.append(" ");
        sb.append(cPrice.getCurrency());
        sb.append(" ");
        BindContextImpl ctx = new BindContextImpl(null,null,true,null,null,null);
        HashMap map = new HashMap();
        map.put("enumClass", Period.class.getSimpleName());
        ctx.setAttribute(BindContextImpl.CONVERTER_ARGS , map);
        String  peridoStr = (String )enumFromPriceConverter.coerceToUi(cPrice.getPeriod(), null, ctx);
        sb.append(peridoStr.toLowerCase());
        return sb.toString();
    }
}
