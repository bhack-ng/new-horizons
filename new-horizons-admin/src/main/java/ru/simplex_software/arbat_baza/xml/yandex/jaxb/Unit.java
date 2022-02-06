package ru.simplex_software.arbat_baza.xml.yandex.jaxb;

import javax.xml.bind.annotation.XmlEnumValue;

/**
 * Единица площади помещения или участка. Элемент необходим, если цена указана за единицу площади.
 */
public enum Unit {
    @Multi({@XmlEnumValue("кв. м") ,@XmlEnumValue("sq. m")}) M2,
    @XmlEnumValue("cотка") M100,
    @Multi({@XmlEnumValue("гектар"),@XmlEnumValue("hectare") })HECTARE,

}
