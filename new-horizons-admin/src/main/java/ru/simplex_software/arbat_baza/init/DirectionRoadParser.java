package ru.simplex_software.arbat_baza.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import ru.simplex_software.arbat_baza.dao.DirectionRoadDao;
import ru.simplex_software.arbat_baza.model.DirectionRoad;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * .
 */
public class DirectionRoadParser {
    private static final Logger LOG = LoggerFactory.getLogger(DirectionRoadParser.class);
    private ArrayList<DirectionRoad> directionRoadList  = new ArrayList<DirectionRoad>();
    XPath xPath = XPathFactory.newInstance().newXPath();

    @Resource
    private DirectionRoadDao directionRoadDao;

    public void parsXML(){
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputStream is = getClass().getClassLoader().getResourceAsStream("location.xml");
            Document doc = builder.parse(is);
            final Element root = doc.getDocumentElement();
            parse(root);

        }catch (Exception e){

        }
    }

    public void parse(Element root)  {
        try {

            NodeList nodeList = (NodeList) xPath.evaluate("//DirectionRoad", root, XPathConstants.NODESET);

            for (int i=0;i<nodeList.getLength();i++){
                DirectionRoad directionRoad = new DirectionRoad();
                String avitoIdStr  = nodeList.item(i).getAttributes().item(0).getNodeValue();
                String roadName = nodeList.item(i).getAttributes().item(1).getNodeValue();
                String city= null;
                String region = null;
                if(nodeList.item(i).getParentNode().getNodeName().equals("City")){
                    city = nodeList.item(i).getParentNode().getAttributes().item(1).getNodeValue();
                    region = nodeList.item(i).getParentNode().getParentNode().getAttributes().item(1).getNodeValue();
                }else if(nodeList.item(i).getParentNode().getNodeName().equals("Region")){
                    city = null;
                    region = nodeList.item(i).getParentNode().getAttributes().item(1).getNodeValue();
                }
                int avitoId = Integer.parseInt(avitoIdStr);

                directionRoad.setRoadName(roadName);
                directionRoad.setAvitoId(avitoId);
                region = region.replace("край","");
                region = region.replace("область","");
                region = region.trim();
                directionRoad.setCityName(city);
                directionRoad.setRegionName(region);
                directionRoadDao.saveOrUpdate(directionRoad);

            }

        }catch (Exception e){
            LOG.info(e.getMessage(),e);
        }
    }

}
