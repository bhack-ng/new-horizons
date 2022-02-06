package ru.simplex_software.arbat_baza.xml.yandex.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import ru.simplex_software.arbat_baza.xml.Offer;

/**
 * Created by dima on 11.07.16.
 */
public class OfferHandler extends AbHandler{
    Offer currentOffer;

    public OfferHandler() {
        getHandlerMap().put("type", new TypeHandler(this));
        tagName="offer";
    }


    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        if ("offer".equals(qName)) {
            currentOffer= new Offer();
        }

        super.startElement(uri,localName,qName,atts);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri,localName,qName);

    }

    public Offer getCurrentOffer() {
        return currentOffer;
    }
}
