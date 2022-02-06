package ru.simplex_software.ord.servlets.xml_export;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestHandler;
import org.w3c.dom.Document;
import ru.simplex_software.ord.utils.XmlBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

public class CianCommercialLeaseOfficeServlet implements HttpRequestHandler {

    private static final Logger LOG = LoggerFactory.getLogger(XmlBuilder.class);

    @Autowired
    private XmlBuilder xmlBuilder;

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {

            // Настройка ответа.
            response.setHeader("Content-Type", "text/xml;charset=UTF-8");

            // Построение документа.
            Document document = xmlBuilder.buildCianCommercialLeaseOfficeXml();

            // Запись документа в поток.
            xmlBuilder.writeXmlToStream(document, response.getOutputStream());

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            response.setStatus(SC_INTERNAL_SERVER_ERROR);
            response.getOutputStream().print(e.getMessage());
        }
    }
}
