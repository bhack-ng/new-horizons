package ru.simplex_software.arbat_baza.viewmodel.commerce;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.arbat_baza.model.commerce.Commerce;
import ru.simplex_software.arbat_baza.model.commerce.CommerceArea;
import ru.simplex_software.arbat_baza.viewmodel.FlatDescriptionRenderer;

/**
 *  Отображение коммерческой недвижимости в главной, колонка "Параметры объекта"..
 */

public class CommerceFlatDescRendererRu extends FlatDescriptionRenderer {
    private static final Logger LOG = LoggerFactory.getLogger(CommerceFlatDescRendererRu.class);

    @Override
    public String toLocolizedString(RealtyObject realtyObject) {
        Commerce com=(Commerce)realtyObject;
        return "Комнат:"+nullChk(com.getArea().getRoomsCount())+ " Этаж:" +nullChk(com.getBuilding().getFloor())+" этаж из "
                + nullChk(com.getBuilding().getFloorTotal()) + " Площадь: общая площадь:"+nullChk(com.getArea().getTotal())
                +"кв. м., мин. пл.:" +nullChk(((CommerceArea)com.getArea()).getMin());
    }
}
