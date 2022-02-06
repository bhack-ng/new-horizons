package ru.simplex_software.arbat_baza.viewmodel.live;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.arbat_baza.model.live.LiveSaleRealty;
import ru.simplex_software.arbat_baza.viewmodel.FlatDescriptionRenderer;

/**
 * Отображение продажи жилой недвижимости в главной, колонка "Параметры объекта".
 */

public class LiveSaleDescRendererRu extends FlatDescriptionRenderer {
    private static final Logger LOG = LoggerFactory.getLogger(LiveSaleDescRendererRu.class);

    @Override
    public String toLocolizedString(RealtyObject realtyObject) {
        LiveSaleRealty lr=(LiveSaleRealty)realtyObject;
        return "Комнат:"+nullChk(lr.getArea().getRoomsCount())+ " Этаж:" +nullChk(lr.getBuilding().getFloor())+" этаж из "
                + nullChk(lr.getBuilding().getFloorTotal()) + " Площадь: общая площадь:"+nullChk(lr.getArea().getTotal())
                ;
    }
}
