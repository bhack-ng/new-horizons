package ru.simplex_software.arbat_baza.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplex_software.arbat_baza.dao.commerce.CommerceDAO;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.arbat_baza.model.commerce.Commerce;
import ru.simplex_software.arbat_baza.model.commerce.CommerceBuilding;
import ru.simplex_software.arbat_baza.model.commerce.CommerceOptions;
import ru.simplex_software.arbat_baza.model.price.CommercePrice;
import ru.simplex_software.arbat_baza.model.price.Period;

import javax.annotation.Resource;
import javax.xml.stream.XMLStreamException;
import java.util.List;

public class YandexCommerceServlet extends AbstractYandexExportServlet {
    private static final Logger LOG = LoggerFactory.getLogger(YandexCommerceServlet.class);

    @Resource
    private CommerceDAO commerceDAO;

    @Override
    protected void writeOptions(RealtyObject offer) throws XMLStreamException {
        Commerce commerce = (Commerce) offer;
        CommerceOptions commerceOptions = commerce.getOptions();
        //печать статуса сделки
        writeDealStatus(commerce);
        writeCommerceType(commerce);


        //количество телефонных линий
        tagWriter("phone-lines", commerceOptions.getPhones());
        //возможность добавления линий
        tagWriter("adding-phone-on-request", commerceOptions.getAdd_phones());
        //интернет
        tagWriter("internet", commerceOptions.getInternet());
        //наличие мебели
        writeRoomFurniture(commerceOptions);
        //водоснабжение
        tagWriter("water-supply", commerceOptions.getWater());
        //канализация
        tagWriter("sewerage-supply", commerceOptions.getCanal());
        //электроснабжение
        tagWriter("electricity-supply", commerceOptions.getElect());
        //газоснабжение
        tagWriter("gas-supply", commerceOptions.getGas());
        //отопление
        tagWriter("heating-supply", commerceOptions.getHeat());
        //лифт
        tagWriter("lift", commerceOptions.getLift());
        //парковка
        tagWriter("parking", commerceOptions.getParking());
        //охрана
        tagWriter("security", commerceOptions.getSecurity());

    }

    private void writeCommerceType(Commerce commerce) throws XMLStreamException {
        if (commerce.getType() != null) {
            String type = "free purpose";
            switch (commerce.getType()) {

                case OFFICE:
                    type = "office";
                    break;
                case WAREHOUSE:
                    type = "warehouse";
                    break;
                case TRADE:
                    type = "retail";
                    break;
                case FOOD_SERVICES:
                    type = "public catering";
                    break;
                case FREE_PURPOSE:
                    type = "free purpose";
                    break;

                case CAR_SERVICE_CENTER:
                    type = "auto repair";
                    break;
                case MANUFACTURING_AREA:
                    type = "manufacturing";
                    break;
                case DETACHED_BUILDING:
                    tagWriter("commercial-building-type", "detached building");
                    break;
                case LEGAL_ADDRESS:
                    type = "legal adress";
                    break;
                case BUSINESS_FOR_SALE:
                    type = "business";
                    break;


            }
            tagWriter("commercial-type", type);
        }
    }

    private void writeDealStatus(Commerce commerce) throws XMLStreamException {
        if (commerce.getContractType() != null) {
            String status = null; //обязателен только для аренды, при продаже не указывается

            switch (commerce.getContractType()) {

                case DIRECT_LEASING:
                    status = "direct rent";
                    break;
                case SUBLEASE:
                    status = "subrent";
                    break;
                case SALE_OF_LEASEHOLD:
                    break;
                case SALE_REALTY_OBJECT:
                    break;
                case CONTRACT_OF_JOINT_ACTIVITY:
                    status = "joint rent";
                    break;
            }

            //тип сделки     //ОБЯЗАТЕЛЬНЫЙ ЭЛЕМЕНТ !!!

            tagWriter("deal-status", status);
        }
    }

    private void writeRoomFurniture(CommerceOptions commerceOptions) throws XMLStreamException {
        if (commerceOptions.getMebel() == CommerceOptions.MebelExistance.NO) {
            tagWriter("room-furniture", "false");
        } else {
            tagWriter("room-furniture", "true");

        }
    }

    @Override
    protected List<? extends RealtyObject> loadListRealtyObjects() {
        return commerceDAO.findToYandexExport();
    }

    @Override
    protected void writeAboutObject(RealtyObject offer) throws XMLStreamException {
        CommerceBuilding commerceBuilding = (CommerceBuilding) offer.getBuilding();

        // класс здания, однако в Яндекс используется класс офиса.
        //tagWriter("office-class",commerceBuilding.getBuildingClass());
    }

    @Override
    protected void writeTypeObject() throws XMLStreamException {

        //категория объекта    //ОБЯЗАТЕЛЬНЫЙ ЭЛЕМЕНТ !!!
        tagWriter("category", "коммерческая");


    }


    @Override
    protected void writeInPriceElement(RealtyObject offer) throws XMLStreamException {
        CommercePrice commercePrice = (CommercePrice) offer.getPrice();
        if (commercePrice.getPeriod() == Period.MONTH) {
            tagWriter("period", "месяц");
        }
        if (commercePrice.getPeriod() == Period.M2_PER_YEAR) {
            tagWriter("unit", "кв.м.");
            tagWriter("period", "год");
        }
    }
}
