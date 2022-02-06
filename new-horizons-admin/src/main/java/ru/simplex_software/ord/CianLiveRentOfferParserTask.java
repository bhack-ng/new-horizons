package ru.simplex_software.ord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.arbat_baza.model.RealtyObjectType;
import ru.simplex_software.arbat_baza.model.live.LiveLeaseOptions;
import ru.simplex_software.arbat_baza.model.price.LiveRentPrice;

import javax.xml.xpath.XPathConstants;

/**
 * extract data from  part of yandex realty  xml (offer element) and save to database
 */
public class CianLiveRentOfferParserTask  extends LiveOfferParser  {
    private static final Logger LOG= LoggerFactory.getLogger(CianLiveRentOfferParserTask.class);


    public CianLiveRentOfferParserTask(byte[] xml) {
        this.xml=xml;
        realtyObjectType=RealtyObjectType.LIVE_LEASE;
    }



    @Override
    public void setOptions(RealtyObject realtyObject,Element root){
        super.setOptions(realtyObject,root);
        LiveLeaseOptions options = (LiveLeaseOptions)realtyObject.getOptions();

            options.setMebel(booleanHelper("/offer/options/@mebel",root,realtyObject));
            options.setHaveBath(booleanHelper("/offer/options/@bath", root,realtyObject));
            options.setMebelInKitchen(booleanHelper("/offer/options/@mebel_kitchen", root,realtyObject));
            options.setPets(booleanHelper("/offer/options/@pets", root,realtyObject));
            options.setChildren(booleanHelper("/offer/options/@kids", root,realtyObject));
            options.setPhone(booleanHelper("/offer/options/@phone", root,realtyObject));
            options.setRefrigerator(booleanHelper("/offer/options/@rfgr", root,realtyObject));
            options.setTv(booleanHelper("/offer/options/@tv", root,realtyObject));
            options.setWashMashine(booleanHelper("/offer/options/@wm", root,realtyObject));

    }

    protected void setPrice(RealtyObject realtyObject, Element root) {
        super.setPrice(realtyObject,root);
        try{
            final LiveRentPrice price = (LiveRentPrice)realtyObject.getPrice();

            price.setPrepay(intHelper("/offer/price/@prepay", root,realtyObject));

            String deposit = (String) xPath.evaluate("/offer/price/@deposit", root, XPathConstants.STRING);
            if(StringUtils.hasLength(deposit)){
                price.setDeposit("1".equals(deposit));
            }

        }catch (Exception e){
            LOG.info(e.getMessage(),e);
        }

    }

}
