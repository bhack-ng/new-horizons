package ru.simplex_software.arbat_baza.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplex_software.arbat_baza.dao.LiveSaleRealtyDAO;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.arbat_baza.model.live.LiveArea;
import ru.simplex_software.arbat_baza.model.live.LiveSaleOptions;

import javax.annotation.Resource;
import javax.xml.stream.XMLStreamException;
import java.util.List;

public class YandexSaleExportServlet extends AbstractYandexExportServlet {
    private static final Logger LOG = LoggerFactory.getLogger(YandexSaleExportServlet.class);

    @Resource
    private LiveSaleRealtyDAO liveSaleRealtyDAO;

    @Override
    protected void writeOptions(RealtyObject offer) throws XMLStreamException {
        LiveSaleOptions options = (LiveSaleOptions) offer.getOptions();

        //ипотека
        tagWriter("mortgage",options.getHomeMortgage());

        //наличие телефона
        tagWriter("phone",options.getPhone());
    }

    @Override
    protected List<? extends RealtyObject> loadListRealtyObjects() {
        return liveSaleRealtyDAO.findToYandexExport();
    }

    @Override
    protected void writeTypeObject() throws XMLStreamException {


        //тип недвижимости    //ОБЯЗАТЕЛЬНЫЙ ЭЛЕМЕНТ !!!
        tagWriter("property-type", "жилая");

        //категория объекта    //ОБЯЗАТЕЛЬНЫЙ ЭЛЕМЕНТ !!!
        tagWriter("category", "квартира");
    }

    @Override
    protected void writeAboutObject(RealtyObject offer) throws XMLStreamException {
        //лифт, балкон и тип санузла

        LiveArea liveArea = (LiveArea) offer.getArea(); //для жилой недвижимости

        //количество комнат ОБЯЗАТЕЛЬНЫЙ ЭЛЕМЕНТ !!!
        essentialTagWriter("rooms", liveArea.getRoomsCount());

        //Участвующих в сделке ОБЯЗАТЕЛЬНЫЙ ЭЛЕМЕНТ !!!
        essentialTagWriter("rooms-offered", liveArea.getRoomsCount());
        //лифт
        writeLift(liveArea);
        //записываем наличие балкона или лоджии
        writeBalconyCount(liveArea);
        //записываем тип санузла (раздельный или совмещенный)
        bathroomUnitTagWriter(liveArea);


    }


}
