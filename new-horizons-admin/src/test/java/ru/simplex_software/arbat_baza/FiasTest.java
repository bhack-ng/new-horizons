package ru.simplex_software.arbat_baza;

import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import ru.simplex_software.arbat_baza.dao.fias.FiasObjectDAO;
import ru.simplex_software.arbat_baza.dao.fias.FiasObjectSave;
import ru.simplex_software.arbat_baza.dao.fias.FiasSocrDAO;
import ru.simplex_software.arbat_baza.init.FiasDataloader;
import ru.simplex_software.arbat_baza.model.fias.FiasObject;
import ru.simplex_software.arbat_baza.model.fias.FiasSocr;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import static ru.simplex_software.arbat_baza.xml.XmlUtils.getAttrValue;

/**
 * Created by dima on 20.07.16.
 */
@RunWith(EasyMockRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class FiasTest {

    @Mock
    private FiasSocrDAO fiasSocrDAO;

    @Mock
    private FiasObjectDAO fiasObjectDAO;

    @Mock
    private FiasObjectSave fiasObjectSave;

    private TreeSet<String> cities = new TreeSet<>();
    private TreeSet<String> next = new TreeSet<>();
    private List<FiasObject> toSave = new ArrayList<>(256);
    private int counter = 0;

    @Test
    @Transactional()
    @Rollback(false)
    public void socrTest() throws Exception {

        final InputStream is = getInputStream();

        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        SAXParser parser = factory.newSAXParser();

        parser.parse(is, new DefaultHandler() {
            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) {
                if (!"AddressObjectType".equals(localName)) {
                    return;
                }
                final FiasSocr socr = new FiasSocr();
                socr.setId(getAttrValue(attributes, "KOD_T_ST"));
                socr.setLevel(Integer.parseInt(getAttrValue(attributes, "LEVEL")));
                socr.setSocrName(getAttrValue(attributes, "SCNAME"));
                socr.setFullName(getAttrValue(attributes, "SOCRNAME"));
                fiasSocrDAO.saveOrUpdate(socr);
            }
        });
    }

    @Test
    public void doitAddrObjects() throws Exception {

        InputStream is = getInputStream();

        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        SAXParser parser = factory.newSAXParser();


        String DOMODEDOVO_GAOUID = "d177d438-ea69-4169-854c-d4512d7c8ebe";
        String MSK_GAOUID = "0c5b2444-70a0-4932-980c-b4dc0d3f02b5";
//
//        final RecursiveHandler recursiveHandler = new RecursiveHandler();
//        recursiveHandler.setRoot(DOMODEDOVO_GAOUID);
//        do {
//
//            is = new FileInputStream("fias_xml/AS_ADDROBJ_20160717_3ce5c848-04ab-4e6a-a3fc-a748674e5583.XML");
//
//            parser = factory.newSAXParser();
//
//
//            parser.parse(is, recursiveHandler);
//            recursiveHandler.next();
//        }while (recursiveHandler.hasNext());
//
//        parser.parse(is, new FIASars());
//
//
        is = getInputStream();

        parser = factory.newSAXParser();

        counter = 0;
        parser.parse(is, new DefaultHandler() {
            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                FiasObject fiasObject = FiasDataloader.createtFiasObject(attributes);
                if ("203b6f77-01bc-44e5-befc-047432f193fc".equals(fiasObject.getAOID())) {
                    System.out.println(fiasObject);
                    counter++;
                    System.out.println("counter = " + counter);
                }
            }
        });
    }
//
//
//
//    class FIASars extends DefaultHandler{
//        @Override
//        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
//            if("Object".equals(localName)){
//                final FiasObject fiasObject = createtFiasObject(attributes);
//                        if (fiasObject.getCURRSTATUS()==0) {
//
//                            toSave.add(fiasObject);
//
//                            counter++;
//                            if(counter%1024==0){
//                                System.out.println("counter = " + counter);
//                                fiasObjectSave.bulkSave(toSave);
//                                toSave.clear();
//                            }
//                        }
//            }
//
//
//        }
//
//    }
//

    private InputStream getInputStream() {
        //return new FileInputStream("fias_xml/AS_ADDROBJ_20160717_3ce5c848-04ab-4e6a-a3fc-a748674e5583.XML");
        String data = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<AddressObjectType " +
                "KOD_T_ST=\"123\" " +
                "LEVEL=\"5\" " +
                "SCNAME=\"SCNAME\" " +
                "SOCRNAME=\"SOCRNAME\"" +
                ">TYPE</AddressObjectType>";

        return new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
    }
}
