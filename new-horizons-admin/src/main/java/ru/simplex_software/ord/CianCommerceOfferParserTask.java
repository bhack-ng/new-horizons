package ru.simplex_software.ord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import ru.simplex_software.arbat_baza.model.AbstractBuilding;
import ru.simplex_software.arbat_baza.model.BuildingClass;
import ru.simplex_software.arbat_baza.model.BuildingEntrance;
import ru.simplex_software.arbat_baza.model.BuildingLiveType;
import ru.simplex_software.arbat_baza.model.BuildingStatus;
import ru.simplex_software.arbat_baza.model.BuildingType;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.arbat_baza.model.RealtyObjectType;
import ru.simplex_software.arbat_baza.model.commerce.Commerce;
import ru.simplex_software.arbat_baza.model.commerce.CommerceArea;
import ru.simplex_software.arbat_baza.model.commerce.CommerceBuilding;
import ru.simplex_software.arbat_baza.model.commerce.CommerceOptions;
import ru.simplex_software.arbat_baza.model.commerce.CommerceType;
import ru.simplex_software.arbat_baza.model.commerce.ContractType;

import javax.xml.xpath.XPathConstants;

/**
 * Created by Nick on 30.08.2016.
 */
public class CianCommerceOfferParserTask extends OfferParser {
    private static final Logger LOG= LoggerFactory.getLogger(CianCommerceOfferParserTask.class);

    public CianCommerceOfferParserTask(byte[] xml) {
        this.xml=xml;
        realtyObjectType= RealtyObjectType.COMMERCE_LEASE;
    }

    protected void setArea(RealtyObject realtyObject, Element root){
        super.setArea(realtyObject,root);

        final CommerceArea area = (CommerceArea)realtyObject.getArea();
        try {
            String rooms = (String) xPath.evaluate("/offer/area/@rooms", root, XPathConstants.STRING);
            area.setRooms(rooms);
            area.setRoomsCount(intHelper("/offer/area/@rooms_count", root,realtyObject));
            String total = (String) xPath.evaluate("/offer/area/@total", root, XPathConstants.STRING);
            area.setTotal(doubleHelper(total,"/offer/area/@total",realtyObject));
        }catch (Exception e){
            LOG.info(e.getMessage(),e);
        }
    }

    protected void setFloor(RealtyObject realtyObject,Element root){
        AbstractBuilding building = realtyObject.getBuilding();
        building.setFloor(intHelper("/offer/building/@floor", root,realtyObject));
        building.setFloorTotal(intHelper("/offer/building/@floor_total", root,realtyObject));
    }
    protected void setOptions(RealtyObject realtyObject ,Element root){
        CommerceOptions options = (CommerceOptions) realtyObject.getOptions();
        options.setAdd_phones(booleanHelper("/offer/options/@add_phones", root, realtyObject));
        options.setElect(booleanHelper("/offer/options/@elect", root, realtyObject));
        options.setHeat(booleanHelper("/offer/options/@heat", root, realtyObject));
        options.setCanal(booleanHelper("/offer/options/@canal", root, realtyObject));
        options.setGas(booleanHelper("/offer/options/@gas", root, realtyObject));
        options.setWater(booleanHelper("/offer/options/@water", root, realtyObject));
        options.setLift(booleanHelper("/offer/options/@lift", root, realtyObject));
        options.setParking(booleanHelper("/offer/options/@parking", root, realtyObject));
        options.setSecurity(booleanHelper("/offer/options/@security",root,realtyObject));
        options.setInternet(booleanHelper("/offer/options/@internet",root,realtyObject));
        options.setPhones(intHelper("/offer/options/@phones",root,realtyObject));
    }
    protected void setContractType(RealtyObject realtyObject ,Element root){
        Commerce commerce  = (Commerce) realtyObject;
        Integer contractTypeCianId = intHelper("/offer/contract_type",root,realtyObject);

        commerce.setContractType(ContractType.findByCianId(contractTypeCianId));
        if (commerce.getContractType()==ContractType.SALE_REALTY_OBJECT){
            realtyObject.setRealtyObjectType(RealtyObjectType.COMMERCE_SALE);
        }
        else {
            realtyObject.setRealtyObjectType(RealtyObjectType.COMMERCE_LEASE);
        }
        try {
            String type = (String) xPath.evaluate("/offer/commerce_type", root, XPathConstants.STRING);
            commerce.setType(CommerceType.findByCianId(type));
        }catch (Exception e){
            commerce.getExternalObectExt().getFeed().getErrors().add(e.getMessage());
            LOG.info(e.getMessage(),e);
        }
    }
    protected void setCommerceBuilding(RealtyObject realtyObject ,Element root){
        CommerceBuilding building  = (CommerceBuilding) realtyObject.getBuilding();
        Integer liveType = intHelper("/offer/building/@type",root,realtyObject);
        if(liveType==null);
        else if(liveType==1)building.setLiveType(BuildingLiveType.NOT_LIVING);
        else if(liveType == 2)building.setLiveType(BuildingLiveType.LIVING);

        Integer enter  = intHelper("/offer/building/@enter", root, realtyObject);
        if(enter == null);
        else if(enter==1)building.setEnter(BuildingEntrance.FREE);
        else if(enter ==2)building.setEnter(BuildingEntrance.PASS_SYSTEM);

        Integer buildingClassCianId = intHelper("/offer/building/@class", root, realtyObject);
        building.setBuildingClass(BuildingClass.findByCianId(buildingClassCianId));

        Integer buildingStatusCianId = intHelper("/offer/building/@status_b", root, realtyObject);
        building.setStatus_b(BuildingStatus.findByCianId(buildingStatusCianId));

        Integer buildingTypeCianId = intHelper("/offer/building/@type_b", root, realtyObject);
        building.setBuildingType(BuildingType.findByCianId(buildingTypeCianId));

        building.setArea_b(intHelper("/offer/building/@area_b", root,realtyObject));

        try {
            String name_bc = (String) xPath.evaluate("/offer/building/@name_bc",root,XPathConstants.STRING);
            building.setName_bc(name_bc);

            String ceiling = (String) xPath.evaluate("/offer/building/@ceiling",root,XPathConstants.STRING);
            building.setCeiling(ceiling);

        }catch (Exception e){
            realtyObject.getExternalObectExt().getFeed().getErrors().add(e.getMessage());
            LOG.info(e.getMessage(), e);
        }
    }
}
