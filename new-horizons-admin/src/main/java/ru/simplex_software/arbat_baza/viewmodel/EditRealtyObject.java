package ru.simplex_software.arbat_baza.viewmodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.Converter;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Window;
import ru.simplex_software.arbat_baza.AuthService;
import ru.simplex_software.arbat_baza.EnumConverter;
import ru.simplex_software.arbat_baza.GeocodeService;
import ru.simplex_software.arbat_baza.dao.AgentDAO;
import ru.simplex_software.arbat_baza.dao.MetroStationDAO;
import ru.simplex_software.arbat_baza.dao.ObjectStatusDAO;
import ru.simplex_software.arbat_baza.dao.RealtyObjectDAO;
import ru.simplex_software.arbat_baza.dao.StreetDAO;
import ru.simplex_software.arbat_baza.dao.fias.FiasObjectDAO;
import ru.simplex_software.arbat_baza.model.Agent;
import ru.simplex_software.arbat_baza.model.BuildingType;
import ru.simplex_software.arbat_baza.model.GeoCoordinates;
import ru.simplex_software.arbat_baza.model.MetroStation;
import ru.simplex_software.arbat_baza.model.ObjectStatus;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.arbat_baza.model.Street;
import ru.simplex_software.arbat_baza.model.price.Currency;
import ru.simplex_software.arbat_baza.xml.IpfsPublisher;
import ru.simplex_software.school_mark.view.Jsr310Converter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Редактирование объекта недвижимости.
 */
public class EditRealtyObject {
    private static final Logger LOG = LoggerFactory.getLogger(EditRealtyObject.class);

    @WireVariable
    private ObjectStatusDAO objectStatusDAO;

    @WireVariable
    private MetroStationDAO metroStationDAO;

    @WireVariable
    private StreetDAO streetDAO;

    @WireVariable
    private RealtyObjectDAO realtyObjectDAO;

    @WireVariable
    private AgentDAO agentDAO;

    @WireVariable
    private GeocodeService geocodeService;

    @WireVariable
    private AuthService authService;

    @WireVariable
    private FiasObjectDAO fiasObjectDAO;

    @WireVariable
    private IpfsPublisher ipfsPublisher;

    private List<MetroStation> metroStations;
    private List<ObjectStatus> statuses;
    private List<Street> allStreets;
    private List<Agent> agentList ;

    // Конвертеры.
    private Jsr310Converter jsr310Converter=new Jsr310Converter();
    private Converter enumFromModelConverter=new EnumConverter("ru.simplex_software.arbat_baza.model");
    private Converter enumFromPriceConverter=new EnumConverter("ru.simplex_software.arbat_baza.model.price");
    private Converter enumFromCommerceConverter=new EnumConverter("ru.simplex_software.arbat_baza.model.commerce");

    protected Window editWin;
    protected RealtyObject realtyObject;

    // Открытие панелей.
    private boolean isPhotoPanelOpen = false;
    private boolean isContactsOfOwnerPanelOpen = false;
    private boolean isRecommendationsPanelOpen = false;
    private boolean isCommentsPanelOpen = false;
    private boolean isPublicationPanelOpen = false;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window view,
                             @ExecutionArgParam("realtyObject") RealtyObject realtyObject,
                             @ExecutionArgParam("tab") Integer tab) {
        this.editWin = view;
        this.realtyObject=realtyObject;
        if (realtyObject.getId() != null) {
            this.realtyObject = realtyObjectDAO.get(realtyObject.getId());
        }

        /*
        else{
                    final FiasObject[] defaultFiaAddress = authService.getLogginedAgent().getFilter().getFiasAddressVector().toArray();
            realtyObject.getAddress().getFiasAddressVector().setLevels(defaultFiaAddress);
        }
        */

        metroStations=metroStationDAO.findAll();

        statuses=objectStatusDAO.findAll();
        if(realtyObject.getAddress().getStreet()!=null){
            allStreets = new ArrayList<>(1);
            allStreets.add(realtyObject.getAddress().getStreet());
        }
        agentList=agentDAO.findAll();
        if(tab!=null){
            Tabbox t1=(Tabbox)editWin.getFirstChild().getFellow("tabbox");
            t1.setSelectedIndex(tab);
            if(tab==3){
                openCommentsPanel();
            }
        }
    }

    /**
     * Сохранение объекта.
     */
    @Command
    public void save(){
//        if (!validate()){
//            return;
//        }
        LOG.info("called method 'save' in EditRealtyObject");

        realtyObject.setChanged(LocalDateTime.now());
        if(realtyObject.getId()==null) {
            realtyObjectDAO.saveOrUpdate(realtyObject);
        }else {
            realtyObjectDAO.merge(realtyObject);
        }
        try{
            ipfsPublisher.publishAll();
        }catch (Exception ex){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String sStackTrace = sw.toString();
            Messagebox.show(sStackTrace, ex.getMessage(),Messagebox.OK, Messagebox.ERROR);
        }

        Events.postEvent("onChangeList", editWin, null);
        editWin.detach();
    }

//    protected boolean validate() {
//        String message = "Обязательные поля должны быть заполнены!";
//        boolean isValidate = true;
//        ObjectStatus status = realtyObject.getObjectStatus();
//
//
//        if(status ==null || status.getId()!=1) {
//            return isValidate;
//        }
//
//
//           //проверка ввода адреса
//            if(realtyObject.getAddress().getFiasAddressVector().getLevel7()==null){
//                message = message + "\n Адрес объекта";
//                isValidate = false;
//            }
//            //проверка данных метро. Только для Москвы
//            if(realtyObject.getAddress().getFiasAddressVector().getLevel1()!=null &&
//            FiasDataloader.MOSCOW_GAOUID.equals(realtyObject.getAddress().getFiasAddressVector().getLevel1().getAOGUID())){
//               if(realtyObject.getAddress()!=null){
//                    if(realtyObject.getAddress().getFiasAddressVector()!=null){
//                            if(realtyObject.getAddress().getFiasAddressVector().getLevel4()!=null){
//
//                if (realtyObject.getAddress().getFiasAddressVector().getLevel4().getFORMALNAME().toString().equals("Москва")){
//                if(realtyObject.getMetroLocation().getMetroStation()==null) {
//                     message = message+"\n Станция метро";
//                     isValidate=false;
//                }
//                }
//            }}}}
//
//            //проверка цены: стоимость
//        if (realtyObject.getPrice().getValue()==null){
//                message = message+"\n Цена";
//                isValidate = false;
//        }
//        //площадь
//        if (!(realtyObject instanceof Stead)){
//
//        if (realtyObject.getArea()==null||realtyObject.getArea().getTotal()==null){
//            isValidate = false;
//            message=message+"\n Площадь";
//        }}
//
//
//
//
//
//        if(realtyObject.getAddress().getFiasAddressVector().getLevel7()!=null) {
//            if (realtyObject.getAddress().getFiasAddressVector().getLevel7().getPrimaryKey()==null){
//                realtyObject.getAddress().getFiasAddressVector().setLevel7(null);
//            }
//        }
//
//        DwellingHouse building = (DwellingHouse) realtyObject.getBuilding();
//        if (building.getBuildingType()==null){
//            message=message + "\n" + "Тип здания";
//            isValidate = false;
//        }
//
//        LiveRentPrice  liveRentPrice = (LiveRentPrice) realtyObject.getPrice();
//        if((liveRentPrice.getPrepay()!=null)&&(liveRentPrice.getPrepay()>3)) {
//            message = message + "\n" + "Необходимо чтобы значение поля 'предоплата' было меньше 4 ";
//            isValidate = false;
//        }
//
//        if((realtyObject.getNote()==null) || !( StringUtils.hasLength(realtyObject.getNote().trim()) ) ){
//            message = message + "\n" + "Текст оъявления";
//            isValidate = false;
//        }
//            if (!isValidate) { Messagebox.show(message,"Внимание!", Messagebox.OK,Messagebox.ERROR);}
//        return isValidate;
//    }

    @Command
    @NotifyChange("allStreets")
    public void streetComboTyped(@ContextParam(ContextType.TRIGGER_EVENT) InputEvent event){
        String value = event.getValue();
        if(value!=null && value.length()>2) {
            allStreets = streetDAO.findByFilterCaseInsensetive("%" + value + "%");
        } else {
            allStreets = null;
        }
    }

    /**
     * Выбор гео-координат.
     */
    @Command
    public void selectGeoCoordinate() {
        HashMap<String,Object> arg = new HashMap<>();
        arg.put("realtyObject",realtyObject);
        Component geoWin= Executions.createComponents("/WEB-INF/zul/selectOnMap.zul", editWin, arg);
        geoWin.addEventListener("onChangeGeo", e -> refreshGeo(e));
    }

    /**
     * Открытие панели фотографий.
     */
    @Command
    public void openPhotoPanel(){
        if(!isPhotoPanelOpen){
            Component photoPanelTabPanel =editWin.getFellow("photoPanelTabPanel");
            HashMap params = new HashMap();
            params.put("realtyObject",realtyObjectDAO.get(realtyObject.getPrimaryKey()));

            if(realtyObject.getExternalObectExt()==null) {
                Executions.createComponents("/WEB-INF/zul/photoPanel.zul", photoPanelTabPanel, params);
            }
            else {
                Executions.createComponents("/WEB-INF/zul/externalPhotoPanel.zul", photoPanelTabPanel, params);
            }
            isPhotoPanelOpen = true;
        }
    }

    /**
     * Открытие панели контактов владельцев.
     */
    @Command
    public void openContactsOfOwnerPanel(){
        if(!isContactsOfOwnerPanelOpen){
            Component contactsOfOwnerPanel =editWin.getFellow("contactsOfOwnerPanel");
            HashMap params = new HashMap();
            params.put("realtyObject",realtyObjectDAO.get(realtyObject.getPrimaryKey()));
            Executions.createComponents("/WEB-INF/zul/contactsOfOwnerPanel.zul", contactsOfOwnerPanel , params);
            isContactsOfOwnerPanelOpen =true;
        }
    }

    /**
     * Открытие панели комментариев.
     */
    @Command
    public void openCommentsPanel(){
        if(!isCommentsPanelOpen){
            Component commentsPanel =editWin.getFellow("commentsPanel");
            HashMap params = new HashMap();
            params.put("realtyObject",realtyObjectDAO.get(realtyObject.getPrimaryKey()));
            Executions.createComponents("/WEB-INF/zul/commentsPanel.zul", commentsPanel , params);
            isCommentsPanelOpen =true;
        }
    }

    /**
     * Открытие панели рекоммендаций.
     */
    @Command
    public void openRecommendationsPanel(){
        if(!isRecommendationsPanelOpen){
            Component recomendationsPanel =editWin.getFellow("recomendationsPanel");
            HashMap params = new HashMap();
            params.put("realtyObject",realtyObjectDAO.get(realtyObject.getPrimaryKey()));
            Executions.createComponents("/WEB-INF/zul/recommendationsPanel.zul", recomendationsPanel , params);
            isRecommendationsPanelOpen=true;
        }
    }

    /**
     * Открытие панели публикации.
     */
    @Command
    public void openPublicationPanel() {
        if (!isPublicationPanelOpen) {
            Component publicationPanel = editWin.getFellow("publicationPanel");

            HashMap<String, RealtyObject> map = new HashMap<>();
            map.put("realtyObject", realtyObject);
            Executions.createComponents("/WEB-INF/zul/publicationPanel.zul", publicationPanel, map);

            isPublicationPanelOpen = true;
        }
    }

    /**
     * Отмена.
     */
    @Command
    public void cancel() {
        editWin.detach();
    }

    /**
     * Обновлеие гео-координат.
     */
    private void refreshGeo(Event event) {
        Map.Entry<String, GeoCoordinates> selectedCoord = (Map.Entry<String, GeoCoordinates>) event.getData();
        realtyObject.getAddress().getGeoCoordinates().setLongitude(selectedCoord.getValue().getLongitude());
        realtyObject.getAddress().getGeoCoordinates().setLatitude(selectedCoord.getValue().getLatitude());
        BindUtils.postNotifyChange(null, null, this, "*");
    }

    public String getChanged() {
        if (realtyObject.getChanged() == null) {
            return "";
        }
        return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).format(realtyObject.getChanged());
    }

    public List<Agent> getAgentList() {
        return agentList;
    }

    public RealtyObject getRealtyObject() {
        return realtyObject;
    }

    public List<MetroStation> getMetroStations() {
        return metroStations;
    }

    public List<ObjectStatus> getStatuses() {
        return statuses;
    }

    public List<Street> getAllStreets() {
        return allStreets;
    }

    public Jsr310Converter getJsr310Converter() {
        return jsr310Converter;
    }

    public Converter getEnumFromModelConverter() {
        return enumFromModelConverter;
    }

    public Converter getEnumFromPriceConverter() {
        return enumFromPriceConverter;
    }

    public Converter getEnumFromCommerceConverter() {
        return enumFromCommerceConverter;
    }

    public BuildingType[] getBuildingTypes() {
        return BuildingType.values();
    }

    public Currency[] getCurrencyList() {
        return Currency.values();
    }

    public boolean isViewMode() {
        return realtyObject.getExternalObectExt() != null;
    }
}
