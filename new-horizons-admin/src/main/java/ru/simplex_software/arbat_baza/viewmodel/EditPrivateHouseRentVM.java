package ru.simplex_software.arbat_baza.viewmodel;

import org.zkoss.bind.Converter;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ExecutionArgParam;
import ru.simplex_software.arbat_baza.EnumConverter;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.arbat_baza.model.privateHouses.PrivateHouseRent;
import ru.simplex_software.arbat_baza.model.privateHouses.WallsType;
import ru.simplex_software.arbat_baza.model.stead.SteadArea;

/**
 *панель с информацией, специфичной для аренды частного дома .
 * @see src/main/webapp/WEB-INF/zul/editPrivateHouseRent.zul
 * @see src/main/webapp/WEB-INF/zul/outOfTownSale.zul
 */
public class EditPrivateHouseRentVM  {
    private SteadArea steadArea = null;
    private PrivateHouseRent realtyObject;
    private Converter enumFromSteadConverter=new EnumConverter("ru.simplex_software.arbat_baza.model.privateHouses");


    @AfterCompose
    public void afterCompose(@ExecutionArgParam("realtyObject")RealtyObject realtyObject) {
        this.realtyObject = (PrivateHouseRent) realtyObject;
        steadArea = (SteadArea)realtyObject.getArea();

    }



    public WallsType[] getWallsType(){
        return WallsType.values();
    }

    public Converter getEnumFromSteadConverter() {
        return enumFromSteadConverter;
    }

    public SteadArea getSteadArea() {
        return steadArea;
    }

    public void setSteadArea(SteadArea steadArea) {
        this.steadArea = steadArea;
    }

    public PrivateHouseRent getRealtyObject() {
        return realtyObject;
    }

    public void setRealtyObject(PrivateHouseRent realtyObject) {
        this.realtyObject = realtyObject;
    }

    public void setEnumFromSteadConverter(Converter enumFromSteadConverter) {
        this.enumFromSteadConverter = enumFromSteadConverter;
    }
}

