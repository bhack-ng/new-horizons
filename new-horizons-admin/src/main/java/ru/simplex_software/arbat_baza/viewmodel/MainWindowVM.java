package ru.simplex_software.arbat_baza.viewmodel;

import org.slf4j.IMarkerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.BasicMarkerFactory;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.Converter;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;
import ru.simplex_software.arbat_baza.AuthService;
import ru.simplex_software.arbat_baza.EnumConverter;
import ru.simplex_software.arbat_baza.dao.AgentDAO;
import ru.simplex_software.arbat_baza.dao.MetroStationDAO;
import ru.simplex_software.arbat_baza.dao.ObjectStatusDAO;
import ru.simplex_software.arbat_baza.dao.PhotoDAO;
import ru.simplex_software.arbat_baza.dao.RealtyFilterDAO;
import ru.simplex_software.arbat_baza.dao.RealtyObjectByFilterDAOImpl;
import ru.simplex_software.arbat_baza.dao.RealtyObjectDAO;
import ru.simplex_software.arbat_baza.dao.StreetDAO;
import ru.simplex_software.arbat_baza.dao.fias.FiasObjectDAO;
import ru.simplex_software.arbat_baza.dao.odor.ExternalAgencyDAO;
import ru.simplex_software.arbat_baza.model.Agent;
import ru.simplex_software.arbat_baza.model.BuildingType;
import ru.simplex_software.arbat_baza.model.Comment;
import ru.simplex_software.arbat_baza.model.MetroStation;
import ru.simplex_software.arbat_baza.model.ObjectStatus;
import ru.simplex_software.arbat_baza.model.RealtyFilter;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.arbat_baza.model.RealtyObjectType;
import ru.simplex_software.arbat_baza.model.SortByField;
import ru.simplex_software.arbat_baza.model.Street;
import ru.simplex_software.arbat_baza.model.commerce.Commerce;
import ru.simplex_software.arbat_baza.model.fias.FiasAddressVector;
import ru.simplex_software.arbat_baza.model.odor.ExternalAgency;
import ru.simplex_software.arbat_baza.model.price.Currency;
import ru.simplex_software.arbat_baza.model.price.Period;
import ru.simplex_software.arbat_baza.viewmodel.price.PriceRenderer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * .
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class MainWindowVM {
    private static final Logger LOG = LoggerFactory.getLogger(MainWindowVM.class);
    private static final IMarkerFactory MARKER_FACTORY = new BasicMarkerFactory();
    private Window mainWin;
    @WireVariable
    private RealtyObjectByFilterDAOImpl realtyObjectByFilterDAOImpl;
    @WireVariable
    private AgentDAO agentDAO;
    @WireVariable
    private ExternalAgencyDAO externalAgencyDAO;
    @WireVariable
    private ObjectStatusDAO objectStatusDAO;
    @WireVariable
    private MetroStationDAO metroStationDAO;
    @WireVariable
    private StreetDAO streetDAO;
    @WireVariable
    private RealtyFilterDAO realtyFilterDAO;
    @WireVariable
    private RealtyObjectDAO realtyObjectDAO;
    @WireVariable
    private AuthService authService;
    @WireVariable
    private PhotoDAO photoDAO;
    @WireVariable
    private FiasObjectDAO fiasObjectDAO;
    private List<Agent> agentList;
    private List<ExternalAgency> externalAgencyList;
    private List<ObjectStatus> objectStatuses;
    private RealtyFilter filter;
    private List<MetroStation> metroStations;
    private List<Street> allStreets;
    private Converter enumFromModelConverter = new EnumConverter("ru.simplex_software.arbat_baza.model");
    private Converter enumFromPriceConverter = new EnumConverter("ru.simplex_software.arbat_baza.model.price");
    private FlatDescriptionRenderer flatDescriptionRenderer = new FlatDescriptionRenderer();
    private LocationRenderer locationRenderer = new LocationRenderer();
    private PriceRenderer priceRenderer = new PriceRenderer();
    private String iconURL = "img/liveRealty.png";
    private boolean photoVisible;
    /**
     * выбраны ли внешние обьекты или объекты собственного агентства.
     */
    private boolean external;

    private RealtyObjectListModel realtyObjectListModel;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window view) {
        mainWin = view;

        realtyObjectListModel = new RealtyObjectListModel(authService, realtyObjectDAO, realtyObjectByFilterDAOImpl);
        agentList = agentDAO.findAll();
        Agent logginedAgent = authService.getLogginedAgent();
        filter = logginedAgent.getFilter();

        externalAgencyList = externalAgencyDAO.findAll();

        if (Agent.Role.ADMIN != logginedAgent.getRole()) {
            if (!logginedAgent.equals(filter.getAgentToFilter())) {
                filter.setAgentToFilter(logginedAgent);
                realtyFilterDAO.saveOrUpdate(filter);
                LOG.warn(MARKER_FACTORY.getMarker("security"), "user try use filter not allowet for him. is role of user changed?");
            }
        }

        objectStatuses = objectStatusDAO.findAll();
        metroStations = metroStationDAO.findAll();
        if (filter.getStreet() != null) {
            allStreets = new ArrayList<>(1);
            allStreets.add(filter.getStreet());
        }
        refreshRealtyObjectList();
        if (filter.getRealtyObjectType() == RealtyObjectType.COMMERCE_LEASE) {
            iconURL = "img/commericalRealtyRent.png";
        } else if (filter.getRealtyObjectType() == RealtyObjectType.COMMERCE_SALE) {
            iconURL = "img/commericalRealtySale.png";
        } else if (filter.getRealtyObjectType() == RealtyObjectType.LIVE_SALE) {
            iconURL = "img/liveRealtySale.png";
        } else if (filter.getRealtyObjectType() == RealtyObjectType.LIVE_LEASE) {
            iconURL = "img/liveRealtyRent.png";
        } else if (filter.getRealtyObjectType() == RealtyObjectType.PRIVATE_HOUSE_RENT) {
            iconURL = "img/privateHouseRent.png";
        } else if (filter.getRealtyObjectType() == RealtyObjectType.PRIVATE_HOUSE_SALE) {
            iconURL = "img/privateHouseSale.png";
        } else if (filter.getRealtyObjectType() == RealtyObjectType.STEAD_SALE) {
            iconURL = "img/steadSale.png";
        } else if (filter.getRealtyObjectType() == RealtyObjectType.STEAD_RENT) {
            iconURL = "img/steadRent.png";
        }
        external = filter.isExternal();
    }

    @GlobalCommand
    public void refreshRealtyObjectList() {
        filter = authService.getLogginedAgent().getFilter();
        realtyObjectListModel.refresh();
    }

    @Command
    public void editRealtyObject(@BindingParam("on") RealtyObject realtyObject, @BindingParam("tab") Integer tab) {
        HashMap params = new HashMap();
        params.put("realtyObject", realtyObject);
        params.put("tab", tab);
        Component editWin = null;
        if (filter.getRealtyObjectType() == RealtyObjectType.COMMERCE_LEASE
                || filter.getRealtyObjectType() == RealtyObjectType.COMMERCE_SALE) {
            editWin = Executions.createComponents("/WEB-INF/zul/editCommercial.zul", mainWin, params);
        } else if (filter.getRealtyObjectType() == RealtyObjectType.LIVE_LEASE) {
            editWin = Executions.createComponents("/WEB-INF/zul/editLiveRent.zul", mainWin, params);
        } else if (filter.getRealtyObjectType() == RealtyObjectType.LIVE_SALE) {
            editWin = Executions.createComponents("/WEB-INF/zul/editLiveSale.zul", mainWin, params);
        } else {
            editWin = Executions.createComponents("/WEB-INF/zul/outOfTownSale.zul", mainWin, params);
        }

        editWin.addEventListener("onChangeList", e -> {
                    realtyObjectListModel.setSelection(Collections.singletonList(realtyObject));
                    refreshRealtyObjectList();
                }
        );
    }

    @Command
    public void createRealtyObject() {
        HashMap params = new HashMap();
        RealtyObject realtyObject = RealtyObject.create(filter.getRealtyObjectType());
        final FiasAddressVector defaultAddress = authService.getLogginedAgent().getAgency().getDefaultAddress();

        realtyObject.getAddress().getFiasAddressVector().setLevel1(defaultAddress.getLevel1());
        realtyObject.getAddress().getFiasAddressVector().setLevel2(defaultAddress.getLevel2());
        realtyObject.getAddress().getFiasAddressVector().setLevel3(defaultAddress.getLevel3());
        realtyObject.getAddress().getFiasAddressVector().setLevel4(defaultAddress.getLevel4());
        realtyObject.getAddress().getFiasAddressVector().setLevel5(defaultAddress.getLevel5());
        realtyObject.getAddress().getFiasAddressVector().setLevel6(defaultAddress.getLevel6());


        realtyObject.setAgent(authService.getLogginedAgent());
        params.put("realtyObject", realtyObject);
        Component editWin = null;
        if (filter.getRealtyObjectType() == RealtyObjectType.COMMERCE_LEASE
                || filter.getRealtyObjectType() == RealtyObjectType.COMMERCE_SALE) {
            editWin = Executions.createComponents("/WEB-INF/zul/editCommercial.zul", mainWin, params);
        } else if (filter.getRealtyObjectType() == RealtyObjectType.LIVE_LEASE) {
            editWin = Executions.createComponents("/WEB-INF/zul/editLiveRent.zul", mainWin, params);
        } else if (filter.getRealtyObjectType() == RealtyObjectType.LIVE_SALE) {
            editWin = Executions.createComponents("/WEB-INF/zul/editLiveSale.zul", mainWin, params);
        } else {
            editWin = Executions.createComponents("/WEB-INF/zul/outOfTownSale.zul", mainWin, params);
        }
        editWin.addEventListener("onChangeList", e -> {
            realtyObjectListModel.setSelection(Collections.singletonList(realtyObject));
            refreshRealtyObjectList();
        });
    }

    /**
     * Переход к окну разметки объекта на плане.
     */
    @Command
    public void bindRealtyObject(@BindingParam("realtyObject") RealtyObject realtyObject) {

        // Сборка параметров.
        Map<String, RealtyObject> map = new HashMap<>();
        map.put("realtyObject", realtyObject);

        // Открытие окна.
        Executions.getCurrent().createComponents("/bindRealtiesToFloorScheme.zul", mainWin, map);
    }

    @Command
    @NotifyChange("*")
    public void doFilter() {
        LOG.debug("doFilter");
        realtyFilterDAO.saveOrUpdate(filter);
        external = filter.isExternal();
        refreshRealtyObjectList();
    }

    @Command
    @NotifyChange("filter")
    public void doClear() {
        LOG.debug("doClear");
        filter = realtyFilterDAO.get(filter.getId());
        if (authService.getLogginedAgent().getRole() == Agent.Role.ADMIN) {
            filter.setAgentToFilter(null);
        }
        filter.clearUserFields();

        realtyFilterDAO.saveOrUpdate(filter);
    }

    @GlobalCommand
    public void searchRealtyObjects(@BindingParam("type") RealtyObjectType type) {
        refreshRealtyObjectList();
        BindUtils.postNotifyChange(null, null, this, "filter");
    }

    @Command
    @NotifyChange("allStreets")
    public void streetComboTyped(@ContextParam(ContextType.TRIGGER_EVENT) InputEvent event) {
        String value = event.getValue();
        if (value != null && value.length() > 2) {
            allStreets = streetDAO.findByFilterCaseInsensetive("%" + value + "%");
        } else {
            allStreets = null;
        }
    }

    // Статус кнопки привяки к планам.
    public boolean isCommerce(RealtyObject realtyObject) {
        return realtyObject instanceof Commerce;
    }

    public Long getMainPhotoId(RealtyObject ro) {
        List<Long> mainPhotoId = photoDAO.getMainPhotoId(ro, 1);
        if (mainPhotoId.size() > 0) {
            return mainPhotoId.get(0);
        } else {
            return null;
        }
    }

    public Comment getLastComment(RealtyObject ro) {
        if (ro.getComments().isEmpty()) {
            return null;
        }
        return ro.getComments().get(0);
    }

    //todo i18n
    public String getLastCommentText(RealtyObject ro) {
        Comment comment = getLastComment(ro);
        if (comment == null) {
            return null;
        }
        DateFormat dateInstance = SimpleDateFormat.getDateInstance();
        StringBuilder sb = new StringBuilder();
        sb.append("Автор: ");
        sb.append(comment.getAuthor().getFio());

        sb.append(" Создан: ");
        sb.append(dateInstance.format(comment.getCreated()));
        sb.append('\n');
        sb.append(comment.getText());
        return sb.toString();
    }

    public String setPreviewPhotoUrl(RealtyObject ro) {
        String photoURL;
        photoVisible = false;
        if ((ro.getExternalObectExt() == null)) {
            if (getMainPhotoId(ro) != null) {
                photoURL = "photo?id=" + getMainPhotoId(ro) + "&amp;preview=1";
                photoVisible = true;
            } else {
                photoURL = null;
                photoVisible = false;
            }
        } else if (ro.getExternalObectExt().getExternalPhoto().size() != 0) {
            photoURL = ro.getExternalObectExt().getExternalPhoto().get(0);
            photoVisible = true;
        } else {
            photoVisible = false;
            photoURL = null;
        }
        return photoURL;
    }

    public List<Agent> getAgentList() {
        return agentList;
    }

    public RealtyFilter getFilter() {
        return filter;
    }

    public List<ObjectStatus> getObjectStatuses() {
        return objectStatuses;
    }

    public List<MetroStation> getMetroStations() {
        return metroStations;
    }

    public List<Street> getAllStreets() {
        return allStreets;
    }

    public BuildingType[] getBuildingTypes() {
        return BuildingType.values();
    }

    private static final SortByField[] sortFields = SortByField.values();

    public SortByField[] getSortFields() {
        return sortFields;
    }

    public boolean isLogginedUserAdmin() {
        return Agent.Role.ADMIN == authService.getLogginedAgent().getRole();
    }

    public Converter getEnumFromModelConverter() {
        return enumFromModelConverter;
    }

    public Converter getEnumFromPriceConverter() {
        return enumFromPriceConverter;
    }

    public Currency[] getCurrencies() {
        return Currency.values();
    }

    public Period[] getPeriods() {
        return Period.values();
    }

    public FlatDescriptionRenderer getFlatDescriptionRenderer() {
        return flatDescriptionRenderer;
    }

    public RealtyObjectListModel getRealtyObjectListModel() {
        return realtyObjectListModel;
    }

    public PriceRenderer getPriceRenderer() {
        return priceRenderer;
    }

    public LocationRenderer getLocationRenderer() {
        return locationRenderer;
    }

    public String getIconURL() {
        return iconURL;
    }

    public List<ExternalAgency> getExternalAgencyList() {
        return externalAgencyList;
    }

    public boolean isPhotoVisible() {
        return photoVisible;
    }

    public void setPhotoVisible(boolean photoVisible) {
        this.photoVisible = photoVisible;
    }

    public boolean isExternal() {
        return external;
    }

    public void setExternal(boolean external) {
        this.external = external;
    }
}
