package ru.simplex_software.arbat_baza;

import org.junit.Ignore;
import org.junit.Test;
import ru.simplex_software.arbat_baza.model.odor.ExternalAgency;
import ru.simplex_software.arbat_baza.model.odor.Feed;
import ru.simplex_software.arbat_baza.xml.yandex.jaxb.RealtyFeed;
import ru.simplex_software.arbat_baza.xml.yandex.parser.SAXPars;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;

import static java.lang.System.out;

/**
 * Created by dima on 11.07.16.
 */
public class YandexReadTest {

    @Test
    public void do1() throws Exception{
        XMLInputFactory factory = XMLInputFactory.newInstance();
        final InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("/yandex_realty-1.xml"));
        XMLStreamReader r = factory.createXMLStreamReader(reader);
        try {
            int event = r.getEventType();
            while (true) {
                switch (event) {
                    case XMLStreamConstants.START_DOCUMENT:
                        out.println("Start Document.");
                        break;
                    case XMLStreamConstants.START_ELEMENT:
                        out.println("Start Element: " + r.getName());
                        for(int i = 0, n = r.getAttributeCount(); i < n; ++i)
                            out.println("Attribute: " + r.getAttributeName(i)
                                    + "=" + r.getAttributeValue(i));

                        break;
                    case XMLStreamConstants.CHARACTERS:
                        if (r.isWhiteSpace())
                            break;

                        out.println("Text: " + r.getText());
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        out.println("End Element:" + r.getName());
                        break;
                    case XMLStreamConstants.END_DOCUMENT:
                        out.println("End Document.");
                        break;
                }

                if (!r.hasNext())
                    break;

                event = r.next();
            }
        } finally {
            r.close();
        }
    }

    @Test
    @Ignore
    public void doit2() throws Exception{

        final InputStream is = getClass().getResourceAsStream("/yandex_realty-1.xml");

        final ExternalAgency externalAgency = new ExternalAgency();
        externalAgency.setName("test companey");
        final Feed feed = new Feed();
        feed.setExternalAgency(externalAgency );
        feed.setLastUpdate(Instant.now());
        feed.setUrl("/yandex_realty-1.xml");

        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        SAXParser parser = factory.newSAXParser();
        SAXPars saxp = new SAXPars();

        parser.parse(is, saxp);
    }

    @Test
    public void doIt3() throws Exception{
        final InputStream is = getClass().getResourceAsStream("/yandex_realty.xml");
        JAXBContext jaxbContext = JAXBContext.newInstance(RealtyFeed.class);

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        final Object unmarshal = jaxbUnmarshaller.unmarshal(is);
        System.out.println(unmarshal );
    }
}
