package ru.simplex_software.arbat_baza.viewmodel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import ru.simplex_software.arbat_baza.AuthService;
import ru.simplex_software.arbat_baza.dao.ContactsOfOwnerDAO;
import ru.simplex_software.arbat_baza.dao.LiveSaleRealtyDAO;
import ru.simplex_software.arbat_baza.dao.RealtyObjectDAO;
import ru.simplex_software.arbat_baza.dao.fias.FiasObjectDAO;
import ru.simplex_software.arbat_baza.model.BuildingType;
import ru.simplex_software.arbat_baza.model.ContactsOfOwner;
import ru.simplex_software.arbat_baza.model.RealtyObjectType;
import ru.simplex_software.arbat_baza.model.fias.FiasAddressVector;
import ru.simplex_software.arbat_baza.model.fias.FiasObject;
import ru.simplex_software.arbat_baza.model.live.DwellingHouse;
import ru.simplex_software.arbat_baza.model.live.LiveArea;
import ru.simplex_software.arbat_baza.model.live.LiveSaleRealty;
import ru.simplex_software.ord.X;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Upload  and import realty objects from excel file.
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ExcelImportVM {

    private static final Logger LOG = LoggerFactory.getLogger(ExcelImportVM.class);
    @WireVariable
    X cianLiveSaleOfferParserTask;//= new CianLiveSaleOfferParserTask();
    @WireVariable
    RealtyObjectDAO realtyObjectDAO;
    @WireVariable
    AuthService authService;
    @WireVariable
    FiasObjectDAO fiasObjectDAO;
    @WireVariable
    LiveSaleRealtyDAO liveSaleRealtyDAO;
    @WireVariable
    ContactsOfOwnerDAO contactsOfOwnerDAO;
    private String fileName;
    private XSSFWorkbook excelBook = null;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window view){

    }

    @Command("upload")
    @NotifyChange("*")
    public void onExcelUpload(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx) {
        LOG.debug("Upload Event Is Coming");
        UploadEvent upEvent = null;
        Object objUploadEvent = ctx.getTriggerEvent();
        if (objUploadEvent != null && (objUploadEvent instanceof UploadEvent)) {
            upEvent = (UploadEvent) objUploadEvent;
        }
        if (upEvent != null) {
            Media media = upEvent.getMedia();
            final String contentType = media.getContentType();

            final byte[] byteData = media.getByteData();
                fileName = media.getName();

            InputStream inp = new ByteArrayInputStream(byteData);
            try {
                excelBook = new XSSFWorkbook(inp);
                loadFeedToDB();                                 //todo это должнобыть не тут, а завязано на интерфейс

            }catch (Exception ex){
                LOG.error(ex.getMessage(), ex);
            }



        }
    }
    public void loadFeedToDB(){
        int countObjects=0;
        Sheet sheet = excelBook.getSheetAt(0);
        Iterator<Row> rows = sheet.rowIterator();
        List<LiveSaleRealty> liveSaleRealtyList =  new ArrayList<LiveSaleRealty>();
        while (rows.hasNext()){

            Row row = rows.next();
            Cell id = row.getCell(2);

            /*проверка на то , описывает ли это строка обьект или нет
             *(у обьекта в этой строке id начинающийся с '#')
             */
            if ( id.getStringCellValue().contains("#") ) {


                /** получаем id который будем использовать в качестве уникального
                 * интдентефикатора для  обьектов из Exel
                 * если его взять не получается , то он не будет уникальным и возможна дубликация
                 * так что в этом случае не будем его сохранять от греха подальше
                 **/
                Long localId;
                try {
                    localId = Long.parseLong(row.getCell(2).getStringCellValue().replaceAll("[^0-9]",""));
                }catch (Exception e){
                    continue;
                }

                String ownerContacts="";
                try {
                    ownerContacts = row.getCell(10).getStringCellValue().trim();    // контакты собственника , тутсразку все напихано в кучу String

                } catch (Exception e) {
                    LOG.info(e.getMessage(), e);
                }

                /**создание обьекта либо взятие уже существующего
                 *
                 * */
                LiveSaleRealty liveSaleRealty;
                 try {
                    liveSaleRealty = getOrCreate(localId, ownerContacts);
                }catch (Exception e){
                    continue;

                }
                    FiasAddressVector fiasAddressVector = null;
                    String region = "Калужская";
                    String city = "Калуга";

                /*получение адрес вектора*/
                    try {
                        fiasAddressVector = new FiasAddressVector();
                        fiasAddressVector.setLevel1(fiasObjectDAO.findRegionByName(region));
                        fiasAddressVector.setLevel4(fiasObjectDAO.findCityByName(city));
                        String street = row.getCell(1).getStringCellValue().trim();
                        FiasObject streetObject = cianLiveSaleOfferParserTask.findFiasObjectInLevel(6, street, fiasAddressVector.getLevel4().getAOGUID());
                        fiasAddressVector.setLevel7(streetObject);
                    } catch (Exception e) {
                        LOG.info(e.getMessage(), e);
                    }

                    /**эта непонятная операция нужна для того , чтобы сделать обьект уникальным
                     *так как в нашей базе нет полей для уникальной индентификации загруженных обьектов
                     * являющихся в то же время обьектами собственного агентства
                     * в качастве поля куда записывается индентификатор из xlsx используем RealtyObject.linkToCian name
                     * да простят нас боги за такую ересь
                     */
                    DwellingHouse dwellingHouse = (DwellingHouse) liveSaleRealty.getBuilding();

                    liveSaleRealty.setOldDbIdentifier(localId);

                    liveSaleRealty.getAddress().setFiasAddressVector(fiasAddressVector);                      //записываем адресс

                    String rooms = row.getCell(0).getStringCellValue().replaceAll("[^0-9]", "");             //количество комнат , требуется преобразование к числу
                    try {
                        liveSaleRealty.getArea().setRoomsCount(Integer.parseInt(rooms));
                    } catch (Exception e) {
                        LOG.info(e.getMessage(), e);
                    }

                    try {
                        double floor = row.getCell(3).getNumericCellValue();            //цифра int этаж
                        liveSaleRealty.getBuilding().setFloor((int) floor);
                    } catch (Exception e) {
                        LOG.info(e.getMessage(), e);
                    }

                    try {
                        double floors = row.getCell(4).getNumericCellValue();            //цифра int этажей
                        liveSaleRealty.getBuilding().setFloorTotal((int) floors);
                    } catch (Exception e) {
                        LOG.info(e.getMessage(), e);
                    }

                    try {
                        double totalArea = row.getCell(6).getNumericCellValue();        // общая площадь int
                        liveSaleRealty.getArea().setTotal(totalArea);
                    } catch (Exception e) {
                        LOG.info(e.getMessage(), e);
                    }

                    try {
                        String note = row.getCell(13).getStringCellValue().trim();             //Текст обьявления String
                        liveSaleRealty.setNote(note);
                    } catch (Exception e) {
                        LOG.info(e.getMessage(), e);
                    }

                    try {
                        double agencyPrice = row.getCell(12).getNumericCellValue();      //цена агентства
                        liveSaleRealty.getPrice().setValue((int) agencyPrice);

                   /* цена собственника  ,но так как у нас нет графы цена собственника
                   * мы на основании цены агентства и цены собственника вычисляем комиссию агентства .
                   * пока без проверки на то что оба этих числа корректны
                   * в комиссия агенства это число (изн.цена  + %агенства*изн.цена = цена написанная в графе цена)
                   */
                        double ownerPrice = row.getCell(11).getNumericCellValue();
                        Double agencyCommission = ((agencyPrice - ownerPrice) / ownerPrice) * 100;
                        liveSaleRealty.getCommission().setAgencyCommission(agencyCommission);
                    } catch (Exception e) {
                        LOG.info(e.getMessage(), e);
                    }

                /* слдующие параметры существкю не для всех типов обьектов , то есть нельзя применить для RealtyObject*/
                    try {
                        String houseType = row.getCell(5).getStringCellValue().trim();        // К или П (кирпичный , панельный)
                        if (houseType.equals("П")) {

                            dwellingHouse.setBuildingType(BuildingType.PANEL);
                        } else if (houseType.equals("К")) {
                            dwellingHouse.setBuildingType(BuildingType.BRICK);
                        }
                    } catch (Exception e) {
                        LOG.info(e.getMessage(), e);
                    }

                    LiveArea liveArea1 = (LiveArea) liveSaleRealty.getArea();
                    try {
                        double liveArea = row.getCell(7).getNumericCellValue();          //жилая плщадь int
                        liveArea1.setLiving(liveArea);
                    } catch (Exception e) {
                        LOG.info(e.getMessage(), e);
                    }

                    try {
                        double kitchenArea = row.getCell(8).getNumericCellValue();       //площадь кухни int
                        liveArea1.setKitchen(kitchenArea);
                    } catch (Exception e) {
                        LOG.info(e.getMessage(), e);
                    }
                /* этот блок для парсинга поля содержащенго информацию о балконах и лоджии
                * само поле в xlxs может иметь значения
                * б/б - без болконов и лоджий
                * Б - балкон
                * Л - лоджия
                * nЛ - n лоджий ,nЛ - n балконов
                *
                * */
                    try {

                        String lodgiaBalcony = row.getCell(9).getStringCellValue().trim();

                        if (lodgiaBalcony.matches("[0-9].*")) {                   //проверка на наличе цифр
                            int amount = Integer.parseInt(lodgiaBalcony.replaceAll("[^0-9]", ""));
                            String type = lodgiaBalcony.replaceAll("[0-9]", "").trim();
                            if (type.equals("Б")) {
                                ((LiveArea) liveSaleRealty.getArea()).setBalconyCount(amount);
                                ((LiveArea) liveSaleRealty.getArea()).setRecessedBalconyCount(0);

                            } else if (type.equals("Л")) {                         //еесли тип лоджия
                                ((LiveArea) liveSaleRealty.getArea()).setBalconyCount(0);
                                ((LiveArea) liveSaleRealty.getArea()).setRecessedBalconyCount(amount);
                            }
                        } else if (lodgiaBalcony.equals("б/б")) {                         //если тип без балкона

                            ((LiveArea) liveSaleRealty.getArea()).setRecessedBalconyCount(0);
                            ((LiveArea) liveSaleRealty.getArea()).setBalconyCount(0);

                        } else if (lodgiaBalcony.equals("Б")) {                            // если тип наличие балкона

                            ((LiveArea) liveSaleRealty.getArea()).setRecessedBalconyCount(0);
                            ((LiveArea) liveSaleRealty.getArea()).setBalconyCount(1);

                        } else if (lodgiaBalcony.equals("Л")) {                           //если тип наличие лождии

                            ((LiveArea) liveSaleRealty.getArea()).setRecessedBalconyCount(1);
                            ((LiveArea) liveSaleRealty.getArea()).setBalconyCount(0);

                        }

                    } catch (Exception e) {
                        LOG.info(e.getMessage(), e);
                    }

                    liveSaleRealty.setAgent(authService.getLogginedAgent());

                    realtyObjectDAO.saveOrUpdate(liveSaleRealty);
                    countObjects++;                 // подсчет количества сохраненных объектов

            }
        }
        Messagebox.show("импортировано:"+ countObjects+ " объектов", "Сообщение", Messagebox.OK, Messagebox.INFORMATION);
        BindUtils.postGlobalCommand(null, null, "refreshRealtyObjectList", null);
    }

    /**
     * функция для создания или взятия обьекта liveSaleRealty и
     * записи контактов если в случае если их еще нету
     * @param id индентификатор обьекта из exel
     * @param contacts контакты собственника из exel
     * @return возваращет LiveSaleRealty
     */
    public LiveSaleRealty getOrCreate(Long id, String contacts){

        ContactsOfOwner contactsOfOwner = null;
        LiveSaleRealty liveSaleRealty;
        liveSaleRealty = liveSaleRealtyDAO.findByOldDbIdentifier(id);

        if(liveSaleRealty==null){                   //если обьекта в базе нету

            liveSaleRealty = new LiveSaleRealty();
            liveSaleRealty.setRealtyObjectType(RealtyObjectType.LIVE_SALE);
            realtyObjectDAO.saveOrUpdate(liveSaleRealty);

            if((contacts!=null)&&(StringUtils.hasLength(contacts))){      //если данные считанные с exel не пустые

                contactsOfOwner = new ContactsOfOwner();
                contactsOfOwner.setName(contacts);
                contactsOfOwner.setRealtyObject(liveSaleRealty);
                contactsOfOwnerDAO.saveOrUpdate(contactsOfOwner);

            }

            return liveSaleRealty;

        }
        else {                                  //если обьект есть в базе

            if(StringUtils.hasLength(contacts)){        //если считанные из exel контакты не пустые

                if( contactsOfOwnerDAO.findByRealtyObject(liveSaleRealty, contacts).size()!=0){
                    contactsOfOwner = contactsOfOwnerDAO.findByRealtyObject(liveSaleRealty, contacts).get(0);
                }

                if(contactsOfOwner==null){              // если в базе таких контактов еще нету
                    contactsOfOwner = new ContactsOfOwner();
                    contactsOfOwner.setRealtyObject(liveSaleRealty);
                    contactsOfOwner.setName(contacts);
                    contactsOfOwnerDAO.saveOrUpdate(contactsOfOwner);
                }

            }

            return liveSaleRealty;

        }

    }

}
