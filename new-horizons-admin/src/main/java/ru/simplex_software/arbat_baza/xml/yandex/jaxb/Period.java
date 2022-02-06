package ru.simplex_software.arbat_baza.xml.yandex.jaxb;

import javax.xml.bind.annotation.XmlEnumValue;

/**
 * Длительность аренды.
 */
public enum Period {

    @Multi({@XmlEnumValue("день"),@XmlEnumValue("day")})DAY,
    @Multi({@XmlEnumValue("месяц"),@XmlEnumValue("month")})MONTH
}
