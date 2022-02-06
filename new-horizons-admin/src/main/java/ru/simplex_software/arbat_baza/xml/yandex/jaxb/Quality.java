package ru.simplex_software.arbat_baza.xml.yandex.jaxb;

import javax.xml.bind.annotation.XmlEnumValue;

/**
 * Состояние объекта.
 * Рекомендуемые значения: «хорошее», «отличное», «нормальное», «плохое».
 */
public enum Quality {
    @XmlEnumValue("хорошее") GOOD,
    @XmlEnumValue("отличное")BEST,
    @XmlEnumValue("нормальное")NORMAL,
    @XmlEnumValue("плохое")BAD
}
