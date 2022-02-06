package ru.simplex_software.arbat_baza.xml.yandex.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * Валюта.
 */
@XmlType
@XmlEnum(String.class)
public enum Currency {//валюта (USD, RUB, EUR),
    @XmlEnumValue("USD") USD,
    @Multi({@XmlEnumValue("RUB"), @XmlEnumValue("RUR")})RUB,
    @XmlEnumValue("EUR")EUR
}
