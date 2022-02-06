package ru.simplex_software.arbat_baza;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import ru.simplex_software.arbat_baza.dao.fias.FiasAddressVectorDAO;
import ru.simplex_software.arbat_baza.model.Address;
import ru.simplex_software.arbat_baza.model.GeoCoordinates;
import ru.simplex_software.arbat_baza.model.fias.FiasAddressVector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * .
 */

public class GeocodeService {
    private static final Logger LOG = LoggerFactory.getLogger(GeocodeService.class);
    private FiasAddressVector fiasAddressVector = null;
    private String geocodeBaseUrl="https://geocode-maps.yandex.ru/1.x/?geocode=";


    public GeoCoordinates getGeoCoordinates(String address) throws Exception{
        GeoCoordinates geoCoordinates = new GeoCoordinates() ;

        String geocodeUrl = address;

        geocodeUrl=geocodeBaseUrl+URLEncoder.encode(geocodeUrl);

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(geocodeUrl);
        CloseableHttpResponse response1 = httpclient.execute(httpGet);
        try {

            HttpEntity entity1 = response1.getEntity();
            InputStream content = entity1.getContent();


            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(content);

            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xPath = xpathFactory.newXPath();

            String nodeListXpath="/ymaps/GeoObjectCollection/featureMember";
            NodeList nodeList = (NodeList)xPath.evaluate(nodeListXpath, doc.getDocumentElement(), XPathConstants.NODESET);
            String coordXpath="GeoObject/Point/pos/text()";
            String coord= (String)xPath.evaluate(coordXpath, nodeList.item(0), XPathConstants.STRING);
            geoCoordinates.setLatitude(Double.parseDouble(coord.split(" ")[1]));
            geoCoordinates.setLongitude(Double.parseDouble(coord.split(" ")[0]));

        } finally {
            response1.close();
        }


        return geoCoordinates;
    }


    public Map<String, GeoCoordinates> getCoordinates(Address address) throws Exception{

        HashMap<String, GeoCoordinates> map = new HashMap<>();
        String geocodeUrl="Москва, ";
        if(address.getFiasAddressVector().getLevel1()==null) {
            if (address.getStreet() != null) {
                geocodeUrl = geocodeUrl + address.getStreet().getStreetName();
            }
            if (address.getHouse_str() != null) {
                geocodeUrl = geocodeUrl + ", дом " + address.getHouse_str();
            }
        }
        else {
            geocodeUrl = address.getFiasAddressVector().toString() +",дом "+address.getHouse_str();
        }

        geocodeUrl=geocodeBaseUrl+URLEncoder.encode(geocodeUrl);

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(geocodeUrl);
        CloseableHttpResponse response1 = httpclient.execute(httpGet);
        try {

            HttpEntity entity1 = response1.getEntity();
            InputStream content = entity1.getContent();


            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(content);


            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xPath = xpathFactory.newXPath();

            String nodeListXpath="/ymaps/GeoObjectCollection/featureMember";
            NodeList nodeList = (NodeList)xPath.evaluate(nodeListXpath, doc.getDocumentElement(), XPathConstants.NODESET);
            for(int i=0;i<nodeList.getLength();i++){

                String addrXpath="GeoObject/metaDataProperty/GeocoderMetaData/AddressDetails/Country/AddressLine/text()";
                String addr = (String)xPath.evaluate(addrXpath, nodeList.item(i), XPathConstants.STRING);


                String coordXpath="GeoObject/Point/pos/text()";
                String coord= (String)xPath.evaluate(coordXpath, nodeList.item(i), XPathConstants.STRING);
                Double lat=Double.parseDouble(coord.split(" ")[1]);
                Double lng=Double.parseDouble(coord.split(" ")[0]);
                map.put(addr, new GeoCoordinates(lat, lng));


            }

        } finally {
            response1.close();
        }

        return map;
    }

    public void setFiasAddressVector(FiasAddressVector fiasAddressVector) {
        this.fiasAddressVector = fiasAddressVector;
    }

}
