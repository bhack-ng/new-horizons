package ru.simplex_software.arbat_baza.viewmodel;

import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.arbat_baza.model.privateHouses.PrivateHouseSale;
import ru.simplex_software.arbat_baza.model.stead.SteadArea;
import ru.simplex_software.arbat_baza.model.stead.SteadSale;

/**
 * .
 */
public class PrivateHouseSaleDescRendererRu extends FlatDescriptionRenderer {
    public String toLocolizedString(RealtyObject realtyObject) {
        PrivateHouseSale st=(PrivateHouseSale)realtyObject;
        SteadArea  area = (SteadArea) st.getArea();
        return  " Площадь: площадь участка:"+nullChk(area.getLendArea())
                +"\n"+ "Площадь дома:"+nullChk(area.getTotal());

    }
}
