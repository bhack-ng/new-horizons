package ru.simplex_software.ord.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ru.simplex_software.arbat_baza.dao.commerce.CommerceDAO;
import ru.simplex_software.arbat_baza.model.commerce.Commerce;

import javax.servlet.ServletOutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.util.List;

public class XmlBuilder {

    @Autowired
    private CommerceDAO commerceDAO;

    /**
     * Создание XML для ЦИАНа об аренде офисов.
     */
    public Document buildCianCommercialLeaseOfficeXml() throws ParserConfigurationException {

        List<Commerce> commerces = commerceDAO.findToCianExport();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        // Корневой элемент фида.
        Document document = builder.newDocument();
        Element feed = document.createElement("feed");
        document.appendChild(feed);

        // Версия фида.
        Element feedVersion = document.createElement("feed_version");
        feedVersion.setTextContent("2");
        feed.appendChild(feedVersion);

        for (Commerce commerce : commerces) {

            // Корневой элемент объекта.
            Element object = document.createElement("object");
            feed.appendChild(object);

            // Категория объекта.
            Element category = document.createElement("Category");
            category.setTextContent("officeRent");
            object.appendChild(category);

            // ExternalId объекта.
            Element externalId = document.createElement("ExternalId");
            externalId.setTextContent(commerce.getId().toString());
            object.appendChild(externalId);

            // Адрес объекта.
            Element address = document.createElement("Address");
            String addressStr = commerce.getAddress().getLocality() + ", " +
                    commerce.getAddress().getStreet() + ", " + commerce.getAddress().getKvartira();
            address.setTextContent(addressStr);
            object.appendChild(address);

            // Информация о теклефонных номерах.
            Element phones = document.createElement("Phones");
            object.appendChild(phones);

            // Информация о теклефонном номере.
            Element phoneSchema = document.createElement("PhoneSchema");
            phones.appendChild(phoneSchema);

            // Код строаны номера.
            Element countryCode = document.createElement("CountryCode");
            countryCode.setTextContent("+7");
            phoneSchema.appendChild(countryCode);

            // Телефонный номер.
            Element number = document.createElement("Number");
            number.setTextContent(commerce.getAgent().getPhone());
            phoneSchema.appendChild(number);

            // Общая площадь объекта.
            Element totalArea = document.createElement("TotalArea");
            totalArea.setTextContent(commerce.getArea().getTotal().toString());
            object.appendChild(totalArea);

            // Этаж объекта.
            Element floorNumber = document.createElement("FloorNumber");
            floorNumber.setTextContent(commerce.getBuilding().getFloor().toString());
            object.appendChild(floorNumber);

            // Информация о здании.
            Element building = document.createElement("Building");
            object.appendChild(building);

            // Количество этажей в здании.
            Element floorsCount = document.createElement("FloorsCount");
            floorsCount.setTextContent(commerce.getBuilding().getFloorTotal().toString());
            building.appendChild(floorsCount);

            // Условия сделки.
            Element bargainTerms = document.createElement("BargainTerms");
            object.appendChild(bargainTerms);

            // Цена объекта.
            Element price = document.createElement("Price");
            price.setTextContent(commerce.getPrice().getValue().toString());
            bargainTerms.appendChild(price);
        }

        return document;
    }

    /**
     * Запись XML документа в поток.
     */
    public void writeXmlToStream(Document document, ServletOutputStream stream) throws TransformerException {

        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();

        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(stream);

        transformer.transform(source, result);
    }
}
