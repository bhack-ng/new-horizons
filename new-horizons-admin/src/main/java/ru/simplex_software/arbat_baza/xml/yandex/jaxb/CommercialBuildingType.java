package ru.simplex_software.arbat_baza.xml.yandex.jaxb;

import javax.xml.bind.annotation.XmlEnumValue;

/**
 * Created by dima on 12.07.16.
 */
public enum CommercialBuildingType {
    /** (бизнес-центр).*/
    @XmlEnumValue("business center")BUSINESS_CENTER,
    @XmlEnumValue("detached building")DETACHED_BUILDING,
    /** встроенное помещение, жилой дом.*/
    @XmlEnumValue("residential building")RESIDENTIAL_BUILDING,
    /**торговый центр.*/
    @XmlEnumValue("shopping center")SHOPPING_CENTER,
    /**складской комплекс.*/
    @XmlEnumValue("warehouse")WAREHOUSE

}
