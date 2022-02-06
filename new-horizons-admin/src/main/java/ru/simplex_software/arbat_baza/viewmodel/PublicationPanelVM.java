package ru.simplex_software.arbat_baza.viewmodel;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import ru.simplex_software.arbat_baza.model.RealtyObject;

/**
 * Панель публикации объекта недвижимости.
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class PublicationPanelVM {

    private RealtyObject realtyObject;

    @AfterCompose
    public void afterCompose(@ExecutionArgParam("realtyObject") RealtyObject realtyObject) {
        this.realtyObject = realtyObject;
    }

    public RealtyObject getRealtyObject() {
        return realtyObject;
    }
}
