package ru.simplex_software.arbat_baza.viewmodel;

import org.zkoss.bind.Converter;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ExecutionArgParam;
import ru.simplex_software.arbat_baza.EnumConverter;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.arbat_baza.model.stead.ObjectType;

/**
 * .
 */
public class EditSteadSaleVM {
    private Converter enumFromSteadConverter = new EnumConverter("ru.simplex_software.arbat_baza.model.stead");
    private RealtyObject realtyObject;
    @AfterCompose
    public void afterCompose(@ExecutionArgParam("realtyObject") RealtyObject realtyObject) {
        this.realtyObject = realtyObject;

    }

    public ObjectType[] getObjectType() {
        return ObjectType.values();
    }

    public Converter getEnumFromSteadConverter() {
        return enumFromSteadConverter;
    }

    public RealtyObject getRealtyObject() {
        return realtyObject;
    }
}
