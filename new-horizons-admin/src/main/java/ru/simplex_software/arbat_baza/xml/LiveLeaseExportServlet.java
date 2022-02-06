package ru.simplex_software.arbat_baza.xml;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestHandler;
import ru.simplex_software.arbat_baza.Utils;
import ru.simplex_software.arbat_baza.dao.CianAdminAreaDAO;
import ru.simplex_software.arbat_baza.dao.LiveLeaseRealtyDAO;
import ru.simplex_software.arbat_baza.model.Agent;
import ru.simplex_software.arbat_baza.model.Photo;
import ru.simplex_software.arbat_baza.model.live.DwellingHouse;
import ru.simplex_software.arbat_baza.model.live.LiveArea;
import ru.simplex_software.arbat_baza.model.live.LiveLeaseRealty;
import ru.simplex_software.arbat_baza.model.live.LiveLeaseOptions;
import ru.simplex_software.arbat_baza.model.live.RoomsNumRent;
import ru.simplex_software.arbat_baza.model.price.LiveRentPrice;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static ru.simplex_software.arbat_baza.xml.XmlUtils.notNulAtrr;


/**
 * .
 */

public class LiveLeaseExportServlet implements HttpRequestHandler {
    private static final Logger LOG = LoggerFactory.getLogger(LiveLeaseExportServlet.class);

    @Resource
    private LiveLeaseRealtyDAO liveLeaseRealtyDAO;

    @Resource
    private CianAdminAreaDAO cianAdminAreaDAO;

    List<LiveLeaseRealty> liveRealtyList = null;

    private ThreadLocal urlLocal= new ThreadLocal();


    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        urlLocal.set(Utils.getFullContextUrl(request));
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type","text/xml;charset=UTF-8");
        XmlUtils.outputAgent.set("true".equals(request.getParameter("site")));

        PrintWriter writer = response.getWriter();

        if (request.getServletPath().equals("/external/liveLease.xml")){
            liveRealtyList = liveLeaseRealtyDAO.findToExternalExport();
        }
        if(request.getServletPath().equals("/cian/liveLease.xml")) {
            liveRealtyList = liveLeaseRealtyDAO.findToCianExport();
        }
        if(request.getServletPath().equals("/site/liveLease.xml")){
            liveRealtyList = liveLeaseRealtyDAO.findToSiteExport();
        }
        if(request.getServletPath().equals("/site/flatRent.xml")){
            String[] strId = request.getParameterValues("id");
            Long id = Long.parseLong(strId[0]);
            liveRealtyList = new ArrayList<LiveLeaseRealty>();
            liveRealtyList.add(liveLeaseRealtyDAO.get(id));
        }
        try {
            writeXml(writer,request);
        }catch (Exception ex){
            response.setStatus(500);
            Date date = new Date();
            LOG.error("cian export error at time"+date, ex);
            writer.write("internal error" + date);
        }
    }

    public void writeXml(PrintStream ps,HttpServletRequest request) throws XMLStreamException{
        XMLOutputFactory output = XMLOutputFactory.newInstance();
        XMLStreamWriter xmlWriter= output.createXMLStreamWriter( ps,"UTF-8");

        writeXml(xmlWriter,request);
    }

    public void writeXml(PrintWriter pw, HttpServletRequest request) throws XMLStreamException{

        XMLOutputFactory output = XMLOutputFactory.newInstance();
        XMLStreamWriter xmlWriter= output.createXMLStreamWriter(pw);
        writeXml(xmlWriter,request);

    }

    private void writeXml(XMLStreamWriter rawWriter,HttpServletRequest request) throws XMLStreamException {

        PrettyPrintHandler handler = new PrettyPrintHandler( rawWriter);

        XMLStreamWriter xmlWriter =  (XMLStreamWriter) Proxy.newProxyInstance(
                XMLStreamWriter.class.getClassLoader(),
                new Class[]{XMLStreamWriter.class},
                handler );



        xmlWriter.writeStartDocument("UTF-8", "1.0");
        if(request.getServletPath().equals("/site/flatRent.xml")){
            xmlWriter.writeProcessingInstruction("xml-stylesheet","type=\"text/xsl\" href=\"../flatRent.xsl\"");
        }
        if(request.getServletPath().equals("/site/liveLease.xml")){
            xmlWriter.writeProcessingInstruction("xml-stylesheet","type=\"text/xsl\" href=\"../flatRentList.xsl\"");
        }


        xmlWriter.writeStartElement("flats_rent");

        LOG.debug("realty objects:{}",liveRealtyList.size());
        for(LiveLeaseRealty llr:liveRealtyList){
            xmlWriter.writeStartElement("offer");
            Agent agent = llr.getAgent();
            //id
            {
                xmlWriter.writeStartElement("id");
                xmlWriter.writeCharacters(""+ llr.getId());
                xmlWriter.writeEndElement();
            }
            //agent for site
            XmlUtils.outputAgent(xmlWriter, agent);

            if(llr.getArea()!=null && llr.getArea().getRoomsCount()!=null){
                xmlWriter.writeStartElement("rooms_num");
                xmlWriter.writeCharacters("" + getRoomsNumEnum(llr.getArea().getRoomsCount()).getCianId());
                xmlWriter.writeEndElement();
            }
            LiveArea area = (LiveArea) llr.getArea();
            if(llr.getArea()!=null){//area
                xmlWriter.writeStartElement("area");
                notNulAtrr(xmlWriter, "total",llr.getArea().getTotal());
                notNulAtrr(xmlWriter, "kitchen", area.getKitchen());
                notNulAtrr(xmlWriter, "living", area.getLiving());
                notNulAtrr(xmlWriter, "rooms",llr.getArea().getRooms());
                xmlWriter.writeEndElement();
            }

            if(llr.getPrice()!=null && llr.getPrice().getValue()!= null){
                xmlWriter.writeStartElement("price");
                notNulAtrr(xmlWriter, "currency",llr.getPrice().getCurrency());
                notNulAtrr(xmlWriter, "prepay",((LiveRentPrice)llr.getPrice()).getPrepay());
                notNulAtrr(xmlWriter, "for_day","0");//todo check
                booleanNotNull01(xmlWriter,"deposit",((LiveRentPrice)llr.getPrice()).getDeposit());
                xmlWriter.writeCharacters("" + llr.getPrice().getValue());
                xmlWriter.writeEndElement();
            }
            DwellingHouse building = (DwellingHouse) llr.getBuilding();
            if(llr.getBuilding()!=null &&llr.getBuilding().getFloor()!=null){
                xmlWriter.writeStartElement("floor");
                notNulAtrr(xmlWriter, "total",llr.getBuilding().getFloorTotal());
                if(building.getBuildingType()!= null){
                    notNulAtrr(xmlWriter, "type", building.getBuildingType().getCianId());
                }
                notNulAtrr(xmlWriter, "seria", building.getSeria());
                xmlWriter.writeCharacters("" + building.getFloor());
                xmlWriter.writeEndElement();
            }

            XmlUtils.writePhoneNumbers(xmlWriter, agent);

            //if(com.getAddress()!=null){
            xmlWriter.writeStartElement("address");
            XmlUtils.writeInsieAdress(xmlWriter, llr.getAddress(), cianAdminAreaDAO);
            if(llr.getBuilding()!=null){
                notNulAtrr(xmlWriter,"name_comp", building.getBuildingName());
            }

            xmlWriter.writeEndElement();
            XmlUtils.wrigthMetroToXML(xmlWriter, llr);

            if(llr.getNote()!=null){
                xmlWriter.writeStartElement("note");
                xmlWriter.writeCharacters(llr.getNote());
                xmlWriter.writeEndElement();
            }


            //if(llr.getNote()!=null){
            xmlWriter.writeStartElement("options");
            LiveLeaseOptions liveOptions = (LiveLeaseOptions) llr.getOptions();
            notNulAtrr(xmlWriter, "phone", liveOptions.getPhone());
            notNulAtrr(xmlWriter, "mebel", liveOptions.getMebel());
            notNulAtrr(xmlWriter, "mebel_kitchen", liveOptions.getMebelInKitchen());
            notNulAtrr(xmlWriter, "wm", liveOptions.getWashMashine());

            if (area.getBalconyCount()!=null)notNulAtrr(xmlWriter, "balcon", area.getBalconyCount());
            if(area.getRecessedBalconyCount()!=null)notNulAtrr(xmlWriter, "lodgia",area.getRecessedBalconyCount());
            notNulAtrr(xmlWriter, "rfgr", liveOptions.getRefrigerator());
            notNulAtrr(xmlWriter, "dishwasher",null);//todo
            notNulAtrr(xmlWriter, "conditioner",null);//todo
                notNulAtrr(xmlWriter, "internet",null);//todo
                notNulAtrr(xmlWriter, "tv", liveOptions.getTv());
                notNulAtrr(xmlWriter, "bath",liveOptions.getHaveBath());
                notNulAtrr(xmlWriter, "chute", null);//todo
                notNulAtrr(xmlWriter, "su_s", area.getFullBathroomCount());
                notNulAtrr(xmlWriter, "su_r", area.getSplitBathroomCount());
                notNulAtrr(xmlWriter, "lift_p", area.getLift_p());
                notNulAtrr(xmlWriter, "lift_g", area.getLift_g());
                notNulAtrr(xmlWriter, "windows",null );
                notNulAtrr(xmlWriter, "repair",null );
            notNulAtrr(xmlWriter, "kids", liveOptions.getChildren());
            notNulAtrr(xmlWriter, "pets", liveOptions.getPets());

                xmlWriter.writeEndElement();
            //}


//            xmlWriter.writeStartElement("composition"); todo
//            xmlWriter.writeEndElement();





            if(llr.getCommission()!=null){
                xmlWriter.writeStartElement("com");
                notNulAtrr(xmlWriter,"agent",llr.getCommission().getBuyerCommission());
                notNulAtrr(xmlWriter,"client",llr.getCommission().getSalesCommission());
                xmlWriter.writeEndElement();
            }


            for(Photo ph:llr.getPhotos()){
                if(ph.isAdvertise()) {
                    xmlWriter.writeStartElement("photo");
                    xmlWriter.writeCharacters(urlLocal.get() + "/photo?id=" + ph.getId());
                    xmlWriter.writeEndElement();
                }
            }
            if(llr.isPremiumInCian()){
                xmlWriter.writeStartElement("promotions");
                xmlWriter.writeStartElement("promotion");
                xmlWriter.writeCharacters("premium");
                xmlWriter.writeEndElement();
                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndElement();//offer
        }
        xmlWriter.writeEndElement();//commerce

        xmlWriter.flush();
        xmlWriter.close();
    }

    private boolean hasMetroInformation(LiveLeaseRealty llRealty) {
        return llRealty.getMetroLocation().getMetroStation()!=null&&llRealty.getMetroLocation().getMetroStation().getId()!=null;
    }

    public void booleanNotNull01(XMLStreamWriter writer, String attr,Boolean val)throws XMLStreamException{
        if(val==null){
            return;
        }
        writer.writeAttribute(attr, val?"1":"0");

    }

    public RoomsNumRent getRoomsNumEnum(int rooms){
        switch(rooms) {
            case 1:
                return RoomsNumRent.ROOMS_1;
            case 2:
                return RoomsNumRent.ROOMS_2;
            case 3:
                return RoomsNumRent.ROOMS_3;
            case 4:
                return RoomsNumRent.ROOMS_4;
            case 5:
                return RoomsNumRent.ROOMS_5;
            case 6:
                return RoomsNumRent.ROOMS_MANY;
            default:
                return RoomsNumRent.ROOM_ONLY;
        }
    }
}
