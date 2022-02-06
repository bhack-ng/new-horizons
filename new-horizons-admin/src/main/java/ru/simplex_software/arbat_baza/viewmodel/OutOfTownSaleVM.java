package ru.simplex_software.arbat_baza.viewmodel;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;
import ru.simplex_software.arbat_baza.dao.DirectionRoadDao;
import ru.simplex_software.arbat_baza.model.DirectionRoad;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.arbat_baza.model.privateHouses.PrivateHouseRent;
import ru.simplex_software.arbat_baza.model.privateHouses.PrivateHouseSale;
import ru.simplex_software.arbat_baza.model.stead.SteadRent;
import ru.simplex_software.arbat_baza.model.stead.SteadSale;

import java.util.List;

/**
 * .
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class OutOfTownSaleVM extends EditRealtyObject {
    List<DirectionRoad> directionRoadList;
    @WireVariable
    DirectionRoadDao directionRoadDao;

    private String linkToZul = null;
    private String priceType = "";

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window view
            , @ExecutionArgParam("realtyObject") RealtyObject realtyObject, @ExecutionArgParam("tab") Integer tab) {
        if (realtyObject instanceof SteadSale) {
            linkToZul = "/WEB-INF/zul/editSteadSale.zul";
            priceType = "Цена";
        } else if (realtyObject instanceof PrivateHouseSale) {
            linkToZul = "/WEB-INF/zul/editPrivateHouseSale.zul";
            priceType = "Цена";
        } else if (realtyObject instanceof SteadRent) {
            linkToZul = "/WEB-INF/zul/editSteadRent.zul";
            priceType = "Цена за месяц";
        } else if (realtyObject instanceof PrivateHouseRent) {
            linkToZul = "/WEB-INF/zul/editPrivateHouseRent.zul";
            priceType = "Цена за месяц";
        } else {
            //тут ошибка должна быть
        }
        super.afterCompose(view, realtyObject, tab);

    }

    @Command
    @NotifyChange("*")
    public void findDirectionRoad() {
        if ((realtyObject.getAddress().getFiasAddressVector().getLevel1() != null) && (realtyObject.getAddress().getFiasAddressVector().getLevel4() != null)) {
            String region = realtyObject.getAddress().getFiasAddressVector().getLevel1().getFORMALNAME();
            String city = realtyObject.getAddress().getFiasAddressVector().getLevel4().getFORMALNAME();
            directionRoadList = directionRoadDao.findByCityAndRegion(city, region);

        } else if (realtyObject.getAddress().getFiasAddressVector().getLevel1() != null) {
            String region = realtyObject.getAddress().getFiasAddressVector().getLevel1().getFORMALNAME();
            directionRoadList = directionRoadDao.findByRegion(region);
        }

    }

   // @Override
//    protected boolean validate() {
//        boolean isValidate = super.validate();
//        String message = "\n";
//        //площадь участка
//        if (realtyObject.getArea() != null) {
//            SteadArea steadArea = (SteadArea) realtyObject.getArea();
//            if (steadArea.getLendArea() == null) {
//                isValidate = false;
//                message = message + "\n Площадь участка";
//            }
//        }
//
//        if (!isValidate) {
//            Messagebox.show(message, "Внимание!", Messagebox.OK, Messagebox.ERROR);
//        }
//        return isValidate;
//    }


    public List<DirectionRoad> getDirectionRoadList() {
        return directionRoadList;
    }

    public String getLinkToZul() {
        return linkToZul;
    }

    public String getPriceType() {
        return priceType;
    }
}
