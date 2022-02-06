package ru.simplex_software.arbat_baza.xml.yandex.jaxb.adapters;

import ru.simplex_software.arbat_baza.xml.yandex.jaxb.Period;

/**
 * Created by dima on 13.07.16.
 */
public class PeriodAdapter extends MultiNameEnumAdapter <Period>{
    public PeriodAdapter() {
        this.enumClass=Period.class;
    }
}
