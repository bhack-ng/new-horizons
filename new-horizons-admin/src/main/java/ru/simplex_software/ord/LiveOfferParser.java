package ru.simplex_software.ord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;
import ru.simplex_software.arbat_baza.model.BuildingType;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.arbat_baza.model.live.DwellingHouse;
import ru.simplex_software.arbat_baza.model.live.LiveArea;
import javax.xml.xpath.XPathConstants;
/**
 * .
 */
public class LiveOfferParser extends OfferParser {
    private static final Logger LOG = LoggerFactory.getLogger(LiveOfferParser.class);

    protected  void setArea(RealtyObject realtyObject, Element root) {
        super.setArea(realtyObject,root);

        final LiveArea area = (LiveArea)realtyObject.getArea();
        try {

            String rooms = (String) xPath.evaluate("/offer/area/@rooms", root, XPathConstants.STRING);
            area.setRooms(rooms);
            String living = (String) xPath.evaluate("/offer/area/@living", root, XPathConstants.STRING);
            if(StringUtils.hasLength(living)){area.setLiving(doubleHelper(living, "/offer/area/@living",realtyObject));}
            String kitchen = (String) xPath.evaluate("/offer/area/@kitchen", root, XPathConstants.STRING);
            if(StringUtils.hasLength(kitchen)){area.setKitchen(doubleHelper(living, "/offer/area/@living", realtyObject));}
            String total = (String) xPath.evaluate("/offer/area/@total", root, XPathConstants.STRING);
            if(StringUtils.hasLength(total)){area.setTotal(doubleHelper(living, "/offer/area/@living",realtyObject));}

            area.setSplitBathroomCount(intHelper("/offer/options/@su_r",root, realtyObject));
            area.setFullBathroomCount(intHelper("/offer/options/@su_s",root, realtyObject));
            area.setLift_p(intHelper("/offer/options/@lift_p",root, realtyObject));
            area.setLift_g(intHelper("/offer/options/@lift_g",root, realtyObject));

            Integer rn=intHelper("/offer/rooms_num", root, realtyObject);
            if(rn!=null) {
                if (rn > 0 && rn <= 5) {
                    area.setRoomsCount(rn);
                }
            }

        }catch (Exception e) {
            LOG.info(e.getMessage(),e);
        }

    }

    @Override
    protected void setOptions(RealtyObject realtyObject, Element root) {

        final LiveArea area  = (LiveArea) realtyObject.getArea();
        try {

            String lodgiaCount = (String) xPath.evaluate("/offer/options/@lodgia", root, XPathConstants.STRING);
            if(lodgiaCount.equals("no")) {
                area.setRecessedBalconyCount(0);
            }
            else if (lodgiaCount.equals("yes")){
                area.setRecessedBalconyCount(-1);
            }
            else {
                area.setRecessedBalconyCount(intHelper("/offer/options/@lodgia", root,realtyObject));
            }

            String balconCount = (String) xPath.evaluate("/offer/options/@balсon", root,XPathConstants.STRING);
            if (balconCount.equals("no")){
                    area.setBalconyCount(0);
            }
            else if (balconCount.equals("yes")){
                area.setBalconyCount(-1);
            }
            else {
                area.setBalconyCount(intHelper("/offer/options/@balсon", root,realtyObject));
            }

        }catch (Exception e){
            LOG.info(e.getMessage(),e);
        }
    }



    protected void setFloor(RealtyObject realtyObject, Element root) {
        super.setFloor(realtyObject,root);

        final DwellingHouse building = (DwellingHouse)realtyObject.getBuilding();
        building.setFloor(intHelper("/offer/floor", root,realtyObject));
        building.setFloorTotal(intHelper("/offer/floor/@total", root,realtyObject));
        building.setBuildingType(BuildingType.findByCianId(intHelper("/offer/floor/@type", root,realtyObject)));

        try{

            String seria = (String) xPath.evaluate("/offer/floor/@seria", root, XPathConstants.STRING);
            building.setSeria(seria);

        }catch (Exception e){
            LOG.info(e.getMessage(),e);
        }

    }


}
