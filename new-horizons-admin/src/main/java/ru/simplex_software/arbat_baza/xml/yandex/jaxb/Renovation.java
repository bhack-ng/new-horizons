package ru.simplex_software.arbat_baza.xml.yandex.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * Ремонт.
 */
@XmlType
@XmlEnum(String.class)
public enum Renovation {
    @XmlEnumValue("евро") EURO,
    @XmlEnumValue("дизайнерский")дизайнерский,
    @XmlEnumValue("частичный ремонт") частичный_ремонт,
    @XmlEnumValue("хороший") хороший,
    @XmlEnumValue("с отделкой")с_отделкой,
    @XmlEnumValue("черновая отделка")черновая_отделка,
    @XmlEnumValue("требует ремонта")требует_ремонта
}
