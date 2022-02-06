package ru.simplex_software.arbat_baza.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestHandler;
import ru.simplex_software.arbat_baza.Utils;
import ru.simplex_software.arbat_baza.dao.CianAdminAreaDAO;
import ru.simplex_software.arbat_baza.dao.commerce.CommerceDAO;
import ru.simplex_software.arbat_baza.model.Photo;
import ru.simplex_software.arbat_baza.model.commerce.Commerce;
import ru.simplex_software.arbat_baza.model.fias.FiasObject;
import ru.simplex_software.arbat_baza.model.price.CommercePrice;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.Proxy;
import java.util.Date;
import java.util.List;

import static ru.simplex_software.arbat_baza.xml.XmlUtils.notNulAtrr;

/**
 * XML фид коммерции для ЦИАНа и внешних агенств.
 */
public class CommercialExportServlet implements HttpRequestHandler {
    private static final Logger LOG = LoggerFactory.getLogger(CommercialExportServlet.class);

    @Resource
    private CommerceDAO commerceDAO;

    @Resource
    private CianAdminAreaDAO cianAdminAreaDAO;

    private List<Commerce> allCommerce = null;
    private ThreadLocal urlLocal = new ThreadLocal();
    private boolean externalFeed = false;

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String urlStr = Utils.getFullContextUrl(request);

        urlLocal.set(urlStr);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "text/xml;charset=UTF-8");
        XmlUtils.outputAgent.set("true".equals(request.getParameter("site")));
        PrintWriter writer = response.getWriter();

        // Загрузка списка коммерции.
        if (request.getServletPath().equals("/external/commerical.xml")) {
            allCommerce = commerceDAO.findToExternalExport();
            externalFeed = true;
        }
        if (request.getServletPath().equals("/cian/commerical.xml")) {
            allCommerce = commerceDAO.findToCianExport();
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

    /**
     * Запись XML.
     */
    public void writeXml(PrintStream ps) throws XMLStreamException {
        XMLOutputFactory output = XMLOutputFactory.newInstance();
        XMLStreamWriter xmlWriter = output.createXMLStreamWriter(ps, "UTF-8");
        writeXml(xmlWriter);
    }

    /**
     * Запись XML.
     */
    public void writeXml(PrintWriter pw) throws XMLStreamException {
        XMLOutputFactory output = XMLOutputFactory.newInstance();
        XMLStreamWriter xmlWriter = output.createXMLStreamWriter(pw);
        writeXml(xmlWriter);
    }

    /**
     * Запись XML.
     */
    private void writeXml(XMLStreamWriter rawWriter) throws XMLStreamException {

        PrettyPrintHandler handler = new PrettyPrintHandler(rawWriter);

        XMLStreamWriter xmlWriter = (XMLStreamWriter) Proxy.newProxyInstance(
                XMLStreamWriter.class.getClassLoader(),
                new Class[]{XMLStreamWriter.class},
                handler);

        xmlWriter.writeStartDocument("UTF-8", "1.0");

        // Корневой элемент.
        xmlWriter.writeStartElement("commerce");

        // Запись предложений.
        for (Commerce com : allCommerce) {

            // Предложение.
            xmlWriter.writeStartElement("offer");

            // Id.
            xmlWriter.writeStartElement("id");
            xmlWriter.writeCharacters("ab2_" + com.getId());
            xmlWriter.writeEndElement();

            // Агент.
            XmlUtils.outputAgent(xmlWriter, com.getAgent());

            // Тип коммерции.
            if (com.getType() != null) {
                xmlWriter.writeStartElement("commerce_type");
                xmlWriter.writeCharacters("" + com.getType().getCianChars());
                xmlWriter.writeEndElement();
            }

            // Тип контракта.
            if (com.getContractType() != null) {
                xmlWriter.writeStartElement("contract_type");
                xmlWriter.writeCharacters("" + com.getContractType().getCianId());
                xmlWriter.writeEndElement();
            }

            // Площадь.
            if (com.getArea() != null) {
                xmlWriter.writeStartElement("area");
                notNulAtrr(xmlWriter, "rooms", com.getArea().getRooms());
                notNulAtrr(xmlWriter, "rooms_count", com.getArea().getRoomsCount());
                notNulAtrr(xmlWriter, "total", com.getArea().getTotal());
                xmlWriter.writeEndElement();
            }

            // Цена.
            if (com.getPrice() != null && com.getPrice().getValue() != null) {
                xmlWriter.writeStartElement("price");
                notNulAtrr(xmlWriter, "period", ((CommercePrice) com.getPrice()).getPeriod());
                notNulAtrr(xmlWriter, "currency", com.getPrice().getCurrency());
                xmlWriter.writeCharacters("" + com.getPrice().getValue());
                xmlWriter.writeEndElement();
            }

            // Строение.
            if (com.getBuilding() != null) {
                xmlWriter.writeStartElement("building");
                notNulAtrr(xmlWriter, "type", com.getBuilding().getLiveType());
                notNulAtrr(xmlWriter, "enter", com.getBuilding().getEnter());
                notNulAtrr(xmlWriter, "floor", com.getBuilding().getFloor());
                notNulAtrr(xmlWriter, "floor_total", com.getBuilding().getFloorTotal());
                notNulAtrr(xmlWriter, "name_bc", com.getBuilding().getName_bc());
                notNulAtrr(xmlWriter, "status_b", com.getBuilding().getStatus_b());
                notNulAtrr(xmlWriter, "class", com.getBuilding().getBuildingClass());
                notNulAtrr(xmlWriter, "area_b", com.getBuilding().getArea_b());
                notNulAtrr(xmlWriter, "type_b", com.getBuilding().getBuildingType());
                notNulAtrr(xmlWriter, "ceiling", com.getBuilding().getCeiling());
                xmlWriter.writeEndElement();
            }

            // Номера телефонов.
            XmlUtils.writePhoneNumbers(xmlWriter, com.getAgent());

            // Комиссия.
            if (com.getCommission() != null) {
                xmlWriter.writeStartElement("com");
                notNulAtrr(xmlWriter, "agent", com.getCommission().getBuyerCommission());
                notNulAtrr(xmlWriter, "client", com.getCommission().getSalesCommission());
                xmlWriter.writeEndElement();
            }

            // Адрес.
            xmlWriter.writeStartElement("address");
            XmlUtils.writeInsieAdress(xmlWriter, com.getAddress(), cianAdminAreaDAO);
            if (externalFeed) { // Запись fiasId только при экспорте на сайт.
                FiasObject lastLevel = com.getAddress().getFiasAddressVector().getLastLevel();
                if (lastLevel != null) {
                    xmlWriter.writeAttribute("fiasId", lastLevel.getAOGUID());
                }
            }
            xmlWriter.writeEndElement();

            // Метро.
            XmlUtils.wrigthMetroToXML(xmlWriter, com);

            // Опции.
            if (com.getOptions() != null) {
                xmlWriter.writeStartElement("options");
                XmlUtils.booleanNotNull01(xmlWriter, "add_phones", com.getOptions().getAdd_phones());
                notNulAtrr(xmlWriter, "mebel", com.getOptions().getMebel());
                notNulAtrr(xmlWriter, "status", com.getBuilding().getStatus_b());
                notNulAtrr(xmlWriter, "elect", com.getOptions().getElect());
                notNulAtrr(xmlWriter, "heat", com.getOptions().getHeat());
                notNulAtrr(xmlWriter, "canal", com.getOptions().getCanal());
                notNulAtrr(xmlWriter, "gas", com.getOptions().getGas());
                notNulAtrr(xmlWriter, "water", com.getOptions().getWater());
                notNulAtrr(xmlWriter, "lift", com.getOptions().getLift());
                notNulAtrr(xmlWriter, "parking", com.getOptions().getParking());
                notNulAtrr(xmlWriter, "security", com.getOptions().getSecurity());
                notNulAtrr(xmlWriter, "internet", com.getOptions().getInternet());
                xmlWriter.writeEndElement();
            }

            // Заметка.
            if (com.getNote() != null) {
                xmlWriter.writeStartElement("note");
                xmlWriter.writeCharacters(com.getNote());
                xmlWriter.writeEndElement();
            }

            // Фотография.
            for (Photo ph : com.getPhotos()) {
                if (ph.isAdvertise()) {
                    xmlWriter.writeStartElement("photo");
                    xmlWriter.writeCharacters(urlLocal.get() + "/photo?id=" + ph.getId());
                    xmlWriter.writeEndElement();
                }
            }

            // Премиум объявление ЦИАН.
            if (com.isPremiumInCian()) {
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
