package ru.simplex_software.arbat_baza.model.price;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * Цена аренды коммерческого помещения.
 */
@Entity
public class CommercePrice extends SimplePrice{

    @Enumerated(EnumType.STRING)
    private Period period= Period.MONTH;

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public String getFullPriceString(){
        return getValue() +" "+getCurrency() +" за "+period;
    }

    @Override
    public String toString() {
        return getFullPriceString();
    }
}
