package ru.simplex_software.arbat_baza.viewmodel;

import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import ru.simplex_software.arbat_baza.AuthService;
import ru.simplex_software.arbat_baza.dao.RealtyObjectDAO;
import ru.simplex_software.arbat_baza.model.Address;
import ru.simplex_software.arbat_baza.model.GeoCoordinates;
import ru.simplex_software.arbat_baza.model.MetroLocation;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.arbat_baza.model.commerce.Commerce;
import ru.simplex_software.arbat_baza.model.commerce.CommerceBuilding;
import ru.simplex_software.arbat_baza.model.fias.FiasAddressVector;
import ru.simplex_software.ord.FormGenerateOfficesValidator;

import java.time.LocalDateTime;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class GenerateOfficesVM {

    @WireVariable
    private RealtyObjectDAO realtyObjectDAO;

    @WireVariable
    private AuthService authService;

    private Data data = new Data();
    private Validator validator;

    @AfterCompose
    public void afterCompose() {
        this.validator = new FormGenerateOfficesValidator(realtyObjectDAO);
    }

    /**
     * Генерация офисов.
     */
    @Command
    public void submit(@ContextParam(ContextType.VIEW) Component component) {

        RealtyObject template = realtyObjectDAO.get(data.getId());

        for (int i = data.getMinNumber(); i <= data.getMaxNumber(); i++) {
            RealtyObject object = copyObject(template, i);
            realtyObjectDAO.saveOrUpdate(object);
        }

        component.detach();
    }

    /**
     * Закрытие окна.
     */
    @Command
    public void close(@ContextParam(ContextType.VIEW) Component component) {
        component.detach();
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Validator getValidator() {
        return validator;
    }

    /**
     * Содает копию коммерческого объекта недвижитмости.
     */
    private RealtyObject copyObject(RealtyObject template, int officeNumber) {
        Commerce result = (Commerce) RealtyObject.create(template.getRealtyObjectType());

        result.setAddress(copyAddress(template.getAddress(), officeNumber));
        result.setAgent(authService.getLogginedAgent());

        MetroLocation location = new MetroLocation();
        location.setAnyTime(template.getMetroLocation().getAnyTime());
        location.setWtimeSelected(template.getMetroLocation().isWtimeSelected());
        location.setMetroStation(template.getMetroLocation().getMetroStation());
        result.setMetroLocation(location);

        result.setChanged(LocalDateTime.now());
        result.setCommission(template.getCommission());
        result.setNote(template.getNote());
        result.setObjectStatus(template.getObjectStatus());

        Commerce comTemplate = (Commerce) template;
        result.setContractType(comTemplate.getContractType());
        result.setType(comTemplate.getType());

        CommerceBuilding building = new CommerceBuilding();
        building.setArea_b(comTemplate.getBuilding().getArea_b());
        building.setBuildingClass(comTemplate.getBuilding().getBuildingClass());
        building.setCeiling(comTemplate.getBuilding().getCeiling());
        building.setEnter(comTemplate.getBuilding().getEnter());
        building.setFloor(comTemplate.getBuilding().getFloor());
        building.setFloorTotal(comTemplate.getBuilding().getFloorTotal());
        building.setLiveType(comTemplate.getBuilding().getLiveType());
        building.setName_bc(comTemplate.getBuilding().getName_bc());
        building.setStatus_b(comTemplate.getBuilding().getStatus_b());
        result.setBuilding(building);

        /*CommerceArea area = new CommerceArea();
        area.setRoomsCount(comTemplate.getArea().getRoomsCount());
        area.setRooms(comTemplate.getArea().getRooms());
        area.setTotal(comTemplate.getArea().getTotal());
        result.setArea(area);*/

        result.setOptions(comTemplate.getOptions());
        result.setPrice(comTemplate.getPrice());

        return result;
    }

    /**
     * Создание копии адреса.
     */
    private Address copyAddress(Address template, int officeNumber) {
        Address address = new Address();

        address.setAdmin_area(template.getAdmin_area());
        address.setDistrict(template.getDistrict());
        address.setHouse_str(template.getHouse_str());
        address.setKvartira(String.valueOf(officeNumber));
        address.setLocality(template.getLocality());
        address.setFiasAddressVector(copyAddressVector(template.getFiasAddressVector()));

        GeoCoordinates coordinates = new GeoCoordinates();
        coordinates.setLatitude(template.getGeoCoordinates().getLatitude());
        coordinates.setLongitude(template.getGeoCoordinates().getLongitude());
        address.setGeoCoordinates(coordinates);

        return address;
    }

    /**
     * Создание копии вектора адреса.
     */
    private FiasAddressVector copyAddressVector(FiasAddressVector template) {
        FiasAddressVector result = new FiasAddressVector();

        result.setLevel1(template.getLevel1());
        result.setLevel2(template.getLevel2());
        result.setLevel3(template.getLevel3());
        result.setLevel4(template.getLevel4());
        result.setLevel5(template.getLevel5());
        result.setLevel6(template.getLevel6());
        result.setLevel7(template.getLevel7());

        return result;
    }

    /**
     * Класс для хоранения данных формы.
     */
    public static class Data {
        private long id;
        private int minNumber;
        private int maxNumber;

        public Data() {

        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public int getMinNumber() {
            return minNumber;
        }

        public void setMinNumber(int minNumber) {
            this.minNumber = minNumber;
        }

        public int getMaxNumber() {
            return maxNumber;
        }

        public void setMaxNumber(int maxNumber) {
            this.maxNumber = maxNumber;
        }
    }
}
