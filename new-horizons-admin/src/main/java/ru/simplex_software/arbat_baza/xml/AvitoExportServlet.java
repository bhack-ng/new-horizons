package ru.simplex_software.arbat_baza.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestHandler;
import ru.simplex_software.arbat_baza.GeocodeService;
import ru.simplex_software.arbat_baza.Utils;
import ru.simplex_software.arbat_baza.dao.LiveLeaseRealtyDAO;
import ru.simplex_software.arbat_baza.dao.LiveSaleRealtyDAO;
import ru.simplex_software.arbat_baza.dao.fias.FiasObjectDAO;
import ru.simplex_software.arbat_baza.dao.fias.FiasSocrDAO;
import ru.simplex_software.arbat_baza.model.GeoCoordinates;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.arbat_baza.model.fias.FiasObject;
import ru.simplex_software.arbat_baza.model.live.BuildingTypeSale;
import ru.simplex_software.arbat_baza.model.live.DwellingHouse;
import ru.simplex_software.arbat_baza.model.live.LiveArea;
import ru.simplex_software.arbat_baza.model.live.LiveLeaseRealty;
import ru.simplex_software.arbat_baza.model.live.LiveSaleRealty;
import ru.simplex_software.arbat_baza.model.price.LiveRentPrice;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by Nick on 07.09.2016.
 * Класс предназначенный для кспорта в Avito
 * в виде xml обьектав типа аренды и продажи помещений
 *
 */



public class AvitoExportServlet implements HttpRequestHandler {
    private static final Logger LOG = LoggerFactory.getLogger(AvitoExportServlet.class);

    private List<LiveSaleRealty> listLiveSaleRealty;
    @Resource
    private LiveSaleRealtyDAO liveSaleRealtyDAO;
    @Resource
    private LiveLeaseRealtyDAO liveLeaseRealtyDAO;
    @Resource
    private FiasObjectDAO fiasObjectDAO;
    @Resource
    private FiasSocrDAO fiasSocrDAO;
    @Resource
    private GeocodeService geocodeService;
    private  List<LiveLeaseRealty> listLiveLeaseRealty;
    private ThreadLocal urlLocal = new ThreadLocal();
    private GeoCoordinates coord;


    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String urlStr = Utils.getFullContextUrl(request);
        urlLocal.set(urlStr);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "text/xml;charset=UTF-8");
        XmlUtils.outputAgent.set("true".equals(request.getParameter("site")));
        PrintWriter writer = response.getWriter();

        /** выгрузка для avito.*/
        if (request.getServletPath().equals("/avito/avito.xml")) {
            listLiveSaleRealty = liveSaleRealtyDAO.findToAvitoExport();
            listLiveLeaseRealty = liveLeaseRealtyDAO.findToAvitoExport();
        }

        /** выгрузка для внешних сайтов в формате avito.*/
        else if (request.getServletPath().equals("/external/avito.xml")) {
            listLiveLeaseRealty = liveLeaseRealtyDAO.findToExternalAvitoExport();
            listLiveSaleRealty = liveSaleRealtyDAO.findToExternalAvitoExport();
        }
        try {
            writeXml(writer);
        } catch (Exception ex) {
            response.setStatus(500);
            Date date = new Date();
            LOG.error("export error at time" + date, ex);
            writer.write("internal error" + date);
        }
    }

    public void writeXml(PrintStream ps) throws Exception {
        XMLOutputFactory output = XMLOutputFactory.newInstance();
        XMLStreamWriter xmlWriter = output.createXMLStreamWriter(ps, "UTF-8");

        writeXml(xmlWriter);
    }

    public void writeXml(PrintWriter pw) throws Exception {

        XMLOutputFactory output = XMLOutputFactory.newInstance();
        XMLStreamWriter xmlWriter = output.createXMLStreamWriter(pw);
        writeXml(xmlWriter);

    }

    private void writeXml(XMLStreamWriter rawWriter) throws Exception {
        PrettyPrintHandler handler = new PrettyPrintHandler(rawWriter);

        XMLStreamWriter xmlWriter = (XMLStreamWriter) Proxy.newProxyInstance(
                XMLStreamWriter.class.getClassLoader(),
                new Class[]{
                        XMLStreamWriter.class
                },
                handler);

        xmlWriter.writeStartDocument("UTF-8", "1.0");


        xmlWriter.writeStartElement("Ads");
        xmlWriter.writeAttribute("formatVersion", "3");
        xmlWriter.writeAttribute("target", "Avito.ru");

        for (LiveSaleRealty lsr : listLiveSaleRealty) {
            xmlWriter.writeStartElement("Ad");

            writeGeneralProperties(xmlWriter, lsr);

            if (lsr.getBuildingTypeSale() != null) {
                xmlWriter.writeStartElement("MarketType");
                if (lsr.getBuildingTypeSale() == BuildingTypeSale.NEW_BUILDING) {
                    xmlWriter.writeCharacters("Новостройка");
                }
                if (lsr.getBuildingTypeSale() == BuildingTypeSale.SECONDHAND) {
                    xmlWriter.writeCharacters("Вторичка");
                }
                xmlWriter.writeEndElement();
            }

            xmlWriter.writeStartElement("OperationType");
            xmlWriter.writeCharacters("Продам");
            xmlWriter.writeEndElement();

            xmlWriter.writeEndElement();  //Ad
        }

        for(LiveLeaseRealty llr: listLiveLeaseRealty){
            xmlWriter.writeStartElement("Ad");
            writeGeneralProperties(xmlWriter,llr);

            xmlWriter.writeStartElement("OperationType");
            xmlWriter.writeCharacters("Сдам");
            xmlWriter.writeEndElement();

            xmlWriter.writeStartElement("LeaseType");
            xmlWriter.writeCharacters("На длительный срок");
            xmlWriter.writeEndElement();


            if(llr.getCommission().getSalesCommission()!=null){
                xmlWriter.writeStartElement("LeaseCommissionSize");
                xmlWriter.writeCharacters(""+llr.getCommission().getSalesCommission().intValue());
                xmlWriter.writeEndElement();
            } else if(llr.getCommission().getSalesCommission()==null){
                xmlWriter.writeStartElement("LeaseCommissionSize");
                xmlWriter.writeCharacters(""+0);
                xmlWriter.writeEndElement();
            }

            LiveRentPrice liveRentPrice= (LiveRentPrice) llr.getPrice();
            if((liveRentPrice.getPrepay()==null)||(liveRentPrice.getPrepay()==0)){
                xmlWriter.writeStartElement("LeaseDeposit");
                xmlWriter.writeCharacters("Без залога");
                xmlWriter.writeEndElement();
            }else {
                xmlWriter.writeStartElement("LeaseDeposit");
                switch (liveRentPrice.getPrepay()){
                    case 1: xmlWriter.writeCharacters("1 месяц");
                        break;
                    case 2: xmlWriter.writeCharacters("2 месяца");
                        break;
                    case 3: xmlWriter.writeCharacters("3 месяца");
                        break;
                }

                xmlWriter.writeEndElement();
            }

            xmlWriter.writeStartElement("LeaseComfort");

            LiveArea liveArea = (LiveArea) llr.getArea();
            if ((liveArea.getBalconyCount() != null) && (liveArea.getRecessedBalconyCount() != null)) {
                if (liveArea.getBalconyCount() + liveArea.getRecessedBalconyCount() > 0) {
                    xmlWriter.writeStartElement("Option");
                    xmlWriter.writeCharacters("Балкон / лоджия");
                    xmlWriter.writeEndElement();
                }
            } else if ((liveArea.getBalconyCount() != null) && (liveArea.getRecessedBalconyCount() == null)) {
                if (liveArea.getBalconyCount() > 0) {
                    xmlWriter.writeStartElement("Option");
                    xmlWriter.writeCharacters("Балкон / лоджия");
                    xmlWriter.writeEndElement();
                }
            } else if ((liveArea.getBalconyCount() == null) && (liveArea.getRecessedBalconyCount() != null)) {
                if (liveArea.getRecessedBalconyCount() > 0) {
                    xmlWriter.writeStartElement("Option");
                    xmlWriter.writeCharacters("Балкон / лоджия");
                    xmlWriter.writeEndElement();
                }
            }

            xmlWriter.writeEndElement();

            xmlWriter.writeEndElement(); //Ad

        }



        xmlWriter.writeEndElement();  //Ads


    }

    public void writeGeneralProperties(XMLStreamWriter xmlWriter , RealtyObject realtyObject) throws Exception {

        xmlWriter.writeStartElement("id");
        xmlWriter.writeCharacters("" + realtyObject.getId());
        xmlWriter.writeEndElement();

        if (realtyObject.getAgent().getPhone() != null) {
            xmlWriter.writeStartElement("ContactPhone");
            xmlWriter.writeCharacters(realtyObject.getAgent().getPhone());
            xmlWriter.writeEndElement();
        }


        if (realtyObject.getAgent().getFio() != null) {
            xmlWriter.writeStartElement("ManagerName");
            xmlWriter.writeCharacters(realtyObject.getAgent().getFio());
            xmlWriter.writeEndElement();
        }

        if (realtyObject.getAddress().getFiasAddressVector().getLevel1()!= null) {
            xmlWriter.writeStartElement("Region");

            String shortRegionName = realtyObject.getAddress().getFiasAddressVector().getLevel1().getSHORTNAME();
            String formalRegionName = realtyObject.getAddress().getFiasAddressVector().getLevel1().getFORMALNAME();

            if ( ( shortRegionName.equals("край") ) || ( shortRegionName.equals("обл") ) ) {

                xmlWriter.writeCharacters(formalRegionName + " " + fiasSocrDAO.findRegionFullName(shortRegionName).getFullName());
                xmlWriter.writeEndElement();
            } else if  (shortRegionName.equals("АО")){
                xmlWriter.writeCharacters(realtyObject.getAddress().getFiasAddressVector().getLevel1().toShortString());
                xmlWriter.writeEndElement();
            }else {
                xmlWriter.writeCharacters(formalRegionName);
                xmlWriter.writeEndElement();
            }
        }

        if (realtyObject.getAddress().getFiasAddressVector().getLevel4() != null) {
            xmlWriter.writeStartElement("City");
            xmlWriter.writeCharacters(realtyObject.getAddress().getFiasAddressVector().getLevel4().getFORMALNAME());
            xmlWriter.writeEndElement();
            if (realtyObject.getAddress().getFiasAddressVector().getLevel7().toShortString() != null) {
                xmlWriter.writeStartElement("Street");
                xmlWriter.writeCharacters(realtyObject.getAddress().getFiasAddressVector().getLevel7().toShortString());
                xmlWriter.writeEndElement();
            }

        }else {

            String cityName = findNearestCity(realtyObject);
            xmlWriter.writeStartElement("City");
            xmlWriter.writeCharacters(cityName);
            xmlWriter.writeEndElement();

            xmlWriter.writeStartElement("Street");
            String fullAddress;
            fullAddress= setAddressToStreetTag(realtyObject);
            xmlWriter.writeCharacters(fullAddress);
            xmlWriter.writeEndElement();

        }

        if (realtyObject.getMetroLocation().getMetroStation()!= null) {
            xmlWriter.writeStartElement("Subway");
            xmlWriter.writeCharacters(realtyObject.getMetroLocation().getMetroStation().getStationName());
            xmlWriter.writeEndElement();
        }

        if (realtyObject.getAddress().getGeoCoordinates().getLongitude() != null) {
            xmlWriter.writeStartElement("Longitude");
            xmlWriter.writeCharacters(realtyObject.getAddress().getGeoCoordinates().getLongitude() + "");
            xmlWriter.writeEndElement();
        }

        if (realtyObject.getAddress().getGeoCoordinates().getLatitude() != null) {
            xmlWriter.writeStartElement("Latitude");
            xmlWriter.writeCharacters("" + realtyObject.getAddress().getGeoCoordinates().getLatitude());
            xmlWriter.writeEndElement();
        }


        if (realtyObject.getAddress().getFiasAddressVector().getLevel3() != null) {
            xmlWriter.writeStartElement("District");
            xmlWriter.writeCharacters(realtyObject.getAddress().getFiasAddressVector().getLevel3().toShortString());
            xmlWriter.writeEndElement();
        }

        if (realtyObject.getArea().getRoomsCount() != null) {
            xmlWriter.writeStartElement("Rooms");
            xmlWriter.writeCharacters(realtyObject.getArea().getRoomsCount() + "");
            xmlWriter.writeEndElement();
        }

        if (realtyObject.getBuilding().getFloorTotal() != null) {
            xmlWriter.writeStartElement("Floors");
            xmlWriter.writeCharacters(realtyObject.getBuilding().getFloorTotal() + "");
            xmlWriter.writeEndElement();
        }

        if (realtyObject.getBuilding().getFloor() != null) {
            xmlWriter.writeStartElement("Floor");
            xmlWriter.writeCharacters(realtyObject.getBuilding().getFloor() + "");
            xmlWriter.writeEndElement();
        }

        DwellingHouse building = (DwellingHouse) realtyObject.getBuilding();
        if (building.getBuildingType() != null) {
            xmlWriter.writeStartElement("HouseType");
            switch (building.getBuildingType()) {
                case BRICK:
                    xmlWriter.writeCharacters("Кирпичный");
                    break;
                case PANEL:
                    xmlWriter.writeCharacters("Панельный");
                    break;
                case BLOCK:
                    xmlWriter.writeCharacters("Блочный");
                    break;
                case WOOD:
                    xmlWriter.writeCharacters("Деревянный");
                    break;
                case MONOLITE:
                    xmlWriter.writeCharacters("Монолитный");
                    break;
            }
            xmlWriter.writeEndElement();
        }


        if (realtyObject.getArea().getTotal() != null) {
            xmlWriter.writeStartElement("Square");
            xmlWriter.writeCharacters("" + realtyObject.getArea().getTotal());
            xmlWriter.writeEndElement();
        }


        if (realtyObject.getPrice().getValue() != null) {
            xmlWriter.writeStartElement("Price");
            xmlWriter.writeCharacters("" + realtyObject.getPrice().getValue());
            xmlWriter.writeEndElement();
        }

        xmlWriter.writeStartElement("PropertyRights");
        xmlWriter.writeCharacters("Посредник");
        xmlWriter.writeEndElement();


        if (realtyObject.getNote() != null) {
            xmlWriter.writeStartElement("Description");
            xmlWriter.writeCharacters(realtyObject.getNote());
            xmlWriter.writeEndElement();
        }


        xmlWriter.writeStartElement("Category");
        xmlWriter.writeCharacters("Квартиры");
        xmlWriter.writeEndElement();

        if (realtyObject.getPhotos() != null) {
            xmlWriter.writeStartElement("images");
            for (int i = 0; i < realtyObject.getPhotos().size(); i++) {

                if (realtyObject.getPhotos().get(i).isAdvertise()) {

                    xmlWriter.writeStartElement("image");
                    xmlWriter.writeAttribute("url", urlLocal.get() + "/photo?id=" + realtyObject.getPhotos().get(i).getId());
                    xmlWriter.writeEndElement(); //image

                }
                if (i == 20) break;
            }
            xmlWriter.writeEndElement(); // images
        }

    }


    /**
     * Функция поиска ближайего города к объекту
     * используется когда у  обьекта не города (для деревень например)
     * будет получен список городов в регионе  ,проверим все эти города на налицие координат
     * отсортирует список по расстоянию от объекта , затем как ближайший возьмем первый элемент
     * из отсортированного списка
     * @param realtyObject загородный обьект для которого будем искать ближайший к нему город
     * @return возвращает название ближайшего к обьекту города
     */
    public String findNearestCity(RealtyObject realtyObject) throws Exception {
        GeoCoordinates coordRealtyObject;               // Гео координаты обьекта недвижимости
        FiasObject targetCity = null;                   // это будет ближацщей город , который мы и хотим найти
        FiasObject region = null;                       // регион , поп которому мы юудем браь выборку городов
        String regionName = "";                         // название треуемого региона

            if (realtyObject.getAddress().getFiasAddressVector().getLevel6()!=null){
                List<FiasObject> cityList;  //сяда мы будем записывать все города региона ,искомого объекта
                String address = "";             //строка адреса по каторой будем искать координаты городов в яндексе

                /*так можно получить id региона */
                String regionCode = realtyObject.getAddress().getFiasAddressVector().getLevel1().getREGIONCODE();
                cityList = fiasObjectDAO.findCityByRegion(regionCode);

                /**
                 * координаты у городов присваиваются при импотре в avito , так что они
                 * Фиас оъекта(города) отсутствуют,если этот город никогда прежде
                 * не рассматривался как кондидат на то  ,чтобы быть ближайшим к населенному пунку при импорте
                 *
                 */
                for(FiasObject city:cityList){

                    address = "";
                    address = address + regionName;
                    /**проверка на наличие координа у Фиас объекта(города)
                     * в случае отсутствия присвоим координаты  ,которые будут получены с помощью
                     * функции getGeoCoordinates класса GeocodeService
                     */
                    if ((city.getLongitude() == null) || (city.getLongitude() == null)) {
                        /**получение координат города.*/
                        address = makeAddress(city, address);

                        coord = geocodeService.getGeoCoordinates(address);
                        city.setLatitude(coord.getLatitude());
                        city.setLongitude(coord.getLongitude());
                        fiasObjectDAO.saveOrUpdate(city);
                    }


                }
                coordRealtyObject = geocodeService.getGeoCoordinates(realtyObject.getAddress().getFiasAddressVector().toString());

                /*выбираем самый близкий к обьекту город*/
                targetCity = Collections.min(cityList,new Comparator<FiasObject>() {
                    @Override
                    public int compare(FiasObject o1, FiasObject o2) {

                        Double distance1;
                        Double distance2;
                        double x = 0;       //квадрат разности коррдинат latitude
                        double y = 0;       //квадрат разности координат longitude

                        x = Math.pow(o1.getLatitude() - coordRealtyObject.getLatitude(), 2);
                        y = Math.pow(o1.getLongitude() - coordRealtyObject.getLongitude(), 2);
                        distance1 = x + y;

                        x = Math.pow(o2.getLatitude() - coordRealtyObject.getLatitude(), 2);
                        y = Math.pow(o2.getLongitude() - coordRealtyObject.getLongitude(), 2);
                        distance2 = x + y;

                        return  (distance1 <= distance2)?-1:1;

                    }
                });


                return targetCity.getFORMALNAME();
            }

        return "";
    }

    /**
     *  для  яндекс поиска координат
     * @param fiasObject FiasObject город
     * @param str        строка из которой будем составлять адрес(уже cодержит регион)
     * @return
     */
    public String makeAddress(FiasObject fiasObject, String str) {
        String address = str;
        FiasObject district = fiasObjectDAO.findByAOGUID(fiasObject.getPARENTGUID());

        if (district.getAOLEVEL() == 1) address = address + "," + fiasObject.toShortString();
        else if (district.getAOLEVEL() == 3) {
            address = address + "," + district.toShortString() + "," + fiasObject.toShortString();
        }
        return address;
    }

    /**составление адреса для avito*/
    public String setAddressToStreetTag(RealtyObject realtyObject) {
        String address = "";
        if (realtyObject.getAddress().getFiasAddressVector().getLevel5() != null) {
            address = address + realtyObject.getAddress().getFiasAddressVector().getLevel5().toShortString() + ",";
        }
        if (realtyObject.getAddress().getFiasAddressVector().getLevel6() != null) {
            address = address + realtyObject.getAddress().getFiasAddressVector().getLevel6().toShortString() + ",";
        }
        if (realtyObject.getAddress().getFiasAddressVector().getLevel7() != null) {
        address = address + realtyObject.getAddress().getFiasAddressVector().getLevel7().toShortString();
        }
        return address;

    }
}