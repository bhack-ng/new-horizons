package ru.simplex_software.arbat_baza.init;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import ru.simplex_software.arbat_baza.DBAppender;
import ru.simplex_software.arbat_baza.dao.LogMessageDAO;
import ru.simplex_software.arbat_baza.dao.fias.FiasObjectDAO;
import ru.simplex_software.arbat_baza.dao.fias.FiasObjectSave;
import ru.simplex_software.arbat_baza.dao.fias.FiasSocrDAO;
import ru.simplex_software.arbat_baza.model.fias.FiasObject;
import ru.simplex_software.arbat_baza.model.fias.FiasSocr;

import javax.annotation.Resource;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import static ru.simplex_software.arbat_baza.xml.XmlUtils.getAttrValue;

/**
 * Загрузка данных из справочника ФИАС. Описание
 *
 */
public class FiasDataloader {
    private static final Logger LOG= LoggerFactory.getLogger(FiasDataloader.class);
    public String DATABASE_URL="http://fias.nalog.ru/Public/Downloads/Actual/fias_xml.rar";
    public static String DOMODEDOVO_GAOUID="d177d438-ea69-4169-854c-d4512d7c8ebe";
    public static String MO_GAOUID="29251dcf-00a1-4e34-98d4-5c47484a36d4";
    public static String MOSCOW_GAOUID="0c5b2444-70a0-4932-980c-b4dc0d3f02b5";
    File extractedXml;
    @Resource
    private DBAppender dbAppender;
    @Resource
    private LogMessageDAO logMessageDAO;
    @Resource
    private FiasSocrDAO fiasSocrDAO;
    @Resource
    private FiasObjectDAO fiasObjectDAO;
    @Resource
    FiasObjectSave fiasObjectSave;

    String fiasURL = "http://fias.nalog.ru/Public/Downloads/Actual/fias_xml.rar";

    String tmpDirPath  = System.getProperty("java.io.tmpdir");
    /** we can check fias catalog state ( downloading now  or not ).*/
    volatile private Boolean isDownloading = false;

    public void ensureDownloaded(){


    }
    public void fillAllDatabase(){

    }
    public void fillPartDatabase(String parent) throws IOException, SAXException, ParserConfigurationException{
        InputStream is =new FileInputStream("fias_xml/AS_ADDROBJ_20160717_3ce5c848-04ab-4e6a-a3fc-a748674e5583.XML");

        SAXParserFactory factory = SAXParserFactory.newInstance();




        final RecursiveHandler recursiveHandler = new RecursiveHandler();
        recursiveHandler.setRoot(DOMODEDOVO_GAOUID);
        do {

            is = new FileInputStream("fias_xml/AS_ADDROBJ_20160717_3ce5c848-04ab-4e6a-a3fc-a748674e5583.XML");

            SAXParser parser = factory.newSAXParser();


            parser.parse(is, recursiveHandler);
            recursiveHandler.next();
        }while (recursiveHandler.hasNext());


    }

    @Transactional
    public void fillFullUnpacked() {
        LOG.warn("downloading full unpacked fias database");
        List<File> xmlFile2=new ArrayList(2);
        InputStream socrIS=null;
        InputStream addrObjIS=null;;
        try {
            isDownloading = true;

                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser parser = factory.newSAXParser();

                LOG.info("start parsing unpacked database");

                LOG.info("start AS_SOCRBASE parsing...");
                    final URL url = new URL("http://www.simplex-software.ru/fias_xml/AS_SOCRBASE.XML");
                    socrIS = url.openStream();
                    parser.parse(socrIS, new SocrDefaultHandler());
                LOG.info("AS_SOCRBASE parsed");

                    final URL url2 = new URL("http://www.simplex-software.ru/fias_xml/AS_ADDROBJ.XML");
                    addrObjIS= url2.openStream();
                    parser.parse(addrObjIS, new FullParser());



        }catch (Exception e){
            isDownloading =false;
            LOG.error(e.getMessage(),e);
            dbAppender.error(e.getMessage());
        }
        finally {
            try {
                addrObjIS.close();
                socrIS.close();
            } catch (IOException e) {
                LOG.error(e.getMessage(),e);
            }
        }
    }


    @Transactional
    public void fillFullDatabase() {
        LOG.warn("downloading full fias database");
        List<File> xmlFile2=new ArrayList(2);
        try {
            isDownloading = true;
            xmlFile2 = saveFiasXml();
            for(File f:xmlFile2) {
                LOG.info("parsing file ", f.getAbsolutePath());
                dbAppender.info("parsing file:" + f.getAbsolutePath());
                InputStream is = new FileInputStream(f);

                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser parser = factory.newSAXParser();

                LOG.info("start parsing "+f.getName());
                if(f.getName().startsWith("AS_SOCRBASE")){
                    LOG.info("start AS_SOCRBASE parsing...");
                    parser.parse(is, new SocrDefaultHandler());
                    LOG.info("AS_SOCRBASE parsed");
                }else{
                    parser.parse(is, new FullParser());
                }


            }
        }catch (Exception e){
            isDownloading =false;
            LOG.error(e.getMessage(),e);
            dbAppender.error(e.getMessage());
        }
        finally {
            if(xmlFile2!=null){
                xmlFile2.forEach(File::delete);
            }
        }
//        }

    }
    public List<File> saveFiasXml() throws IOException{
        List<File> xmlFiles=new ArrayList(2);
        final String tmpDirPathLocal = tmpDirPath;
        final  File tmpDir = new File(tmpDirPathLocal);
        URL website = new URL(fiasURL);
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        final File downloadedFile = File.createTempFile("FIAS-", ".rar",tmpDir);
        downloadedFile.deleteOnExit();
        FileOutputStream fos = new FileOutputStream(downloadedFile);
        LOG.info("try to download fias file..");
        dbAppender.info("try to download fias file..");
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        LOG.info("fias file downloaded");
        dbAppender.info("fias file downloaded");
        String entryName = getEntry(downloadedFile,"AS_ADDROBJ");
        xmlFiles.add(extractEntry(tmpDir, downloadedFile, entryName));
        entryName = getEntry(downloadedFile,"AS_SOCRBASE");
        xmlFiles.add(extractEntry(tmpDir, downloadedFile, entryName));
        return xmlFiles;
    }

    @NotNull
    private File extractEntry(File tmpDir, File downloadedFile, String entryName) throws IOException {
        LOG.info("try extract fias file: ["+entryName+ "]");
        dbAppender.info("try extract fias file: ["+entryName+ "]");
        final Runtime runtime = Runtime.getRuntime();
        final Process extract = runtime.exec(new String[]{"unrar","e", downloadedFile.getAbsolutePath(), entryName}, new String[]{},tmpDir);
        try {
            final BufferedReader er= new BufferedReader(new InputStreamReader(extract.getInputStream()));
            String line=er.readLine();
            while(line!=null){
                LOG.info(line);
                dbAppender.info(line);
                line=er.readLine();
            }
            extract.waitFor(3, TimeUnit.HOURS);
            LOG.info("entry AS_ADDROBJ*.XML extracted");
            dbAppender.info("entry AS_ADDROBJ*.XML extracted");
            File file = new File(tmpDir, entryName);
            return file;
        } catch (InterruptedException e) {
            isDownloading = false;
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    private String getEntry(File downloadedFile, String prefix ) throws IOException {
        LOG.info("read rar entries");
        dbAppender.info("read rar entries");
        final Process unrar = Runtime.getRuntime().exec(new String[]{"unrar", "l", downloadedFile.getAbsolutePath()});
        final BufferedReader rr= new BufferedReader(new InputStreamReader(unrar.getInputStream()));
        String line;
        String entryName=null;
        while((line=rr.readLine())!=null){           ;
            LOG.info(line);
            dbAppender.info(line);
            final String[] split = line.split("\\s");
            for(int idx=0;idx<split.length;idx++){
                line= split[idx];
                if(line.startsWith(prefix) && line.endsWith(".XML")){
                    entryName=line;
                }
            }

        }
        LOG.info("entries read");
        dbAppender.info("entries read");
        return entryName;
    }

    @NotNull
    public static FiasObject createtFiasObject(Attributes attributes) {
        final FiasObject fiasObject = new FiasObject();
        final int aolevelIndex = attributes.getIndex("AOLEVEL");
        final String aolLevel = attributes.getValue(aolevelIndex);

        if(aolLevel!=null){fiasObject.setAOLEVEL(Integer.parseInt(aolLevel));}
        fiasObject.setOFFNAME(attributes.getValue(attributes.getIndex("OFFNAME")));
        fiasObject.setAOID(getAttrValue(attributes,"AOID"));
        fiasObject.setAOGUID(getAttrValue(attributes,"AOGUID"));
        fiasObject.setFORMALNAME(getAttrValue(attributes,"FORMALNAME"));
        fiasObject.setREGIONCODE(getAttrValue(attributes,"REGIONCODE"));
        fiasObject.setAUTOCODE(getAttrValue(attributes,"AUTOCODE"));
        fiasObject.setAREACODE(getAttrValue(attributes,"AREACODE"));
        fiasObject.setCITYCODE(getAttrValue(attributes,"CITYCODE"));
        fiasObject.setCTARCODE(getAttrValue(attributes,"CTARCODE"));

        fiasObject.setPLACECODE(getAttrValue(attributes,"PLACECODE"));
        fiasObject.setPLANCODE(getAttrValue(attributes,"PLANCODE"));
        fiasObject.setSTREETCODE(getAttrValue(attributes,"STREETCODE"));
        fiasObject.setEXTRCODE(getAttrValue(attributes,"EXTRCODE"));
        fiasObject.setSEXTCODE(getAttrValue(attributes,"SEXTCODE"));
        fiasObject.setPOSTALCODE(getAttrValue(attributes,"POSTALCODE"));
        fiasObject.setIFNSFL(getAttrValue(attributes,"IFNSFL"));
        fiasObject.setTERRIFNSFL(getAttrValue(attributes,"TERRIFNSFL"));
        fiasObject.setIFNSUL(getAttrValue(attributes,"IFNSUL"));
        fiasObject.setTERRIFNSUL(getAttrValue(attributes,"TERRIFNSUL"));
        fiasObject.setSHORTNAME(getAttrValue(attributes,"SHORTNAME"));
        fiasObject.setPARENTGUID(getAttrValue(attributes,"PARENTGUID"));
        final String currstatusStr = getAttrValue(attributes, "CURRSTATUS");
        if(StringUtils.hasLength(currstatusStr)){
            fiasObject.setCURRSTATUS(Integer.parseInt(currstatusStr));
        }
        fiasObject.setSTARTDATE(getLocalDate(getAttrValue(attributes, "STARTDATE")));
        fiasObject.setENDDATE(getLocalDate(getAttrValue(attributes,"ENDDATE")));
        return fiasObject;
    }
    public static LocalDate getLocalDate(String s){
        if(s==null) {
            return null;
        }else{
            return DateTimeFormatter.ISO_DATE.parse(s, LocalDate::from);
        }
    }
    class RecursiveHandler extends DefaultHandler {
        private int counter;
        private TreeSet<String> curr= new TreeSet<>();
        private TreeSet<FiasObject> next= new TreeSet<>(new FiasObjectComporator());
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if("Object".equals(localName)) {
                counter++;
                final FiasObject fiasObject = createtFiasObject(attributes);
                if (fiasObject.getPARENTGUID()!=null && curr.contains(fiasObject.getPARENTGUID())) {
                    next.add(fiasObject);
                }
                if(counter%4096==0){
                    float pc =((float)counter)*100/2900229;
                    LOG.info("load "+pc+"% completed");
                    dbAppender.info("load "+pc+"% completed");
                }

            }
        }
        public void setRoot(String id){
            curr.add(id);
        }
        public void next(){
            fiasObjectSave.bulkSave(next);
            curr= new TreeSet<String>();
            for(FiasObject fo:next){
                curr.add(fo.getAOGUID());
            }
            next=new TreeSet<>(new FiasObjectComporator());

            counter=0;
        }
        public boolean hasNext() {
            return curr.size()>0;
        }
    }

    private class FullParser extends DefaultHandler{
        private List<FiasObject> toSave=new ArrayList<>(256);
        int counter=0;
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if("Object".equals(qName)){
                final FiasObject fiasObject = createtFiasObject(attributes);
                counter++;
                if(counter%16384==0){
                    float pc =((float)counter)*100/2900229;
                    LOG.info("load "+pc+"% completed");
                    dbAppender.info("load "+pc+"% completed");
                }
                //if (fiasObject.getCURRSTATUS()==0) {
                    toSave.add(fiasObject);
                    if(toSave.size()>1024){
                        LOG.info("save 256 objects");
                        dbAppender.info("save 256 objects");
                        fiasObjectSave.bulkSave(toSave);
                        toSave.clear();
                    }
                //}
            }
        }
        @Override
        public void endDocument() throws SAXException {
            fiasObjectSave.bulkSave(toSave);
            toSave.clear();
            LOG.info("import success completed");
            dbAppender.info("import success completed");
            isDownloading = false;

        }
    }
    public class SocrDefaultHandler extends DefaultHandler {
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (!"AddressObjectType".equals(qName)) {
                return;
            }
            final FiasSocr socr = new FiasSocr();
            socr.setId(getAttrValue(attributes, "KOD_T_ST"));
            socr.setLevel(Integer.parseInt(getAttrValue(attributes, "LEVEL")));
            socr.setSocrName(getAttrValue(attributes, "SCNAME"));
            socr.setFullName(getAttrValue(attributes, "SOCRNAME"));
            fiasSocrDAO.saveOrUpdate(socr);
            LOG.info("socr saved ");

        }
    }

    public static class FiasObjectComporator implements Comparator<FiasObject> {
        @Override
        public int compare(FiasObject o1, FiasObject o2) {
            return o1.getAOID().compareTo(o2.getAOID());
        }
    }

    public Boolean getIsDownloading() {
        return isDownloading;
    }

    public void setIsDownloading(Boolean isDownloading) {
        this.isDownloading = isDownloading;
    }

    public String getFiasURL() {
        return fiasURL;
    }

    public void setFiasURL(String fiasURL) {
        this.fiasURL = fiasURL;
    }

    public String getTmpDirPath() {
        return tmpDirPath;
    }

    public void setTmpDirPath(String tmpDirPath) {
        this.tmpDirPath = tmpDirPath;
    }
}
