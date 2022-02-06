package ru.simplex_software.arbat_baza.viewmodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.zkutils.DetachableModel;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ExternalPhotoPanelVM {
    Logger LOG = LoggerFactory.getLogger(ExternalPhotoPanelVM.class);

    RealtyObject realtyObject =null;
    private Component editWin = null;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view
            ,@ExecutionArgParam("realtyObject") RealtyObject realtyObject){
        editWin = view;
        this.realtyObject = realtyObject;
    }

    public RealtyObject getRealtyObject() {
        return realtyObject;
    }
}
