package ru.simplex_software.arbat_baza.xml.yandex.jaxb;

import javax.xml.bind.annotation.XmlEnumValue;

/**
 * Created by dima on 12.07.16.
 */
public enum Purpose {
    /**помещение для банк.*/
    @XmlEnumValue("bank") BANK,
    /**салон красоты.*/
    @XmlEnumValue("beauty shop") BEAUTY_SHOP,
    /** продуктовый магазин.*/
    @XmlEnumValue("food store") FOOD_STORE,
    /**медицинский центр.*/
    @XmlEnumValue("medical center") MEDICAL_CENTER,
    /**турагентство.*/
    @XmlEnumValue("touragency") TOURAGENCY,
    /**шоу-рум.*/
    @XmlEnumValue("show room") SHOW_ROOM
}
