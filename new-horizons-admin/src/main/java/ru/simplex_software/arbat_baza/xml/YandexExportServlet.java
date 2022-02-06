package ru.simplex_software.arbat_baza.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestHandler;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class YandexExportServlet implements HttpRequestHandler {
    private static final Logger LOG = LoggerFactory.getLogger(YandexExportServlet.class);

    @Resource
    private YandexLeaseExportServlet yandexLeaseExportServlet;

    @Resource
    private YandexSaleExportServlet yandexSaleExportServlet;

    @Resource
    private YandexCommerceServlet yandexCommerceServlet;

    @Resource
    private YandexPrivateHouseServlet yandexPrivateHouseServlet;
    @Resource
    private YandexSteadServlet yandexSteadServlet;
    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();

        try {
            XMLOutputFactory outputXML = XMLOutputFactory.newInstance();
            XMLStreamWriter xmlWriter = outputXML.createXMLStreamWriter(writer);
            //печать заголовка документа
            xmlWriter.writeStartDocument("UTF-8", "1.0");
            //печать начала фида
            xmlWriter.writeStartElement("realty-feed"); //<realty-feed>

            xmlWriter.writeDefaultNamespace("http://webmaster.yandex.ru/schemas/feed/realty/2010-06");
            //печать времени создания фида
            xmlWriter.writeStartElement("generation-date");
            xmlWriter.writeCharacters(ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
            xmlWriter.writeEndElement();
            yandexLeaseExportServlet.handleRequest(request, response);
            yandexSaleExportServlet.handleRequest(request, response);
            yandexCommerceServlet.handleRequest(request, response);

            yandexPrivateHouseServlet.handleRequest(request, response);
            yandexSteadServlet.handleRequest(request, response);
            //печать окончания фида
            xmlWriter.writeEndElement();

            xmlWriter.close();

        } catch (XMLStreamException ex) {
            response.setStatus(500);
            Date date = new Date();
            LOG.error("export error at time" + date, ex);
            writer.write("internal error" + date);
        }
    }
}//end servlet
