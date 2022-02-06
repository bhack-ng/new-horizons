package ru.simplex_software.arbat_baza.xml.yandex.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Created by dima on 11.07.16.
 */
public interface IHandler {
    public void characters (char ch[], int start, int length)
        throws SAXException;
    public void endElement (String uri, String localName,
                            String qName)
            throws SAXException;
    public void startElement (String uri, String localName,
                              String qName, Attributes atts)
            throws SAXException;

    public boolean isEnable() ;

}
