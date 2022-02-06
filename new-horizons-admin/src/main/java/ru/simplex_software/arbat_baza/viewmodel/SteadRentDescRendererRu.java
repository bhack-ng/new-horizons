package ru.simplex_software.arbat_baza.viewmodel;

import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.arbat_baza.model.stead.SteadArea;
import ru.simplex_software.arbat_baza.model.stead.SteadRent;
import ru.simplex_software.arbat_baza.model.stead.SteadSale;

/**
 * .
 */
public class SteadRentDescRendererRu extends FlatDescriptionRenderer {

    public String toLocolizedString(RealtyObject realtyObject) {
        SteadRent st=(SteadRent)realtyObject;
        return  " Площадь: площадь участка:"+nullChk(((SteadArea) st.getArea()).getLendArea());

    }
}
