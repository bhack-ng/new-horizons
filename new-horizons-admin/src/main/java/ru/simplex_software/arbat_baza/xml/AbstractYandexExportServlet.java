package ru.simplex_software.arbat_baza.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestHandler;
import ru.simplex_software.arbat_baza.Utils;
import ru.simplex_software.arbat_baza.dao.fias.FiasSocrDAO;
import ru.simplex_software.arbat_baza.model.Agency;
import ru.simplex_software.arbat_baza.model.Photo;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.arbat_baza.model.fias.FiasAddressVector;
import ru.simplex_software.arbat_baza.model.fias.FiasObject;
import ru.simplex_software.arbat_baza.model.fias.FiasSocr;
import ru.simplex_software.arbat_baza.model.live.LiveArea;
import ru.simplex_software.arbat_baza.model.stead.SteadArea;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public abstract class AbstractYandexExportServlet implements HttpRequestHandler {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractYandexExportServlet.class);

    @Resource
    protected FiasSocrDAO fiasSocrDAO;

    protected XMLOutputFactory outputXML;
    protected XMLStreamWriter xmlWriter;

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        PrintWriter writer = response.getWriter();

        try {
            outputXML = XMLOutputFactory.newInstance();
            xmlWriter = outputXML.createXMLStreamWriter(writer);
            for (RealtyObject offer : loadListRealtyObjects()) {

                xmlWriter.writeStartElement("offer"); //<offer>
                xmlWriter.writeAttribute("internal-id", offer.getId().toString());
                //Печать тега <type>
                writeContractType(offer);
                writeTypeObject();
                writeMainElements(offer); //печать основных данных об объекте
                writePriceElements(offer); //печать данных о цене
                writeOptions(offer); //печать опций

                //ИНФОРМАЦИЯ ОБ ОБЪЕКТЕ
                //Общая площадь ОБЯЗАТЕЛЬНЫЙ ЭЛЕМЕНТ !!!
                if (offer.getArea().getTotal() != null) {
                    xmlWriter.writeStartElement("area");
                    essentialTagWriter("value", offer.getArea().getTotal().intValue());
                    tagWriter("unit", "Кв.м.");
                    xmlWriter.writeEndElement();
                }

                if (offer.getArea() instanceof SteadArea) {
                    SteadArea area = (SteadArea) offer.getArea();
                    if (area.getLendArea() != null) {   //УБРАТЬ ПОТОМ!!! ОБЯЗАТЕЛЬНО!!!
                        xmlWriter.writeStartElement("lot-area");

                        essentialTagWriter("value", area.getLendArea().intValue());
                        tagWriter("unit", "Кв.м.");

                        xmlWriter.writeEndElement();
                    }
                }
                //изображения
                //ОБЯЗАТЕЛЬНЫЙ ЭЛЕМЕНТ !!!
                List<Photo> photoList = offer.getPhotos();
                String urlStr = Utils.getFullContextUrl(request);
                for (Photo photo : photoList) {
                    String imglink = urlStr + "/photo?id=" + photo.getId().toString();
                    tagWriter("image", imglink);
                }
                //дополнительная информация
                tagWriter("description", offer.getNote());

                writeAboutObject(offer);//Печать дополнительных данных об объекте
                buildWriter(offer); //этаж и этажность здания
                xmlWriter.writeEndElement();//</offer>

            }//конец цикла for
            xmlWriter.close();

        } catch (XMLStreamException ex) {
            response.setStatus(500);
            Date date = new Date();
            LOG.error("export error at time" + date, ex);
            writer.write("internal error" + date);
        }
    }//end of handleRequest

    protected void writeContractType(RealtyObject offer) throws XMLStreamException {
        String type = null;
        switch (offer.getRealtyObjectType()) {

            case COMMERCE_SALE:
                type = "продажа";
                break;
            case COMMERCE_LEASE:
                type = "аренда";
                break;
            case LIVE_SALE:
                type = "продажа";
                break;
            case LIVE_LEASE:
                type = "аренда";
                break;
            case STEAD_RENT:
                type = "аренда";
                break;
            case STEAD_SALE:
                type = "продажа";
                break;
            case PRIVATE_HOUSE_SALE:
                type = "продажа";
                break;
            case PRIVATE_HOUSE_RENT:
                type = "аренда";
                break;
        }
        tagWriter("type", type);
    }

    //наличие лифта
    protected void writeLift(LiveArea liveArea) throws XMLStreamException {
        if (hasLift(liveArea)) {
            tagWriter("lift", "true");
        }
    }

    public static boolean hasLift(LiveArea liveArea) {
        return !((liveArea.getLift_p() == null || liveArea.getLift_p() == 0) && (liveArea.getLift_g() == null || liveArea.getLift_g() == 0));
    }

    //тип санузла
    void bathroomUnitTagWriter(LiveArea liveArea) throws XMLStreamException {
        if (liveArea.getSplitBathroomCount() == null && liveArea.getFullBathroomCount() == null) {
            return;
        }
        if (liveArea.getSplitBathroomCount() != null) {
            tagWriter("bathroom-unit", "раздельный");
        }
        if (liveArea.getFullBathroomCount() != null) {
            tagWriter("bathroom-unit", "совмещенный");
        }
    }

    //балконы
    protected void writeBalconyCount(LiveArea liveArea) throws XMLStreamException {
        //БАЛКОНЫ И ЛОДЖИИ
        if (liveArea.getBalconyCount() != null || liveArea.getRecessedBalconyCount() != null) {
            //Integer count;
            String msg = "";
            xmlWriter.writeStartElement("balcony");

            if (liveArea.getBalconyCount() != null) {

                switch (liveArea.getBalconyCount()) {
                    case 1:
                        msg = " балкон";
                        break;
                    case -1:
                        msg = " балкон";
                        break;
                    case 2:
                        msg = "2 балкона";
                        break;
                    case 3:
                        msg = "3 балкона";
                        break;
                    case 4:
                        msg = "4 балкона";
                        break;
                    default:
                        msg = liveArea.getBalconyCount().toString() + " балконов";
                }
                xmlWriter.writeCharacters(msg);
            } else {

                switch (liveArea.getRecessedBalconyCount()) {
                    case 1:
                        msg = " лоджия";
                        break;
                    case -1:
                        msg = " лоджия";
                        break;
                    case 2:
                        msg = "2 лоджии";
                        break;
                    case 3:
                        msg = "3 лоджии";
                        break;
                    case 4:
                        msg = "4 лоджии";
                        break;
                    default:
                        msg = liveArea.getRecessedBalconyCount().toString() + " лоджий";
                }
                xmlWriter.writeCharacters(msg);
            }

            xmlWriter.writeEndElement();//</writeBalconyCount>
        }
    }

    //Печать основных данных о цене
    protected void writePriceElements(RealtyObject offer) throws XMLStreamException { //Печать элементов сделки

        if (offer.getPrice() != null) {
            xmlWriter.writeStartElement("price"); //<price>

            //цена ОБЯЗАТЕЛЬНЫЙ ЭЛЕМЕНТ !!!
            essentialTagWriter("value", offer.getPrice().getValue());

            //Валюта    //ОБЯЗАТЕЛЬНЫЙ ЭЛЕМЕНТ !!!
            essentialTagWriter("currency", offer.getPrice().getCurrency());

            writeInPriceElement(offer);//- абстрактный метод

            xmlWriter.writeEndElement(); //</price>

        } else {
            LOG.error("no a required element: price");
        }

        //комиссия
        if (offer.getCommission() != null) {
            tagWriter("commission", offer.getCommission().getBuyerCommission());
        }
        //залог
        writeDeposit(offer); //Выводит залог - абстрактный метод
    }

    //печать основных элементов об объекте
    protected void writeMainElements(RealtyObject offer) throws XMLStreamException {
        //печать предложения

        //в отдельном методе находятся печать тегов type, property-type, category
        //или в строках

        //номер лота
        tagWriter("lot-number", offer.getId());

        //дата создания объявления    //ОБЯЗАТЕЛЬНЫЙ ЭЛЕМЕНТ !!!
        tagWriter("creation-date", offer.getCreationTime().atZone(TimeZone.getDefault().toZoneId()).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));

        //дата последнего обновления    //ОБЯЗАТЕЛЬНЫЙ ЭЛЕМЕНТ !!!
        LocalDateTime dateTime = offer.getChanged();

        //String format = dateTime.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        String format = dateTime.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        tagWriter("last-update-date", format);

        //МЕСТОПОЛОЖЕНИЕ ОБЪЕКТА ОБЯЗАТЕЛЬНЫЙ ЭЛЕМЕНТ !!
        xmlWriter.writeStartElement("location");//<location>

        //страна   //ОБЯЗАТЕЛЬНЫЙ ЭЛЕМЕНТ !!!
        tagWriter("country", "Россия");

        //регион
        FiasAddressVector fiasAddressVector = offer.getAddress().getFiasAddressVector();
        tagWriter("region", fiasAddressVector.getLevel1());

        //район субъекта РФ 3
        tagWriter("district", fiasAddressVector.getLevel3());

        //населенный пункт (город, село)
        tagWriter("locality-name", fiasAddressVector.getLevel4());

        //район населенного пункта 6
        tagWriter("sub-locality-name", fiasAddressVector.getLevel6());

        //адрес (улица и номер дома)
        if (fiasAddressVector.getLevel7() != null) {

            if (offer.getAddress().getHouse_str() != null) {
                String formalName = fiasAddressVector.getLevel7().getFORMALNAME();
                String shortName = fiasAddressVector.getLevel7().getSHORTNAME();
                String houseNumber = offer.getAddress().getHouse_str();
                tagWriter("address", formalName + " " + shortName + "., д. " + houseNumber);
            } else {
                tagWriter("address", fiasAddressVector.getLevel7());
            }
        }

        //метро
        if (offer.getMetroLocation().getMetroStation() != null) {
            xmlWriter.writeStartElement("metro");
            tagWriter("name", offer.getMetroLocation().getMetroStation().getStationName());

            if (offer.getMetroLocation().getAnyTime() != null) {
                if (offer.getMetroLocation().isWtimeSelected()) {
                    tagWriter("time-on-foot", offer.getMetroLocation().getAnyTime());
                } else {
                    tagWriter("time-on-transport", offer.getMetroLocation().getAnyTime());
                }
            }
            xmlWriter.writeEndElement();
        }

        //координаты
        //широта
        tagWriter("latitude", offer.getAddress().getGeoCoordinates().getLatitude());
        //долгота
        tagWriter("longitude", offer.getAddress().getGeoCoordinates().getLongitude());

        insideLocalityTag(offer);

        xmlWriter.writeEndElement();//</location>


        //ИНФОРМАЦИЯ ОБ АРЕНДОДАТЕЛЕ
        xmlWriter.writeStartElement("sales-agent");//<sales-agent>

        //имя агента
        tagWriter("name", offer.getAgent().getFio());

        //телефон агента
        // ОБЯЗАТЕЛЬНЫЙ ЭЛЕМЕНТ !!!
        essentialTagWriter("phone", offer.getAgent().getPhone());

        //Категория
        tagWriter("category", "agency");

        //организация
        Agency agency = offer.getAgent().getAgency();
        tagWriter("organization", agency.getName());

        //сайт агентства или застройщика
        tagWriter("url", agency.getAgencyURL());


        //электронная почта продавца
        tagWriter("email", offer.getAgent().getEmail());

        xmlWriter.writeEndElement();//</sales-agent>

    }

    protected void insideLocalityTag(RealtyObject offer) throws XMLStreamException {
        //for overriding in YandexPrivateHouseServlet and YandexSteadServlet
    }

    protected void buildWriter(RealtyObject offer) throws XMLStreamException {
        essentialTagWriter("floor", offer.getBuilding().getFloor());
        essentialTagWriter("floors-total", offer.getBuilding().getFloorTotal());
    }

    protected void tagWriter(String tagName, Object object) throws XMLStreamException {
        if (object != null) {
            xmlWriter.writeStartElement(tagName);
            xmlWriter.writeCharacters(object.toString());
            xmlWriter.writeEndElement();
           }
    }

    protected void tagWriter(String tagName, FiasObject fiasObj) throws XMLStreamException {
        if (fiasObj != null) {

            String formalName = fiasObj.getFORMALNAME();
            FiasSocr socr = fiasSocrDAO.findRegionFullName(fiasObj.getAOLEVEL(), fiasObj.getSHORTNAME());
            String shortName = socr.getFullName();

            xmlWriter.writeStartElement(tagName);
            xmlWriter.writeCharacters(formalName + " " + shortName);
            xmlWriter.writeEndElement();
        }
    }

    protected void essentialTagWriter(String tagName, Object object) throws XMLStreamException {
        if (object != null) {
            tagWriter(tagName, object.toString());
        } else {
            LOG.error("essential tag is emty: " + tagName);
        }
    }

    protected void writeInPriceElement(RealtyObject offer) throws XMLStreamException {
    }

    //абстрактные методы
    protected abstract void writeOptions(RealtyObject offer) throws XMLStreamException; //абстрактный - печать опций (Options)

    protected abstract List<? extends RealtyObject> loadListRealtyObjects(); //абстрактный - загрузка списка объектов

    abstract protected void writeAboutObject(RealtyObject offer) throws XMLStreamException;

    protected abstract void writeTypeObject() throws XMLStreamException; //тип сделки, тип недвижимости, категория объекта и т.д.

    protected void writeDeposit(RealtyObject offer) throws XMLStreamException {
    } //печать залога

}//end of Servlet(RealtyObject





