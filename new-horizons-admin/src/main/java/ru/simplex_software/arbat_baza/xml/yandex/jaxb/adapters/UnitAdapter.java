package ru.simplex_software.arbat_baza.xml.yandex.jaxb.adapters;

import ru.simplex_software.arbat_baza.xml.yandex.jaxb.Unit;

/**
 * Created by dima on 13.07.16.
 */
public class UnitAdapter extends MultiNameEnumAdapter <Unit>{
    public UnitAdapter() {
        this.enumClass=Unit.class;
    }
}
