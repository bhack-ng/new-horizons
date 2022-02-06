package ru.simplex_software.arbat_baza.xml.yandex.jaxb;

import javax.xml.bind.annotation.XmlEnumValue;

/**
 * Назначение склада. Элемент может повторяться несколько раз.
 */
public enum PurposeWarehouse {
    /**алкогольный склад.*/
    @XmlEnumValue("alcohol")ALCAHOL,
    /**фармацевтический склад.*/
    @XmlEnumValue("pharmaceutical storehouse")PHARMACEUTICAL_STOREHAUSE,
    /**овощехранилище.*/
    @XmlEnumValue("vegetable storehouse")VEGETABLE_STOREHAUSE

}
