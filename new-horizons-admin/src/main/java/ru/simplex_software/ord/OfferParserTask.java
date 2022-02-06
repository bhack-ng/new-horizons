package ru.simplex_software.ord;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ru.simplex_software.arbat_baza.dao.fias.FiasObjectDAO;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.arbat_baza.model.commerce.Commerce;
import ru.simplex_software.arbat_baza.model.fias.FiasObject;
import ru.simplex_software.arbat_baza.model.live.LiveLeaseRealty;
import ru.simplex_software.arbat_baza.model.live.LiveSaleRealty;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.util.List;

/**
 * extract data from  part of yandex realty  xml (offer element) and save to database
 */
public class OfferParserTask implements Runnable {
    private static final Logger LOG= LoggerFactory.getLogger(OfferParserTask.class);
    @Resource
    private FiasObjectDAO fiasObjectDAO;
    private byte[] xml;
    public OfferParserTask(byte[] xml) {
        this.xml=xml;
    }

    @Override
    public void run() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();


            ByteArrayInputStream input =  new ByteArrayInputStream(xml);
            Document doc = builder.parse(input);
            final Element root = doc.getDocumentElement();


            RealtyObject realtyObject=createRealtyObject(root);
            setAddress( realtyObject, root);


        } catch (Exception e) {
            LOG.info(e.getMessage(),e);
        }
    }
    private void setAddress(RealtyObject realtyObject, Element root){
        XPath xPath = XPathFactory.newInstance().newXPath();
        String regionStr="";
        try {
            regionStr= (String)xPath.evaluate("/offer/region", root, XPathConstants.STRING);
        } catch (XPathExpressionException e) {
            LOG.info(e.getMessage(),e);
        }
        final List<FiasObject> fiasRegionList = fiasObjectDAO.findRegion();
        final FiasObject fiasRegion = findBest(regionStr, fiasRegionList);
        realtyObject.getAddress().getFiasAddressVector().setLevel1(fiasRegion );

    }

    private FiasObject findBest(String regionStr, List<FiasObject> fiasRegions) {
        int minDistance=Integer.MAX_VALUE;
        FiasObject similarObject=null;
        for(FiasObject o:fiasRegions){
            similarObject=o;
            int distance= StringUtils.getLevenshteinDistance(regionStr, o.getOFFNAME());
            if(minDistance>distance){
                minDistance=distance;
                similarObject=o;
            }

        }
        return similarObject;
    }

    private RealtyObject  createRealtyObject( Element root){
        RealtyObject realtyObject;

        String propertyType=null;
        String type=null;
        String commercialType=null;
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            propertyType = (String)xPath.evaluate("/offer/property-type", root, XPathConstants.STRING);
            type = (String)xPath.evaluate("/offer/type", root, XPathConstants.STRING);
            commercialType = (String)xPath.evaluate("/offer/commercial-type", root, XPathConstants.STRING);
        } catch (XPathExpressionException e) {
            LOG.info(e.getMessage(),e);
        }


        if("жилая".equalsIgnoreCase(propertyType)){
            if("продажа".equalsIgnoreCase(type)){
                realtyObject= new LiveSaleRealty();
                return realtyObject;
            }else if ("аренда".equalsIgnoreCase(type)){
                realtyObject= new LiveLeaseRealty();
                return realtyObject;
            }else{
                throw new RuntimeException();
            }
        }if(commercialType!=null){
            realtyObject=new Commerce();
            return realtyObject;
        }

        throw new IncorrectYandexRealtyXml("Unknow realty type in xml");
    }
}
