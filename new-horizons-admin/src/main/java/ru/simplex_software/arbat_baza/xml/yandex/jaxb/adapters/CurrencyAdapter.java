package ru.simplex_software.arbat_baza.xml.yandex.jaxb.adapters;

import ru.simplex_software.arbat_baza.xml.yandex.jaxb.Currency;

/**
 * Created by dima on 13.07.16.
 */
public class CurrencyAdapter extends MultiNameEnumAdapter {
    public CurrencyAdapter() {
        this.enumClass=Currency.class;
    }
}
