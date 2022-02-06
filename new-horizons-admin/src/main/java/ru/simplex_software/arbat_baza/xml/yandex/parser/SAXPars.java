package ru.simplex_software.arbat_baza.xml.yandex.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import ru.simplex_software.arbat_baza.model.odor.Feed;
import ru.simplex_software.arbat_baza.model.odor.FeedUpdate;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dima on 11.07.16.
 */
public class SAXPars extends DefaultHandler {
    private static final Logger LOG = LoggerFactory.getLogger(SAXPars.class);
    private String currentElem;
    private Feed feed;
    private FeedUpdate feedUpdate;

    Map <String, IHandler > handlerMap= new HashMap<>();
    IHandler nextHandler;

    public Map<String, IHandler> getHandlerMap() {
        return handlerMap;
    }

    public SAXPars() {

        handlerMap.put("realty-feed", new RealtyFeedHanler());

    }



    String currChars;
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (nextHandler == null || !nextHandler.isEnable()) {
            currChars=new String(ch, start, length);
        }
        if(nextHandler!=null && nextHandler.isEnable()){
            nextHandler.characters(ch, start, length);
        }
    }


    @Override
    public void endDocument() throws SAXException {
        feedUpdate.setUpdateEndTime(Instant.now());
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (nextHandler == null || !nextHandler.isEnable()) {
            if("realty-feed".equals(qName)){
                return;
            }
            nextHandler=getHandlerMap().get(qName);
        }
        if(nextHandler!=null && nextHandler.isEnable()){
            nextHandler.endElement(uri, localName, qName);
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        LOG.info("start element "+ qName);
        if(! "http://webmaster.yandex.ru/schemas/feed/realty/2010-06".equals(uri)){
            LOG.error("element don't contains correct namespace ");
            throw new SAXException("element don't contains correct namespace ");
        }
        if (nextHandler == null || !nextHandler.isEnable()) {
            nextHandler=getHandlerMap().get(qName);
        }
        if(nextHandler!=null && nextHandler.isEnable()){
            nextHandler.startElement(uri, localName, qName, attributes);
        }

    }


}
