package ru.simplex_software.arbat_baza.xml.yandex.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import ru.simplex_software.arbat_baza.xml.Offer;

/**
 * Created by dima on 11.07.16.
 */
public class TypeHandler extends AbHandler {
    OfferHandler currentOffer;
    public TypeHandler(OfferHandler currentOffer) {
        this.currentOffer=currentOffer;
    }


    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        currChars="";
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        final Offer.Type type = Offer.Type.getValue(currChars);
        if(type == null){
            throw new SAXException();
        }else{
            currentOffer.getCurrentOffer().setType(type);
        }
    }

}
