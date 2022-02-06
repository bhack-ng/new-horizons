package ru.simplex_software.arbat_baza.xml;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestHandler;
import ru.simplex_software.arbat_baza.Utils;
import ru.simplex_software.arbat_baza.dao.CianAdminAreaDAO;
import ru.simplex_software.arbat_baza.dao.LiveSaleRealtyDAO;
import ru.simplex_software.arbat_baza.model.Photo;
import ru.simplex_software.arbat_baza.model.commerce.Commerce;
import ru.simplex_software.arbat_baza.model.live.DwellingHouse;
import ru.simplex_software.arbat_baza.model.live.LiveArea;
import ru.simplex_software.arbat_baza.model.live.LiveLeaseRealty;
import ru.simplex_software.arbat_baza.model.live.LiveSaleOptions;
import ru.simplex_software.arbat_baza.model.live.LiveSaleRealty;
import ru.simplex_software.arbat_baza.model.live.RoomsNumRent;

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

public class LiveSaleExportServlet implements HttpRequestHandler {
    private static final Logger LOG = LoggerFactory.getLogger(LiveSaleExportServlet.class);

    @Resource
    private LiveSaleRealtyDAO liveSaleRealtyDAO;
    @Resource
    private CianAdminAreaDAO cianAdminAreaDAO;
    List<LiveSaleRealty> liveRealtyList;

    private ThreadLocal urlLocal = new ThreadLocal();

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        urlLocal.set(Utils.getFullContextUrl(request));
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "text/xml;charset=UTF-8");
        XmlUtils.outputAgent.set("true".equals(request.getParameter("site")));
        PrintWriter writer = response.getWriter();

        if (request.getServletPath().equals("/external/liveSale.xml")) {
            liveRealtyList = liveSaleRealtyDAO.findToExternalExport();
        }
        else if (request.getServletPath().equals("/cian/liveSale.xml")) {
            liveRealtyList = liveSaleRealtyDAO.findToCianExport();
        }
        else if (request.getServletPath().equals("/site/liveSale.xml")){
            liveRealtyList = liveSaleRealtyDAO.findToSiteExport();
        }
        else if(request.getServletPath().equals("/site/flatSale.xml")){
            String[] strId = request.getParameterValues("id");
            Long id = Long.parseLong(strId[0]);
            liveRealtyList = new ArrayList<LiveSaleRealty>();
            liveRealtyList.add(liveSaleRealtyDAO.get(id));
        }
        try {
            writeXml(writer,request);
        } catch (Exception ex) {
            response.setStatus(500);
            Date date = new Date();
            LOG.error("cian export error at time" + date, ex);
            writer.write("internal error" + date);
        }
    }


    public void writeXml(PrintStream ps,HttpServletRequest request) throws XMLStreamException {
        XMLOutputFactory output = XMLOutputFactory.newInstance();
        XMLStreamWriter xmlWriter = output.createXMLStreamWriter(ps, "UTF-8");

        writeXml(xmlWriter,request);
    }

    public void writeXml(PrintWriter pw,HttpServletRequest request) throws XMLStreamException {

        XMLOutputFactory output = XMLOutputFactory.newInstance();
        XMLStreamWriter xmlWriter = output.createXMLStreamWriter(pw);
        writeXml(xmlWriter,request);

    }

    private void writeXml(XMLStreamWriter rawWriter,HttpServletRequest request) throws XMLStreamException {

        PrettyPrintHandler handler = new PrettyPrintHandler(rawWriter);

        XMLStreamWriter xmlWriter = (XMLStreamWriter) Proxy.newProxyInstance(
                XMLStreamWriter.class.getClassLoader(),
                new Class[]{XMLStreamWriter.class},
                handler);


        xmlWriter.writeStartDocument("UTF-8", "1.0");

        if(request.getServletPath().equals("/site/flatSale.xml")){
            xmlWriter.writeProcessingInstruction("xml-stylesheet","type=\"text/xsl\" href=\"../flatSale.xsl\"");
        }
        if(request.getServletPath().equals("/site/liveSale.xml")){
            xmlWriter.writeProcessingInstruction("xml-stylesheet","type=\"text/xsl\" href=\"../flatSaleList.xsl\"");
        }

        xmlWriter.writeStartElement("flats_for_sale");

        for (LiveSaleRealty lsr : liveRealtyList) {
            xmlWriter.writeStartElement("offer");

            //id
            {
                xmlWriter.writeStartElement("id");
                xmlWriter.writeCharacters("" + lsr.getId());
                xmlWriter.writeEndElement();
            }
            //agent for site
            XmlUtils.outputAgent(xmlWriter, lsr.getAgent());
            if (lsr.getArea() != null && lsr.getArea().getRoomsCount() != null) {
                xmlWriter.writeStartElement("rooms_num");
                xmlWriter.writeCharacters("" + RoomsNumRent.findByCianId(lsr.getArea().getRoomsCount()).getCianId());
                xmlWriter.writeEndElement();
            }
            LiveArea area = (LiveArea) lsr.getArea();
            if (lsr.getArea() != null) {//area
                xmlWriter.writeStartElement("area");
                notNulAtrr(xmlWriter, "total", lsr.getArea().getTotal());
                notNulAtrr(xmlWriter, "kitchen", area.getKitchen());
                notNulAtrr(xmlWriter, "living", area.getLiving());
                notNulAtrr(xmlWriter, "rooms", lsr.getArea().getRooms());
                xmlWriter.writeEndElement();
            }

            if (lsr.getPrice() != null && lsr.getPrice().getValue() != null) {
                xmlWriter.writeStartElement("price");
                notNulAtrr(xmlWriter, "currency", lsr.getPrice().getCurrency());
                xmlWriter.writeCharacters("" + lsr.getPrice().getValue());
                xmlWriter.writeEndElement();
            }
            DwellingHouse building = (DwellingHouse) lsr.getBuilding();
            if (lsr.getBuilding() != null && lsr.getBuilding().getFloor() != null) {
                xmlWriter.writeStartElement("floor");
                notNulAtrr(xmlWriter, "total", lsr.getBuilding().getFloorTotal());
                notNulAtrr(xmlWriter, "type", building.getBuildingType());
                notNulAtrr(xmlWriter, "seria", building.getSeria());
                xmlWriter.writeCharacters("" + lsr.getBuilding().getFloor());
                xmlWriter.writeEndElement();
            }

            XmlUtils.writePhoneNumbers(xmlWriter, lsr.getAgent());

            //if(com.getAddress()!=null){
            xmlWriter.writeStartElement("address");
            XmlUtils.writeInsieAdress(xmlWriter, lsr.getAddress(), cianAdminAreaDAO);
            if (lsr.getBuilding() != null) {
                notNulAtrr(xmlWriter, "na   me_comp", building.getBuildingName());
            }

            xmlWriter.writeEndElement();
            //}


            XmlUtils.wrigthMetroToXML(xmlWriter, lsr);


            if (lsr.getNote() != null) {
                xmlWriter.writeStartElement("note");
                xmlWriter.writeCharacters(lsr.getNote());
                xmlWriter.writeEndElement();
            }


            //if(llr.getNote()!=null){
            xmlWriter.writeStartElement("options");
            LiveSaleOptions liveOptions = (LiveSaleOptions) lsr.getOptions();
            notNulAtrr(xmlWriter, "object_type", lsr.getBuildingTypeSale().getCianId());
            notNulAtrr(xmlWriter, "sale_type", lsr.getSaleType().getCianId());

            notNulAtrr(xmlWriter, "phone", liveOptions.getPhone());
            notNulAtrr(xmlWriter, "lift_p",area.getLift_p());
            notNulAtrr(xmlWriter, "lift_g",area.getLift_g());

            notNulAtrr(xmlWriter, "balcon", area.getBalconyCount());
            if (area.getRecessedBalconyCount() != null) {
                notNulAtrr(xmlWriter, "lodgia", area.getRecessedBalconyCount());
            }
            notNulAtrr(xmlWriter, "su_s", area.getFullBathroomCount());
            notNulAtrr(xmlWriter, "su_r", area.getSplitBathroomCount());

            notNulAtrr(xmlWriter, "windows", liveOptions.getWindowsPosition());
            notNulAtrr(xmlWriter, "ipoteka", liveOptions.getHomeMortgage());
            notNulAtrr(xmlWriter, "chute", null);//not found in GUI
            notNulAtrr(xmlWriter, "repair", null);//not found in GUI
            notNulAtrr(xmlWriter, "finishing", null);//not found in GUI
            xmlWriter.writeEndElement();
            //}


//            if(lsr.getCommission()!=null){
//                xmlWriter.writeStartElement("com");
//                notNulAtrr(xmlWriter,"agent",lsr.getCommission().getBuyerCommission());
//                notNulAtrr(xmlWriter,"client",lsr.getCommission().getSalesCommission());
//                xmlWriter.writeEndElement();
//


            for (Photo ph : lsr.getPhotos()) {
                if (ph.isAdvertise()) {
                    xmlWriter.writeStartElement("photo");
                    xmlWriter.writeCharacters(urlLocal.get() + "/photo?id=" + ph.getId());
                    xmlWriter.writeEndElement();
                }
            }
            if (lsr.isPremiumInCian()) {
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

}
