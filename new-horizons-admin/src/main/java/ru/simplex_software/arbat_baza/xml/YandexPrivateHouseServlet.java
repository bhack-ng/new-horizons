package ru.simplex_software.arbat_baza.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplex_software.arbat_baza.dao.PrivateHouseRentDao;
import ru.simplex_software.arbat_baza.dao.PrivateHouseSaleDao;
import ru.simplex_software.arbat_baza.model.Address;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.arbat_baza.model.fias.FiasAddressVector;
import ru.simplex_software.arbat_baza.model.privateHouses.PrivateHouse;
import ru.simplex_software.arbat_baza.model.privateHouses.PrivateHouseRent;
import ru.simplex_software.arbat_baza.model.privateHouses.PrivateHouseSale;

import javax.annotation.Resource;
import javax.xml.stream.XMLStreamException;
import java.util.ArrayList;
import java.util.List;

public class YandexPrivateHouseServlet extends AbstractYandexExportServlet {
    private static final Logger LOG = LoggerFactory.getLogger(YandexPrivateHouseServlet.class);

    @Resource
    private PrivateHouseRentDao privateHouseRentDao;
    @Resource
    private PrivateHouseSaleDao privateHouseSaleDao;

    @Override
    protected void writeOptions(RealtyObject offer) throws XMLStreamException {
        //is empty
    }

    @Override
    protected List<? extends RealtyObject> loadListRealtyObjects() {
        List<PrivateHouseRent> rentList = privateHouseRentDao.findToYandexExport();
        List<PrivateHouseSale> saleList = privateHouseSaleDao.findToYandexExport();
        List<RealtyObject> list = new ArrayList<RealtyObject>();
        list.addAll(rentList);
        list.addAll(saleList);
        return list;
    }

    @Override
    protected void writeAboutObject(RealtyObject offer) throws XMLStreamException {
        writeBuildingType(offer);
    }

    @Override
    protected void writeTypeObject() throws XMLStreamException {
//тип сделки     //ОБЯЗАТЕЛЬНЫЙ ЭЛЕМЕНТ !!!

        //тип недвижимости    //ОБЯЗАТЕЛЬНЫЙ ЭЛЕМЕНТ !!!
        tagWriter("property-type", "жилая");

        //категория объекта    //ОБЯЗАТЕЛЬНЫЙ ЭЛЕМЕНТ !!!
        tagWriter("category", "дом с участком");
    }

    private void writeBuildingType(RealtyObject offer) throws XMLStreamException {

        String wallsType = null;
        PrivateHouse privateHouse = (PrivateHouse) offer;

        if (privateHouse.getWallsType() != null) {
            switch (privateHouse.getWallsType()) {

                case BRICK:
                    wallsType = "кирпич";
                    break;
                case TIMBER:
                    wallsType = "брус";
                    break;
                case BULK:
                    wallsType = "бревно";
                    break;
                case METAL:
                    wallsType = "металл";
                    break;
                case AIR_BLOCKS:
                    wallsType = "пеноблоки";
                    break;
                case SANDWICH_PANELS:
                    wallsType = "сэндвич-панели";
                    break;
                case EXPERIMENTAL_MATERIAL:
                    wallsType = "экспериментальные материалы";
                    break;
                case FERROCONCRETE:
                    wallsType = "Ж/Б панели";
                    break;
            }
            tagWriter("building-type", wallsType);
        }
    }

    @Override
    protected void writeInPriceElement(RealtyObject offer) throws XMLStreamException {
        String period = null;
        switch (offer.getRealtyObjectType()) {

            case COMMERCE_LEASE:
                period = "месяц";
                break;
            case LIVE_LEASE:
                period = "месяц";
                break;
            case STEAD_RENT:
                period = "месяц";
                break;
            case PRIVATE_HOUSE_RENT:
                period = "месяц";
                break;
            default:
                break;
        }
        tagWriter("period", period);
    }

    @Override
    protected void insideLocalityTag(RealtyObject offer) throws XMLStreamException {
        PrivateHouse privateHouse = (PrivateHouse) offer;
        Address address = offer.getAddress();

        FiasAddressVector fiasAddressVector = address.getFiasAddressVector();
        if (!fiasAddressVector.getLevel1().getFORMALNAME().toString().equals("Москва")) {
            return;
        }

        if (privateHouse.getDirectionRoad() == null) {
            return;
        }
        tagWriter("direction", privateHouse.getDirectionRoad().getRoadName());
        tagWriter("distance", privateHouse.getDistanceToCity());
    }
}
