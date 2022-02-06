package ru.simplex_software.arbat_baza.model.price;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplex_software.zkutils.entity.LongIdPersistentEntity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * .
 */
@Entity @Inheritance(strategy = InheritanceType.JOINED)
public class SimplePrice extends LongIdPersistentEntity{
    private static final Logger LOG = LoggerFactory.getLogger(SimplePrice.class);
    private Integer value;
    @Enumerated(EnumType.STRING)
    private Currency currency= Currency.RUB;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        if(value==null){
            return "-";
        }
        return ""+value+" "+currency.name();
    }
}
