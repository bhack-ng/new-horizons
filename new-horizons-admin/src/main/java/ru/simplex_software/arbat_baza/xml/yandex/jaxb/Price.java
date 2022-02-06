package ru.simplex_software.arbat_baza.xml.yandex.jaxb;

import ru.simplex_software.arbat_baza.xml.yandex.jaxb.adapters.CurrencyAdapter;
import ru.simplex_software.arbat_baza.xml.yandex.jaxb.adapters.PeriodAdapter;
import ru.simplex_software.arbat_baza.xml.yandex.jaxb.adapters.UnitAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Информация об условиях сделки.
 * https://yandex.ru/support/webmaster/realty/requirements.xml#concept6
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Price {
    @XmlElement
    private Double value;
    @XmlElement
    @XmlJavaTypeAdapter(CurrencyAdapter.class)
    private Currency  currency;
    @XmlElement @XmlJavaTypeAdapter(UnitAdapter.class)
    private Unit unit;
    @XmlElement @XmlJavaTypeAdapter(PeriodAdapter.class)
    private Period period;

    @XmlElement(name = "taxation-form")
    private TaxationForm taxationForm;

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public TaxationForm getTaxationForm() {
        return taxationForm;
    }

    public void setTaxationForm(TaxationForm taxationForm) {
        this.taxationForm = taxationForm;
    }
}
