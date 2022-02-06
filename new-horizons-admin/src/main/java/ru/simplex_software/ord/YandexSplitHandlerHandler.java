package ru.simplex_software.ord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.OutputStream;
import java.time.OffsetDateTime;

/**
 * Divide xml into separated output streams: Each <offer> in separated output streams.
 */
public abstract  class YandexSplitHandlerHandler extends DefaultHandler {
    private static final Logger LOG = LoggerFactory.getLogger(YandexSplitHandlerHandler.class);
    private boolean realtyFeedStarted;
    private boolean generationDateStarted;
    private OffsetDateTime generationDate;
    private boolean offerStarted;
    private int elementsLimit;
    private XMLStreamWriter xmlWriter;
    private long offerLengthLimit;
    protected OutputStream splitedOutputStream;


    abstract public OutputStream createOutputStram();
    abstract public void closeOutputStram();

    public YandexSplitHandlerHandler(long offerLengthLimit) {
        this.offerLengthLimit = offerLengthLimit;
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        realtyFeedStarted = false;
        generationDate = null;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if ("realty-feed".equals(qName)) {
            if(realtyFeedStarted) throw new SAXException("only one <realty-feed> tag allowed");
            realtyFeedStarted = true;
        }
        if (realtyFeedStarted && "generation-date".equals(qName)) {
            if(generationDateStarted) throw new SAXException("only one <generationDateStarted> tag allowed");
            generationDateStarted = true;
        }
        if (realtyFeedStarted && "offer".equals(qName)) {
            if (!offerStarted) {
                offerStarted = true;
                XMLOutputFactory output = XMLOutputFactory.newInstance();
                splitedOutputStream = createOutputStram();
                try {
                    LimitOutputStream lout = new LimitOutputStream(splitedOutputStream, offerLengthLimit);
                    xmlWriter = output.createXMLStreamWriter(lout);
                    xmlWriter.writeStartDocument();
                    xmlWriter.writeCharacters("\n");
                    xmlWriter.writeStartElement("offer");
                    xmlWriter.writeAttribute("internal-id", attributes.getValue("internal-id"));
                } catch (XMLStreamException e) {
                    throw new SAXException(e.getMessage(),e);
                }

            } else {
                throw new SAXException("incorrect xml");
            }
        } else if (offerStarted) {
            try {
                xmlWriter.writeStartElement(qName);
            } catch (XMLStreamException e) {
                throw new SAXException(e.getMessage(), e);
            }
        }

    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (generationDateStarted) {
            final StringBuilder sb = new StringBuilder();
            sb.append(ch, start, length);
            generationDate = OffsetDateTime.parse(sb);
        } else if (offerStarted) {
            try {
                xmlWriter.writeCharacters(ch, start, length);
            } catch (XMLStreamException e) {
                throw new SAXException("incorrect xml", e);
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ("realty-feed".equals(qName)) {
            realtyFeedStarted = false;
        } else if (realtyFeedStarted && "generation-date".equals(qName)) {
            generationDateStarted = false;
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
