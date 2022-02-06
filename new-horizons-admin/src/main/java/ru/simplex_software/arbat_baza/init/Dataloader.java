package ru.simplex_software.arbat_baza.init;

import com.opencsv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.simplex_software.arbat_baza.PhotoUtils;
import ru.simplex_software.arbat_baza.dao.AgencyDAO;
import ru.simplex_software.arbat_baza.dao.AgentDAO;
import ru.simplex_software.arbat_baza.dao.CianAdminAreaDAO;
import ru.simplex_software.arbat_baza.dao.DirectionRoadDao;
import ru.simplex_software.arbat_baza.dao.MetroStationDAO;
import ru.simplex_software.arbat_baza.dao.ObjectStatusDAO;
import ru.simplex_software.arbat_baza.dao.PhotoDAO;
import ru.simplex_software.arbat_baza.dao.PhotoDataDAO;
import ru.simplex_software.arbat_baza.dao.RealtyObjectDAO;
import ru.simplex_software.arbat_baza.dao.StreetDAO;
import ru.simplex_software.arbat_baza.dao.commerce.CommerceDAO;
import ru.simplex_software.arbat_baza.model.Agency;
import ru.simplex_software.arbat_baza.model.Agent;
import ru.simplex_software.arbat_baza.model.CianAdminArea;
import ru.simplex_software.arbat_baza.model.GeoCoordinates;
import ru.simplex_software.arbat_baza.model.MetroStation;
import ru.simplex_software.arbat_baza.model.ObjectStatus;
import ru.simplex_software.arbat_baza.model.Photo;
import ru.simplex_software.arbat_baza.model.RealtyFilter;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.arbat_baza.model.Street;
import ru.simplex_software.arbat_baza.model.commerce.Commerce;
import ru.simplex_software.arbat_baza.model.live.LiveLeaseOptions;
import ru.simplex_software.arbat_baza.model.live.LiveLeaseRealty;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * .
 */
public class Dataloader {
    private static final Logger LOG= LoggerFactory.getLogger(Dataloader .class);
    @Resource
    private MetroStationDAO metroStationDAO;
    @Resource
    private StreetDAO streetDAO;

    @Resource
    private AgencyDAO agencyDAO;

    @Resource
    private AgentDAO agentDAO;

    @Resource
    private CommerceDAO commerceDAO;

    @Resource
    private RealtyObjectDAO realtyObjectDAO;
    @Resource
    private DirectionRoadDao directionRoadDao;
    @Resource
    private DirectionRoadParser directionRoadParser;
    @Resource
    private PhotoDAO photoDAO;
    @Resource
    private PhotoDataDAO photoDataDAO;
    @Resource
    private FiasDataloader fiasDataloader;

    @Resource
    private ObjectStatusDAO objectStatusDAO;
    @Resource
    private CianAdminAreaDAO cianAdminAreaDAO;

    @Transactional
    public void load() throws Exception{

    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void importDirectionRoad(){
        if(directionRoadDao.findAll().isEmpty()) {
            directionRoadParser.parsXML();
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void objectStatuses(){
        if(objectStatusDAO.findAll().isEmpty()) {
            createObjectStatus(1l, "Рекламируется", "#b7f769");
            createObjectStatus(2L, "Свободен", "#faf07f");
            createObjectStatus(3L, "Сдан", "#c5dafc");
            createObjectStatus(4l, "Некачественный лот", "#fdb4af");
            createObjectStatus(5l, "Недозвон", "#fbe58f");
            createObjectStatus(6l, "Думает", "#feffb0");
            createObjectStatus(7L, "Удален", "FF0000");
        }
    }

    private void createObjectStatus(Long id, String name, String color) {
        final ObjectStatus os=new ObjectStatus();
        os.setId(id);
        os.setName(name);
        os.setColor(color);
        objectStatusDAO.saveOrUpdate(os);
    }

    @Transactional
    public void makePreviewPhotos(){
        for(Photo p:photoDAO.findWithoutPreview()){
            makePreview(p);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void makePreview(Photo p){
        try {
            PhotoUtils.makePreviewImageInmem(p);
            photoDataDAO.saveOrUpdate(p.getPreview());
            photoDAO.saveOrUpdate(p);
        }catch (Exception ex){
            LOG.error(ex.getMessage(),ex);
        }
    }




    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void insertAgent() {
        if(agentDAO.findAll().size()!=0){
            return;
        }
        Agent agent = new Agent();
        agent.setExternalId(1);
        agent.setLogin("admin");
        agent.setPassword("password");
        agent.setPhone("+7(926)555-55-55");
        agent.setFio("Админ");
        agent.setRole(Agent.Role.ADMIN);
        agent.setFilter(new RealtyFilter());
        agent.setAgency(agencyDAO.findOne());
        agentDAO.saveOrUpdate(agent);
    }

    private void insetOptions() {
        for(RealtyObject r:realtyObjectDAO.findAll()){
            if(r instanceof LiveLeaseRealty){
                LiveLeaseRealty lr=(LiveLeaseRealty)r;
                if(lr.getOptions()==null){
                    lr.setOptions(new LiveLeaseOptions());
                    realtyObjectDAO.saveOrUpdate(lr);
                }



            }

        }
    }


    private void updateGeoCoordinates() {
        for(Commerce c:commerceDAO.findAll()){
            if(c.getAddress().getGeoCoordinates()==null){
                c.getAddress().setGeoCoordinates(new GeoCoordinates());
                commerceDAO.saveOrUpdate(c);
            }

        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void insertAgency(){

        if(agencyDAO.findOne()==null){
            Agency agency = new Agency();
            agency.setPhone("555");
            agencyDAO.saveOrUpdate(agency);
        }

    }

    /**
     * import form   http://www.cian.ru/metros.php
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public  void importMetroStations() {
        if(metroStationDAO.findAll().size()!=0){
            return;
        }
        try{
//            URL url=new URL("http://www.cian.ru/metros.php");
//            URLConnection urlConnection = url.openConnection();
            final ClassLoader cl = getClass().getClassLoader();
            final InputStream stream = cl.getResourceAsStream("avito.metros.php.xml");


            DocumentBuilderFactory dbFactory
                    = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(stream);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("location");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);


                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String value =eElement.getTextContent();
                    String id=eElement.getAttributeNode("id").getValue();
                    MetroStation metroStation = new MetroStation();
                    metroStation.setId(Long.parseLong(id));
                    metroStation.setStationName(value);
                    metroStationDAO.saveOrUpdate(metroStation);

                }
            }
        }catch (Exception ex){
            LOG.error(ex.getMessage(),ex);
        }
    }


    public void importStreets() throws IOException{
        if(streetDAO.findAll().size()!=0){
            return;
        }
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream resourceAsStream = classLoader.getResourceAsStream("db_copy_streets.csv");
        InputStreamReader inputStreamReader = new InputStreamReader(resourceAsStream);
        CSVReader reader = new CSVReader(inputStreamReader, ';');
        reader.readNext();//read headers
        String [] nextLine;

        while ((nextLine = reader.readNext()) != null) {
            Street street = new Street();
            street.setId(Long.parseLong(nextLine[0]));
            street.setStreetName(nextLine[1]);
            streetDAO.saveOrUpdate(street);

        }
    }

    /**this function create and save object CianAdminArea
     *
     * @param cianId - cianId
     * @param fiasAoguid - fiasID
     * @param name - region
     */
    public void setCianAdminArea(int cianId,String fiasAoguid,String name){
        CianAdminArea cianAdminArea = new CianAdminArea();
        cianAdminArea.setName(name);
        cianAdminArea.setFiasAoguid(fiasAoguid);
        cianAdminArea.setCianId(cianId);
        cianAdminAreaDAO.saveOrUpdate(cianAdminArea);
    }

    /**
     * To filled table cianadminarea in DB
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void insertAdminArea(){
        if(!cianAdminAreaDAO.findAll().isEmpty()) {
            return;
        }
            setCianAdminArea(1, "0c5b2444-70a0-4932-980c-b4dc0d3f02b5", "Москва");
            setCianAdminArea(2, "29251dcf-00a1-4e34-98d4-5c47484a36d4", "Московская область");
            setCianAdminArea(10, "c2deb16a-0330-4f05-821f-1d09c93331e6", "Санкт-Петербург");
            setCianAdminArea(11, "6d1ebb35-70c6-4129-bd55-da3969658f5d", "Ленинградская область");
            setCianAdminArea(3, "b8837188-39ee-4ff9-bc91-fcc9ed451bb3", "Владимирская область");
            setCianAdminArea(4, "18133adf-90c2-438e-88c4-62c41656de70", "Калужская область");
            setCianAdminArea(5, "963073ee-4dfc-48bd-9a70-d2dfc6bd1f31", "Рязанская область");
            setCianAdminArea(9, "e8502180-6d08-431b-83ea-c7038f0df905", "Смоленская область");
            setCianAdminArea(6, "61723327-1c20-42fe-8dfa-402638d9b396", "Тверская область");
            setCianAdminArea(7, "d028ec4f-f6da-4843-ada6-b68b3e0efa3d", "Тульская область");
            setCianAdminArea(8, "a84b2ef4-db03-474b-b552-6229e801ae9b", "Ярославская область");
            setCianAdminArea(12, "844a80d6-5e31-4017-b422-4d9c01e9942c", "Амурская область");
            setCianAdminArea(13, "294277aa-e25d-428c-95ad-46719c4ddb44", "Архангельская область");
            setCianAdminArea(14, "83009239-25cb-4561-af8e-7ee111b1cb73", "Астраханская область");
            setCianAdminArea(15, "639efe9d-3fc8-4438-8e70-ec4f2321f2a7", "Белгородская область");
            setCianAdminArea(16, "f5807226-8be0-4ea8-91fc-39d053aec1e2", "Брянская область");
            setCianAdminArea(17, "da051ec8-da2e-4a66-b542-473b8d221ab4", "Волгоградская область");
            setCianAdminArea(18, "ed36085a-b2f5-454f-b9a9-1c9a678ee618", "Вологодская область");
            setCianAdminArea(19, "b756fe6b-bbd3-44d5-9302-5bfcc740f46e", "Воронежская область");
            setCianAdminArea(20, "0824434f-4098-4467-af72-d4f702fed335", "Ивановская область");
            setCianAdminArea(21, "6466c988-7ce3-45e5-8b97-90ae16cb1249", "ркутская область");
            setCianAdminArea(22, "90c7181e-724f-41b3-b6c6-bd3ec7ae3f30", "Калининградская область");
            setCianAdminArea(85, "d02f30fc-83bf-4c0f-ac2b-5729a866a207", "Камчатский край");
            setCianAdminArea(23, "393aeccb-89ef-4a7e-ae42-08d5cebc2e30", "Кемеровская область");
            setCianAdminArea(24, "0b940b96-103f-4248-850c-26b6c7296728", "Кировская область");
            setCianAdminArea(25, "15784a67-8cea-425b-834a-6afe0e3ed61c", "Костромская область");
            setCianAdminArea(26, "4a3d970f-520e-46b9-b16c-50d4ca7535a8", "Курганская область");
            setCianAdminArea(27, "ee594d5e-30a9-40dc-b9f2-0add1be44ba1", "Курская область");
            setCianAdminArea(28, "1490490e-49c5-421c-9572-5673ba5d80c8", "Липецкая область");
            setCianAdminArea(29, "9c05e812-8679-4710-b8cb-5e8bd43cdf48", "Магаданская область");
            setCianAdminArea(30, "1c727518-c96a-4f34-9ae6-fd510da3be03", "Мурманская область");
            setCianAdminArea(31, "88cd27e2-6a8a-4421-9718-719a28a0a088", "Нижегородская область");
            setCianAdminArea(32, "e5a84b81-8ea1-49e3-b3c4-0528651be129", "Новгородская область");
            setCianAdminArea(33, "1ac46b49-3209-4814-b7bf-a509ea1aecd9", "Новосибирская область");
            setCianAdminArea(34, "05426864-466d-41a3-82c4-11e61cdc98ce", "Омская область");
            setCianAdminArea(35, "8bcec9d6-05bc-4e53-b45c-ba0c6f3a5c44", "Оренбургская область");
            setCianAdminArea(36, "5e465691-de23-4c4e-9f46-f35a125b5970", "Орловская область");
            setCianAdminArea(37, "c99e7924-0428-4107-a302-4fd7c0cca3ff", "Пензенская область");
            setCianAdminArea(38, "c99e7924-0428-4107-a302-4fd7c0cca3ff", "Пензенская область");
            setCianAdminArea(84, "4f8b1a21-e4bb-422f-9087-d3cbf4bebc14", "Пермский край");
            setCianAdminArea(38, "f6e148a1-c9d0-4141-a608-93e3bd95e6c4", "Псковская область");
            setCianAdminArea(39, "f10763dc-63e3-48db-83e1-9c566fe3092b", "Ростовская область");
            setCianAdminArea(40, "df3d7359-afa9-4aaa-8ff9-197e73906b1c", "Самарская область");
            setCianAdminArea(41, "df594e0e-a935-4664-9d26-0bae13f904fe", "Саратовская область");
            setCianAdminArea(42, "aea6280f-4648-460f-b8be-c2bc18923191", "Сахалинская область");
            setCianAdminArea(43, "92b30014-4d52-4e2e-892d-928142b924bf", "Свердловская область");
            setCianAdminArea(44, "a9a71961-9363-44ba-91b5-ddf0463aebc2", "Тамбовская область");
            setCianAdminArea(45, "889b1f3a-98aa-40fc-9d3d-0f41192758ab", "Томская область");
            setCianAdminArea(46, "54049357-326d-4b8f-b224-3c6dc25d6dd3", "Тюменская область");
            setCianAdminArea(47, "fee76045-fe22-43a4-ad58-ad99e903bd58", "Ульяновская область");
            setCianAdminArea(48, "27eb7c10-a234-44da-a59c-8b1f864966de", "Челябинская область");
            setCianAdminArea(49, "b6ba5716-eb48-401b-8443-b197c9578734", "Забайкальский край");
            setCianAdminArea(71, "8276c6a1-1a86-4f0d-8920-aba34d4cc34a", "Алтайский край");
            setCianAdminArea(53, "5c48611f-5de6-4771-9695-7e36a4e7529d", "Республика Алтай");
            setCianAdminArea(50, "d8327a56-80de-4df2-815c-4f6ab1224c50", "Республика Адыгея");
            setCianAdminArea(51, "6f2cbfd8-692a-4ee4-9b16-067210bde3fc", "Республика Башкортостан");
            setCianAdminArea(52, "a84ebed3-153d-4ba9-8532-8bdf879e1f5a", "Республика Бурятия");
            setCianAdminArea(54, "0bb7fa19-736d-49cf-ad0e-9774c4dae09b", "Республика Дагестан");
            setCianAdminArea(55, "b2d8cd20-cabc-4deb-afad-f3c4b4d55821", "Республика Ингушетия");
            setCianAdminArea(56, "1781f74e-be4a-4697-9c6b-493057c94818", "Кабардино-Балкарская Республика");
            setCianAdminArea(57, "491cde9d-9d76-4591-ab46-ea93c079e686", "Республика Калмыкия");
            setCianAdminArea(58, "61b95807-388a-4cb1-9bee-889f7cf811c8", "Карачаево-Черкесская Республика");
            setCianAdminArea(59, "248d8071-06e1-425e-a1cf-d1ff4c4a14a8", "Республика Карелия");
            setCianAdminArea(60, "c20180d9-ad9c-46d1-9eff-d60bc424592a", "Республика Коми");
            setCianAdminArea(61, "de2cbfdf-9662-44a4-a4a4-8ad237ae4a3e", "Республика Марий эл");
            setCianAdminArea(62, "37a0c60a-9240-48b5-a87f-0d8c86cdb6e1", "Республика Мордовия");
            setCianAdminArea(63, "c225d3db-1db6-4063-ace0-b3fe9ea3805f", "Республика Саха (Якутия)");
            setCianAdminArea(64, "de459e9c-2933-4923-83d1-9c64cfd7a817", "Республика Северная Осетия-Алания");
            setCianAdminArea(65, "0c089b04-099e-4e0e-955a-6bf1ce525f1a", "Республика Татарстан");
            setCianAdminArea(66, "026bc56f-3731-48e9-8245-655331f596c0", "Республика Тыва");
            setCianAdminArea(67, "52618b9c-bcbb-47e7-8957-95c63f0b17cc", "Удмуртская Республика");
            setCianAdminArea(68, "8d3f1d35-f0f4-41b5-b5b7-e7cadf3e7bd7", "Республика Хакасия");
            setCianAdminArea(69, "de67dc49-b9ba-48a3-a4cc-c2ebfeca6c5e", "Чеченская Республика");
            setCianAdminArea(70, "878fc621-3708-46c7-a97f-5a13a4176b3e", "Чувашская Республика");
            setCianAdminArea(72, "d00e1013-16bd-4c09-b3d5-3cb09fc54bd8", "Краснодарский край");
            setCianAdminArea(73, "db9c4f8b-b706-40e2-b2b4-d31b98dcd3d1", "Красноярский край");
            setCianAdminArea(87, "bd8e6511-e4b9-4841-90de-6bbc231a789e", "Крым");
            setCianAdminArea(87, "6fdecb78-893a-4e3f-a5ba-aa062459463b", "Севастополь");
            setCianAdminArea(74, "43909681-d6e1-432d-b61f-ddac393cb5da", "Приморский край");
            setCianAdminArea(75, "327a060b-878c-4fb4-8dc4-d5595871a3d8", "Ставропольский край");
            setCianAdminArea(76, "7d468b39-1afa-41ec-8c4f-97a8603cb3d4", "Хабаровский край");
            setCianAdminArea(77, "1b507b09-48c9-434f-bf6f-65066211c73e", "Еврейская автономная область");
            setCianAdminArea(78, "89db3198-6803-4106-9463-cbf781eff0b8", "НАО");
            setCianAdminArea(80, "d66e5325-3a25-4d29-ba86-4ca351d9704b", "ХМАО");
            setCianAdminArea(81, "f136159b-404a-4f1f-8d8d-d169e1374d5c", "Чукотский автономный округ");
            setCianAdminArea(82, "826fa834-3ee8-404f-bdbc-13a5221cfb6e", "ЯНАО");
    }


}
