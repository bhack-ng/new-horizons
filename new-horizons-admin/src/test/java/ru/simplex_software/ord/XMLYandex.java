package ru.simplex_software.ord;

import org.junit.Test;
import org.xml.sax.helpers.DefaultHandler;
import ru.simplex_software.arbat_baza.xml.yandex.jaxb.Offer;
import ru.simplex_software.arbat_baza.xml.yandex.jaxb.RealtyFeed;
import ru.simplex_software.arbat_baza.xml.yandex.jaxb.adapters.LiveOffer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class XMLYandex {
    @Test
    public void doTest() throws Exception{
        final LiveOffer ld = new LiveOffer();
        ld.setNewFlat(Boolean.TRUE);
        ld.setRooms(3);
        ld.setRoomsOffered(3);

        JAXBContext jaxbContext = JAXBContext.newInstance(Offer.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        // output pretty printed
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        jaxbMarshaller.marshal(ld, System.out);

    }
    @Test
    public void read()throws Exception{
        JAXBContext jaxbContext = JAXBContext.newInstance(RealtyFeed.class);
        Unmarshaller um = jaxbContext.createUnmarshaller();
        final InputStream is = getClass().getClassLoader().getResourceAsStream("yandex_realty-1.xml");
        final RealtyFeed rf = (RealtyFeed) um.unmarshal(is);
        System.out.println("feed = " + rf);
    }

    @Test
    public void read2() throws Exception{
        final InputStream is = getClass().getClassLoader().getResourceAsStream("yandex_realty.xml");
        LimitedInputStream lis= new LimitedInputStream(is,1*1024*1024) {
            @Override
            protected void raiseError(long pSizeMax, long pCount) throws IOException {
                throw new SizeLimitException("input stream too big");
            }
        };
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        SAXParser parser = factory.newSAXParser();
        DefaultHandler saxp = new YandexSplitHandlerHandler(1024*16){
            @Override
            public OutputStream createOutputStram() {
                 OutputStream splitedOutputStream = new ByteArrayOutputStream(512);
                return splitedOutputStream ;//System.out;
            }

            @Override
            public void closeOutputStram() {
                final String s = new String(((ByteArrayOutputStream) splitedOutputStream).toByteArray());
                System.out.println(s);
            }
        };

        parser.parse(is, saxp);


    }


}
