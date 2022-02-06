package ru.simplex_software.ord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestHandler;
import ru.simplex_software.arbat_baza.dao.odor.ExternalAgencyDAO;
import ru.simplex_software.arbat_baza.model.odor.ExternalAgency;
import ru.simplex_software.arbat_baza.model.odor.Feed;
import ru.simplex_software.arbat_baza.xml.PrettyPrintHandler;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Proxy;


/**
 * .
 */
public class FriendsServlet implements HttpRequestHandler {
    private static final Logger LOG = LoggerFactory.getLogger(FriendsServlet.class);
    @Resource
    private ExternalAgencyDAO externalAgencyDAO;



    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "text/xml;charset=UTF-8");
        try {
            PrintWriter writer = response.getWriter();
            XMLOutputFactory output = XMLOutputFactory.newInstance();
            XMLStreamWriter xmlWriter = output.createXMLStreamWriter(writer);

            PrettyPrintHandler handler = new PrettyPrintHandler(xmlWriter);

            XMLStreamWriter xmlWriter2 = (XMLStreamWriter) Proxy.newProxyInstance(
                    XMLStreamWriter.class.getClassLoader(),
                    new Class[]{XMLStreamWriter.class},
                    handler);

            xmlWriter2.writeStartDocument("UTF-8", "1.0");

            xmlWriter2.writeStartElement("friends");

            for(ExternalAgency ag:externalAgencyDAO.findFriends()){
                xmlWriter2.writeStartElement("friend");
                    xmlWriter2.writeStartElement("name");
                        xmlWriter2.writeCharacters(ag.getName());
                    xmlWriter2.writeEndElement();

                    xmlWriter2.writeStartElement("url");
                        xmlWriter2.writeCharacters(ag.getSite());
                    xmlWriter2.writeEndElement();

                xmlWriter2.writeStartElement("feeds");
                for(Feed feed:ag.getFeedList()){
                    xmlWriter2.writeStartElement("feed");
                    {
                        xmlWriter2.writeStartElement("feedType");
                        xmlWriter2.writeCharacters(feed.getFeedType().name());
                        xmlWriter2.writeEndElement();

                        xmlWriter2.writeStartElement("url");
                        xmlWriter2.writeCharacters(feed.getUrl());
                        xmlWriter2.writeEndElement();


                        xmlWriter2.writeStartElement("description");
                        xmlWriter2.writeCharacters(feed.getDescription());
                        xmlWriter2.writeEndElement();
                    }
                    xmlWriter2.writeEndElement();
                }
                xmlWriter2.writeEndElement();


                xmlWriter2.writeEndElement();

            }

            xmlWriter2.writeEndElement();
        } catch (Exception e) {
            LOG.error(e.getMessage(),e);
        }


    }
}
