package ru.simplex_software.arbat_baza.viewmodel;

import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.arbat_baza.model.live.LiveSaleRealty;
import ru.simplex_software.arbat_baza.model.stead.SteadArea;
import ru.simplex_software.arbat_baza.model.stead.SteadSale;

/**
 * .
 */
public class SteadSaleDescRendererRu extends FlatDescriptionRenderer{

    public String toLocolizedString(RealtyObject realtyObject) {
        SteadSale st=(SteadSale)realtyObject;
        return  " Площадь: площадь участка:"+nullChk(((SteadArea) st.getArea()).getLendArea());

    }
}
