package ru.simplex_software.arbat_baza.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplex_software.arbat_baza.dao.LiveLeaseRealtyDAO;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.arbat_baza.model.live.LiveArea;
import ru.simplex_software.arbat_baza.model.live.LiveLeaseOptions;
import ru.simplex_software.arbat_baza.model.price.LiveRentPrice;

import javax.annotation.Resource;
import javax.xml.stream.XMLStreamException;
import java.util.List;

/**
 * Сервлет создает фид для автоматической выгрузки объявлений на Яндекс.Недвижимость
 * <p>
 * Рекомендуется в форме добавления/редактирования объявлений
 * заменить элемент CheckBox на поле ввода депозита
 * значение депозита вводить либо как процент от аренды, либо как сумма депозита
 * <p>
 * в Яндекс вводится значение депозита в процентах
 * <p>
 * цена аренды указывается только за месяц. Возможности указать цену аренды за сутки нет.
 * <p>
 * рекомендуются следующие пункты:
 * railway-station
 * Ближайшая железнодорожная станция
 * <p>
 * renovation
 * Ремонт.
 * <p>
 * quality
 * Состояние объекта.
 * <p>
 * studio
 * тип объекта - студия
 * <p>
 * bathroom-unit
 * тип санузла
 * <p>
 * наличие интернета
 * internet
 * <p>
 * системы кондиционирования
 * air-conditioner
 * <p>
 * покрытие пола
 * floor-coverting
 */
public class YandexLeaseExportServlet extends AbstractYandexExportServlet {
    private static final Logger LOG = LoggerFactory.getLogger(YandexLeaseExportServlet.class);

    @Resource
    private LiveLeaseRealtyDAO liveLeaseRealtyDAO;

    @Override
    protected List<? extends RealtyObject> loadListRealtyObjects() {
        return liveLeaseRealtyDAO.findToYandexExport();
    }

    //тип сделки
    @Override
    protected void writeTypeObject() throws XMLStreamException {


        //тип недвижимости    //ОБЯЗАТЕЛЬНЫЙ ЭЛЕМЕНТ !!!
        tagWriter("property-type", "жилая");

        //категория объекта    //ОБЯЗАТЕЛЬНЫЙ ЭЛЕМЕНТ !!!
        tagWriter("category", "квартира");
    }

    //период (в методе writePriceElements)
    @Override
    protected void writeInPriceElement(RealtyObject offer) throws XMLStreamException {
        tagWriter("period", "month");
    }

    //Печать депозита (в методе writePriceElements)
    @Override
    protected void writeDeposit(RealtyObject offer) throws XMLStreamException {
        LiveRentPrice liveRentPrice = (LiveRentPrice) offer.getPrice();
        if (liveRentPrice.getDeposit() == Boolean.TRUE) {
            tagWriter("security-payment", "100");
        }
    }

    @Override
    protected void writeOptions(RealtyObject offer) throws XMLStreamException {
        LiveLeaseOptions liveLeaseOptions = (LiveLeaseOptions) offer.getOptions();

        //c животными
        tagWriter("with-pets", liveLeaseOptions.getPets());

        //с детьми
        tagWriter("with-children", liveLeaseOptions.getChildren());

        //мебель
        tagWriter("room-furniture", liveLeaseOptions.getMebel());

        //кухонная мебель
        tagWriter("kitchen-furniture", liveLeaseOptions.getMebelInKitchen());

        //охраняемая парковка
        tagWriter("parking", liveLeaseOptions.getParking());

        //охрана
        tagWriter("security", liveLeaseOptions.getSecurity());

        //телефон
        tagWriter("phone", liveLeaseOptions.getPhone());

        //стиральная машина
        tagWriter("washing-machine", liveLeaseOptions.getWashMashine());

        //холодильник
        tagWriter("refrigerator", liveLeaseOptions.getRefrigerator());

        //телевизор
        tagWriter("television", liveLeaseOptions.getTv());

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
