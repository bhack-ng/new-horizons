package ru.simplex_software.arbat_baza.viewmodel;

import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.arbat_baza.model.privateHouses.PrivateHouseRent;
import ru.simplex_software.arbat_baza.model.privateHouses.PrivateHouseSale;
import ru.simplex_software.arbat_baza.model.stead.SteadArea;

/**
 * .
 */
public class PrivateHouseRentDescRendererRu extends FlatDescriptionRenderer {

    public String toLocolizedString(RealtyObject realtyObject) {
        PrivateHouseRent st=(PrivateHouseRent)realtyObject;
        SteadArea area = (SteadArea) st.getArea();
        return  " Площадь: площадь участка:"+nullChk(area.getLendArea())
                +"\n"+ "Площадь дома:"+nullChk(area.getTotal());

    }
}
