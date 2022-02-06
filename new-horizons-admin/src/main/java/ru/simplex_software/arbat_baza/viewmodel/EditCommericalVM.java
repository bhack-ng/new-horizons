package ru.simplex_software.arbat_baza.viewmodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;
import ru.simplex_software.arbat_baza.AbstractArea;
import ru.simplex_software.arbat_baza.GeocodeService;
import ru.simplex_software.arbat_baza.dao.AgentDAO;
import ru.simplex_software.arbat_baza.dao.MetroStationDAO;
import ru.simplex_software.arbat_baza.dao.ObjectStatusDAO;
import ru.simplex_software.arbat_baza.dao.StreetDAO;
import ru.simplex_software.arbat_baza.dao.commerce.CommerceDAO;
import ru.simplex_software.arbat_baza.model.Agent;
import ru.simplex_software.arbat_baza.model.BuildingClass;
import ru.simplex_software.arbat_baza.model.BuildingEntrance;
import ru.simplex_software.arbat_baza.model.BuildingLiveType;
import ru.simplex_software.arbat_baza.model.BuildingStatus;
import ru.simplex_software.arbat_baza.model.GeoCoordinates;
import ru.simplex_software.arbat_baza.model.MetroStation;
import ru.simplex_software.arbat_baza.model.ObjectStatus;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.arbat_baza.model.RealtyObjectType;
import ru.simplex_software.arbat_baza.model.Street;
import ru.simplex_software.arbat_baza.model.commerce.Commerce;
import ru.simplex_software.arbat_baza.model.commerce.CommerceType;
import ru.simplex_software.arbat_baza.model.commerce.ContractType;
import ru.simplex_software.arbat_baza.model.commerce.CommerceOptions;
import ru.simplex_software.arbat_baza.model.price.CommercePrice;
import ru.simplex_software.arbat_baza.model.price.Currency;
import ru.simplex_software.arbat_baza.model.price.Period;
import ru.simplex_software.arbat_baza.model.price.SimplePrice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * View Model for src/main/webapp/WEB-INF/zul/editCommercial.zul
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class EditCommericalVM extends EditRealtyObject {
    private static final Logger LOG = LoggerFactory.getLogger(EditCommericalVM.class);

    @WireVariable
    private ObjectStatusDAO objectStatusDAO;
    @WireVariable
    private MetroStationDAO metroStationDAO;
    @WireVariable
    private StreetDAO streetDAO;
    @WireVariable
    private CommerceDAO commerceDAO;
    @WireVariable
    private AgentDAO agentDAO;
    @WireVariable
    private GeocodeService geocodeService;


    private List<MetroStation> metroStations;

    private List<ObjectStatus> statuses;
    private List<Street> allStreets;
    private List<Agent> agentList;


    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window view
            , @ExecutionArgParam("realtyObject") Commerce realtyObject, @ExecutionArgParam("tab") Integer tab) {
        super.afterCompose(view, realtyObject, tab);
    }

    public CommerceType[] getCommerceTypes() {
        return CommerceType.values();
    }

    public ContractType[] getContractTypes() {
        if (getRealtyObject().getRealtyObjectType() == RealtyObjectType.COMMERCE_LEASE) {
            return new ContractType[]{ContractType.DIRECT_LEASING, ContractType.SUBLEASE, ContractType.CONTRACT_OF_JOINT_ACTIVITY};
        } else {
            return new ContractType[]{ContractType.SALE_REALTY_OBJECT, ContractType.SALE_OF_LEASEHOLD};
        }
    }

    private void refreshGeo(Event event) {
        Map.Entry<String, GeoCoordinates> selectedCoord = (Map.Entry<String, GeoCoordinates>) event.getData();
        getRealtyObject().getAddress().getGeoCoordinates().setLongitude(selectedCoord.getValue().getLongitude());
        getRealtyObject().getAddress().getGeoCoordinates().setLatitude(selectedCoord.getValue().getLatitude());
        BindUtils.postNotifyChange(null, null, this, "*");
    }
    /**
    * Открывает окно, в котором будут выведены результаты расчетов
    * */

    @Command
    public void openCalculateWindow() {
        Map<String, RealtyObject> map = new HashMap<>();
        map.put("realtyObject", realtyObject);
        if (validateCommerce()) {
            Component component = Executions.getCurrent().createComponents("/showRentPriceCalculator.zul", null, map);
        }
    }


    /**
     * Проверяет наличие данных о площади коммерческой недвижимости и цене и подсвечивает ссылку (disabled)
     * */
    @Command
    @NotifyChange("countPrice")
    public boolean priceCalculator() {
        return isCountPrice();
    }

    /**
     * Проверяет наличие всех сведений для отображения данных о расчете цены
     * */

    private boolean validateCommerce() {
        if ((realtyObject.getPrice().getValue() == null) || (realtyObject.getArea().getTotal() == null)) {
            Clients.showNotification("Введены не все данные!!! Проверьте наличие данных в полях: 'Общая площадь' и 'Цена'," +
                            " после завершения введения данных перейдите на любое поле и только затем пройдите по ссылке...",
                                        Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 15000, true);
            return false;
        }
        if (!(Currency.EUR.equals(realtyObject.getPrice().getCurrency()) ||
                Currency.USD.equals(realtyObject.getPrice().getCurrency()) || Currency.RUB.equals(realtyObject.getPrice().getCurrency()))) {
            Clients.showNotification("Укажите корректное значение валюты...", Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 5000, true);
            return false;
        }

        CommercePrice price = (CommercePrice) realtyObject.getPrice();

        if (!(Period.M2_PER_YEAR.equals(price.getPeriod()) || Period.MONTH.equals(price.getPeriod()))) {
            Clients.showNotification("Укажите корректный период...", Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 5000, true);
            return false;
        }
        return true;
    }

    public boolean isCountPrice() {
        Commerce commerce = (Commerce) realtyObject;
        return (commerce.getPrice().getValue() == null) ||
                (realtyObject.getArea().getTotal() == null);
    }

    public Period[] getPeriodList() {
        return Period.values();
    }

    public BuildingLiveType[] getLiveTypes() {
        return BuildingLiveType.values();
    }

    public BuildingEntrance[] getEntrances() {
        return BuildingEntrance.values();
    }

    public CommerceOptions.MebelExistance[] getMebelExistanceValues() {
        return CommerceOptions.MebelExistance.values();
    }

    public BuildingStatus[] getOptionsStatuses() {
        return BuildingStatus.values();
    }

    public BuildingClass[] getBuildingClass() {
        return BuildingClass.values();
    }


}
