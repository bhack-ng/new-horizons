package ru.simplex_software.arbat_baza.viewmodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.Converter;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zul.Window;
import ru.simplex_software.arbat_baza.EnumConverter;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.arbat_baza.model.live.BuildingTypeSale;
import ru.simplex_software.arbat_baza.model.live.LiveArea;
import ru.simplex_software.arbat_baza.model.live.LiveSaleRealty;
import ru.simplex_software.arbat_baza.model.live.SaleType;
import ru.simplex_software.arbat_baza.model.live.WindowsPosition;

/**
 * Продажа жилой недвижимости.
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class EditLiveSaleVM extends EditRealtyObject {
    private static final Logger LOG = LoggerFactory.getLogger(EditLiveSaleVM.class);
    private boolean hasBalcon = false;
    private boolean hasLodgia = false;
    private Converter enumFromLiveConverter=new EnumConverter("ru.simplex_software.arbat_baza.model.live");

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window view,
                             @ExecutionArgParam("realtyObject") RealtyObject realtyObject,
                             @ExecutionArgParam("tab") Integer tab) {
        LiveArea liveArea = (LiveArea) realtyObject.getArea();
        super.afterCompose(view, realtyObject, tab);
        if (liveArea.getBalconyCount()!=null){
            if(liveArea.getBalconyCount()==-1) hasBalcon=true;
        }
        if (liveArea.getRecessedBalconyCount()!=null){
            if (liveArea.getRecessedBalconyCount()==-1) hasLodgia = true;
        }
    }
    public BuildingTypeSale[] getBuildingTypeSales(){
        return BuildingTypeSale.values();
    }

    @NotifyChange("saleTypeList")
    public void setSelectedBuildingTypeSale(BuildingTypeSale bts){
        LiveSaleRealty ro = (LiveSaleRealty)getRealtyObject();
        ro.setBuildingTypeSale(bts);
    }

    public BuildingTypeSale  getSelectedBuildingTypeSale(){
        LiveSaleRealty ro = (LiveSaleRealty)getRealtyObject();
        return ro.getBuildingTypeSale();
    }
    public Converter getEnumFromLiveConverter() {
        return enumFromLiveConverter;
    }
    public WindowsPosition[] getWindowsPositions(){
        return WindowsPosition.values();
    }

    public SaleType[] getSaleTypeList(){
        LiveSaleRealty ro = (LiveSaleRealty)getRealtyObject();
        if(BuildingTypeSale.NEW_BUILDING==ro.getBuildingTypeSale()){
            return SaleType.NEW_BUILDING_SALE_TYPE;
        }
        if(BuildingTypeSale.SECONDHAND==ro.getBuildingTypeSale()){
            return SaleType.SECONDHAND_SALE_TYPE;
        }
        return new SaleType[]{};
    }

//    protected boolean validate() {
//        boolean yandexCorrect;
//        boolean isCorrect = super.validate();
//        String errorMsg= "Для объектов у которых выбрана опция 'Выгружать на Avito' ";
//        if (  realtyObject.isPublishAvito()||realtyObject.isPublishExternalAvito() ){
//            if(realtyObject.getArea().getRoomsCount()==null) {
//                errorMsg =errorMsg+ "\n" + "Необходимо заполнить поле 'Количество комнат'";
//                isCorrect = false;
//            }
//            DwellingHouse building = (DwellingHouse) realtyObject.getBuilding();
//            if (building.getBuildingType()==null){
//                errorMsg = errorMsg + "\n" + "Необходимо заполнить поле 'Тип здания'";
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
//                errorMsg = errorMsg + "\n" + "Необходимо заполнить поле 'Общая площадь'";
//                isCorrect = false;
//            }
//
//            LiveSaleRealty liveSaleRealty = (LiveSaleRealty) realtyObject;
//
//            if(liveSaleRealty.getBuildingTypeSale()==null){
//                errorMsg = errorMsg + "\n" +"Необходимо заполнить поле 'Тип продажи'";
//                isCorrect = false;
//            }
//
//            if((realtyObject.getNote()==null)|| !( StringUtils.hasLength(realtyObject.getNote().trim()) )){
//                errorMsg = errorMsg + "\n" + "Необходимо чтобы поле 'Текст оъявления' не было пустым";
//                isCorrect = false;
//            }
//
//            if(!isCorrect) Messagebox.show(errorMsg, "Внимание!", Messagebox.OK, Messagebox.ERROR);
//        }
//
//
//        isCorrect = isCorrect ;
//        return isCorrect;
//    }
private Validator yandexValidator = new YandexValidator();

    public Validator getYandexValidator() {
        return yandexValidator;
    }

    public boolean isHasBalcon() {
        return hasBalcon;
    }

    public void setHasBalcon(boolean hasBalcon) {
        this.hasBalcon = hasBalcon;
    }

    public boolean isHasLodgia() {
        return hasLodgia;
    }

    public void setHasLodgia(boolean hasLodgia) {
        this.hasLodgia = hasLodgia;
    }
}
