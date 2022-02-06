package ru.simplex_software.arbat_baza.xml.yandex.jaxb.adapters;

import ru.simplex_software.arbat_baza.xml.yandex.jaxb.CommercialType;

/**
 * Created by dima on 12.07.16.
 */
public class CommercialTypeAdapter extends SpaceLowCaseEnumAdapter {
    public CommercialTypeAdapter() {
        this.enumClass=CommercialType.class;
    }
}
