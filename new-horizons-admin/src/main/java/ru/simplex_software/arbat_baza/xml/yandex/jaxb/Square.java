package ru.simplex_software.arbat_baza.xml.yandex.jaxb;

import ru.simplex_software.arbat_baza.xml.yandex.jaxb.adapters.UnitAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Created by dima on 13.07.16.
 */
@XmlType @XmlAccessorType(XmlAccessType.FIELD)
public class Square {
    @XmlElement
    Float value;
    @XmlElement @XmlJavaTypeAdapter(UnitAdapter.class)
    Unit unit;

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }
}
