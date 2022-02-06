package ru.simplex_software.arbat_baza.viewmodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.Converter;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;
import ru.simplex_software.arbat_baza.EnumConverter;
import ru.simplex_software.arbat_baza.dao.NameOfBuildingDAO;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.arbat_baza.model.live.LiveArea;
import ru.simplex_software.arbat_baza.model.live.NameOfBuilding;
import ru.simplex_software.arbat_baza.model.live.OwnerStatus;
import ru.simplex_software.arbat_baza.model.live.RoomsNumRent;
import ru.simplex_software.arbat_baza.model.price.PayForm;

import java.util.List;

/**
 * View Model for src/main/webapp/WEB-INF/zul/editLiveRent.zul
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class EditLiveRentVM extends EditRealtyObject{
    private static final Logger LOG= LoggerFactory.getLogger(EditLiveRentVM.class);
    @WireVariable
    private NameOfBuildingDAO nameOfBuildingDAO;
    private Converter enumFromLiveConverter=new EnumConverter("ru.simplex_software.arbat_baza.model.live");
    private List<NameOfBuilding> nameOfBuildingList;
    private boolean hasLodgia;
    private boolean hasBalcon;
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window view
            , @ExecutionArgParam("realtyObject") RealtyObject realtyObject, @ExecutionArgParam("tab") Integer tab) {
        super.afterCompose(view,realtyObject,tab);
        LiveArea liveArea = (LiveArea) realtyObject.getArea();
        nameOfBuildingList=nameOfBuildingDAO.findAll();
        if (liveArea.getBalconyCount()!=null){
            if(liveArea.getBalconyCount()==-1) hasBalcon=true;
        }
        if (liveArea.getRecessedBalconyCount()!=null){
            if (liveArea.getRecessedBalconyCount()==-1) hasLodgia = true;
        }
    }


//    protected boolean validate() {
//       // boolean yandexCorrect = true;
//        boolean isCorrect = super.validate();
//
//        if (  realtyObject.isPublishAvito()||realtyObject.isPublishExternalAvito() ){
//            String errorMsg= "Ошибка. Заполните обязательные поля для публикации в Авито! ";
//            if(realtyObject.getArea().getRoomsCount()==null) {
//                errorMsg =errorMsg+ "\n" + "Количество комнат";
//                isCorrect = false;
//            }
//            DwellingHouse building = (DwellingHouse) realtyObject.getBuilding();
//            if (building.getBuildingType()==null){
//                errorMsg = errorMsg + "\n" + "Тип здания";
//                isCorrect = false;
//            }
//
//            if((building.getBuildingType()== BuildingType.OLD_FOND)||
//                    (building.getBuildingType()==BuildingType.BRICK_MONOLITE)||
//                    (building.getBuildingType()==BuildingType.STALIN)){
//                errorMsg = errorMsg + "\n" + "Необходимо , чтобы  поле 'Тип здания' имело значение:" +
//                        "\n" + "Блочный,Деревянныйй,Монолитный,Кирпичный,Панельный" +"\n";
//                isCorrect = false;
//            }
//
//            if(realtyObject.getArea().getTotal()==null){
//                errorMsg = errorMsg + "\n" + "Общая площадь";
//                isCorrect = false;
//            }
//
//            if((realtyObject.getNote()==null) || !( StringUtils.hasLength(realtyObject.getNote().trim()) ) ){
//                errorMsg = errorMsg + "\n" + "Текст оъявления";
//                isCorrect = false;
//            }
//
//            LiveRentPrice  liveRentPrice = (LiveRentPrice) realtyObject.getPrice();
//            if((liveRentPrice.getPrepay()!=null)&&(liveRentPrice.getPrepay()>3)){
//                errorMsg = errorMsg + "\n" + "Необходимо чтобы значение поля 'предоплата' было меньше 4 ";
//                isCorrect = false;
//
//            }
//            if (!isCorrect) {Messagebox.show(errorMsg, "Внимание!", Messagebox.OK, Messagebox.ERROR);}
//        }
//
//
//
//        return isCorrect;
//    }

    private Validator yandexValidator = new YandexValidator();

    public Validator getYandexValidator() {
        return yandexValidator;
    }

    public RoomsNumRent[] getRoomsNumList() {
        return RoomsNumRent.values();
    }

    public Converter getEnumFromLiveConverter() {
        return enumFromLiveConverter;
    }
    public PayForm[] getPayFormList(){
        return PayForm.values();
    }

    public OwnerStatus[] getOwnerStatusList(){
        return OwnerStatus.values();
    }

    public List<NameOfBuilding> getNameOfBuildingList() {
        return nameOfBuildingList;
    }

    public boolean isHasLodgia() {
        return hasLodgia;
    }

    public void setHasLodgia(boolean hasLodgia) {
        this.hasLodgia = hasLodgia;
    }

    public boolean isHasBalcon() {
        return hasBalcon;
    }

    public void setHasBalcon(boolean hasBalcon) {
        this.hasBalcon = hasBalcon;
    }
}
