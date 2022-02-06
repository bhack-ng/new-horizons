package ru.simplex_software.arbat_baza.viewmodel;

import org.zkoss.bind.Converter;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ExecutionArgParam;
import ru.simplex_software.arbat_baza.EnumConverter;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.arbat_baza.model.privateHouses.PrivateHouseSale;
import ru.simplex_software.arbat_baza.model.privateHouses.WallsType;
import ru.simplex_software.arbat_baza.model.stead.SteadArea;


public class EditPrivateHouseSaleVM  {
    private SteadArea  steadArea = null;
    private PrivateHouseSale realtyObject;
    private Converter enumFromSteadConverter=new EnumConverter("ru.simplex_software.arbat_baza.model.privateHouses");


    @AfterCompose
    public void afterCompose(@ExecutionArgParam("realtyObject")RealtyObject realtyObject) {
        this.realtyObject = (PrivateHouseSale) realtyObject;
        steadArea = (SteadArea)realtyObject.getArea();

    }

    public PrivateHouseSale getPrivateHouseSale() {
        return realtyObject;
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

    public PrivateHouseSale getRealtyObject() {
        return realtyObject;
    }
}

