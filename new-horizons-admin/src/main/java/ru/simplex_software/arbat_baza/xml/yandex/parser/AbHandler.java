package ru.simplex_software.arbat_baza.xml.yandex.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dima on 11.07.16.
 */
public abstract class AbHandler implements IHandler {

    boolean enable=true;
    IHandler nextHandler;
    protected String tagName;

    public AbHandler() {
    }

    protected String currChars;
    Map<String, IHandler> handlerMap= new HashMap<>();

    public Map<String, IHandler> getHandlerMap() {
        return handlerMap;
    }

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
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        if (nextHandler == null || !nextHandler.isEnable()) {
            nextHandler=getHandlerMap().get(qName);
        }
        if(nextHandler!=null && nextHandler.isEnable()){
            nextHandler.startElement(uri, localName, qName, atts);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (nextHandler == null || !nextHandler.isEnable()) {
            if(tagName.equals(qName)){
                enable=false;
            }
            nextHandler=getHandlerMap().get(qName);
        }
        if(nextHandler!=null && nextHandler.isEnable()){
            nextHandler.endElement(uri, localName, qName);
        }
    }


    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

}
