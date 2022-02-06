package ru.simplex_software.arbat_baza.xml.yandex.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 *  Тип сделки: «продажа», «аренда».
 */
@XmlType
@XmlEnum(String.class)
public enum Type {
    продажа, аренда;
}
