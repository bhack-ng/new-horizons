package ru.simplex_software.ord;

import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import ru.simplex_software.arbat_baza.model.FeedType;
import ru.simplex_software.arbat_baza.model.odor.Feed;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.OutputStream;

/**
 * Divide xml into separated output streams: Each <offer> in separated output streams.
 */
public abstract class CianRentSplitHandler extends DefaultHandler {
    private static final Logger LOG = LoggerFactory.getLogger(CianRentSplitHandler.class);


    private boolean flatsRentStarted;
    private boolean offerStarted;


    private XMLStreamWriter xmlWriter;
    protected OutputStream splitedOutputStream;
    private Feed feed;
    private String rootElementName;


    abstract public OutputStream createOutputStram();
    abstract public void closeOutputStram();

    public CianRentSplitHandler(Feed feed) {
        this.feed=feed;
        if(FeedType.CIAN_LIVE_SALE==feed.getFeedType()){
            rootElementName="flats_for_sale";
        }else if(FeedType.CIAN_LIVE_RENT==feed.getFeedType()){
            rootElementName="flats_rent";
        }else if (FeedType.CIAN_COMMERICAL==feed.getFeedType()){
            rootElementName = "commerce";
        }

        else{
            throw new NotImplementedException("not implimeted for feedType "+feed.getFeedType());
        }
    }

    @Override
    public void startDocument() throws SAXException {
        flatsRentStarted = false;
        offerStarted = false;
    }

    @Override
    public void endDocument() throws SAXException {
        flatsRentStarted = false;
        offerStarted = false;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if((!flatsRentStarted) && (!rootElementName.equals(qName))){
            throw new SAXException("root tag must be <"+rootElementName+">  but tag is "+qName);
        }
        if (rootElementName.equals(qName)) {
            if(flatsRentStarted) throw new SAXException("only one <"+rootElementName+"> tag allowed");
            flatsRentStarted = true;
        }
        if (flatsRentStarted && "offer".equals(qName)) {
            if (!offerStarted) {
                offerStarted = true;
                XMLOutputFactory output = XMLOutputFactory.newInstance();
                splitedOutputStream = createOutputStram();
                try {
                    LimitOutputStream lout = new LimitOutputStream(splitedOutputStream, feed.getOfferSizeLimit());
                    xmlWriter = output.createXMLStreamWriter(lout);
                    xmlWriter.writeStartDocument();
                    xmlWriter.writeCharacters("\n");
                    xmlWriter.writeStartElement("offer");
                    xmlWriter.writeAttribute("feedId",""+feed.getId());

                } catch (XMLStreamException e) {
                    throw new SAXException(e.getMessage(),e);
                }

            } else {
                throw new SAXException("incorrect xml");
            }
        } else if (offerStarted) {
            try {
                xmlWriter.writeStartElement(qName);
                for(int i=0;i<attributes.getLength();i++){
                    xmlWriter.writeAttribute(attributes.getQName(i),attributes.getValue(i));
                }
            } catch (XMLStreamException e) {
                throw new SAXException(e.getMessage(), e);
            }
        }

    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
       if (offerStarted) {
            try {
                xmlWriter.writeCharacters(ch, start, length);
            } catch (XMLStreamException e) {
                throw new SAXException("incorrect xml", e);
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (rootElementName.equals(qName)) {
             flatsRentStarted = false;
        } else if ("offer".equals(qName)) {
            if (offerStarted) {
                offerStarted = false;
                try {
                    xmlWriter.writeEndElement();//offer
                    xmlWriter.writeEndDocument();
                    closeOutputStram();
                    //send msg
                } catch (XMLStreamException e) {
                    throw new SAXException("stop parse");
                }
            } else {
                throw new SAXException("incorrect xml");
            }
        } else if (offerStarted) {
            try {
                xmlWriter.writeEndElement();
            } catch (XMLStreamException e) {
                throw new SAXException("incorrect xml");
            }
        }
    }
}
