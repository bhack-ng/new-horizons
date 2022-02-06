package ru.simplex_software.ord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.arbat_baza.model.RealtyObjectType;
import ru.simplex_software.arbat_baza.model.live.BuildingTypeSale;
import ru.simplex_software.arbat_baza.model.live.DwellingHouse;
import ru.simplex_software.arbat_baza.model.live.LiveSaleOptions;
import ru.simplex_software.arbat_baza.model.live.LiveSaleRealty;
import ru.simplex_software.arbat_baza.model.live.SaleType;
import ru.simplex_software.arbat_baza.model.live.WindowsPosition;

import javax.xml.xpath.XPathConstants;

import static java.lang.String.valueOf;

/**
 * .
 */
public class CianLiveSaleOfferParserTask extends LiveOfferParser  {
    private static final Logger LOG = LoggerFactory.getLogger(CianLiveSaleOfferParserTask.class);

    public CianLiveSaleOfferParserTask(){}

    public CianLiveSaleOfferParserTask(byte[] xml) {
        this.xml=xml;
        realtyObjectType= RealtyObjectType.LIVE_SALE;
    }

    protected void setOptions(RealtyObject realtyObject,Element root){
        super.setOptions(realtyObject,root);
        LiveSaleRealty liveSaleRealty = (LiveSaleRealty) realtyObject;
        DwellingHouse dwellingHouse = (DwellingHouse) realtyObject.getBuilding();
        LiveSaleOptions options =(LiveSaleOptions) liveSaleRealty.getOptions();
        try {

            String buildingType = (String) xPath.evaluate("/offer/options/@object_type", root, XPathConstants.STRING);
            if(buildingType.equals(2))liveSaleRealty.setBuildingTypeSale(BuildingTypeSale.NEW_BUILDING);
            else if (buildingType.equals(1))liveSaleRealty.setBuildingTypeSale(BuildingTypeSale.SECONDHAND);

            String saleType = (String) xPath.evaluate("/offer/options/@sale_type",root,XPathConstants.STRING);
            liveSaleRealty.setSaleType(SaleType.findByCianId(saleType));

            options.setWindowsPosition(WindowsPosition.findByCianId(intHelper("/offer/options/@windows",root,realtyObject)));
            options.setPhone(booleanHelper("/offer/options/@phone", root,realtyObject));
            options.setHomeMortgage(booleanHelper("/offer/options/@ipoteka", root,realtyObject));


        }catch (Exception e ){
            LOG.info(e.getMessage(),e);
        }
    }

}
