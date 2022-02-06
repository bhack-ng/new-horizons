package ru.simplex_software.arbat_baza.xml.yandex.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * Категория объекта недвижимости. Возможные значения в xml приведены в аннотациях XmlEnumValue.
 */
@XmlType
@XmlEnum(String.class)
public enum Category {
    @Multi({@XmlEnumValue("комната"),@XmlEnumValue("room")}) ROOM(),
    @Multi({@XmlEnumValue("квартира"),@XmlEnumValue("flat")}) FLAT(),
    @Multi({@XmlEnumValue("таунхаус"),@XmlEnumValue("townhouse")})TOWNHOUSE(),
    @Multi({@XmlEnumValue("дом"),@XmlEnumValue("house")})HOUSE(),
        @XmlEnumValue("часть дома")HOUSE_PART(),
    @Multi({@XmlEnumValue("участок"),@XmlEnumValue("lot")})LOT(),
        @XmlEnumValue("земельный участок")AREA_PART(),
    @Multi({@XmlEnumValue("дом с участком"),@XmlEnumValue("house with lot")})HOUSE_WITH_LOT(),
    @Multi({@XmlEnumValue("дача"),@XmlEnumValue("cottage")})COTTAGE(),
    @Multi({@XmlEnumValue("коммерческая"),@XmlEnumValue("commercial")})COMMERICAL()


}
