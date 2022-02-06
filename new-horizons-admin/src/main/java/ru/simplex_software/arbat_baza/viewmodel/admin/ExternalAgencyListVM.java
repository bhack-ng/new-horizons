package ru.simplex_software.arbat_baza.viewmodel.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;
import ru.simplex_software.arbat_baza.AuthService;
import ru.simplex_software.arbat_baza.DeleteExtAgService;
import ru.simplex_software.arbat_baza.dao.odor.ExternalAgencyDAO;
import ru.simplex_software.arbat_baza.model.odor.ExternalAgency;
import ru.simplex_software.zkutils.DetachableModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * View model for external agency list
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ExternalAgencyListVM {
    private static final Logger LOG= LoggerFactory.getLogger(ExternalAgencyListVM.class);

    @DetachableModel
    private ExternalAgency selectedExternalAgency;
    @WireVariable
    DeleteExtAgService deleteExtAgService;
    @WireVariable
    AuthService authService;
    @WireVariable
    ExternalAgencyDAO externalAgencyDAO;
    List<ExternalAgency> externalAgencyList;
    private Window win;
    private boolean onlyEnable = false;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window view){
        win=view;
        externalAgencyList = authService.getLogginedAgent().getAgency().getExternalAgencyList();
    }

    @Command
    @NotifyChange("*")
    public void checkOnlyIncludedExternalAgency(){
        if (onlyEnable){

            List<ExternalAgency> extAgList =new ArrayList();
            for(ExternalAgency extAg:externalAgencyList){
                if (extAg.isEnable()==true){
                    extAgList.add(extAg);
                }
            }
            externalAgencyList = extAgList;
        }
        else {
            externalAgencyList = authService.getLogginedAgent().getAgency().getExternalAgencyList();
        }
    }


    @Command
    public void edit(@BindingParam("externalAgency") ExternalAgency externalAgency){
        HashMap params = new HashMap();
        params.put("externalAgency",externalAgency);
        Component editWin = Executions.createComponents("/WEB-INF/zul/admin/editExternalAgency.zul", win, params);
        editWin.addEventListener("onChangeList",  e-> refresh());
    }

    @Command
    public void editSelected(){
        HashMap params = new HashMap();
        params.put("externalAgency",selectedExternalAgency);
        Component editWin = Executions.createComponents("/WEB-INF/zul/admin/editExternalAgency.zul", win, params);
        editWin.addEventListener("onChangeList",  e-> refresh());
    }
    @Command
    public void create(){
        HashMap params = new HashMap();
        ExternalAgency externalAgency = new ExternalAgency();
        externalAgency.setAgency(authService.getLogginedAgent().getAgency());
        params.put("externalAgency", externalAgency);
        Component editWin = Executions.createComponents("/WEB-INF/zul/admin/editExternalAgency.zul", win, params);
        editWin.addEventListener("onChangeList", e-> refresh());
    }

    @GlobalCommand("refreshExternalAgencyList")
    public void refresh() {
        externalAgencyList = authService.getLogginedAgent().getAgency().getExternalAgencyList();
        BindUtils.postNotifyChange(null, null, this, "externalAgencyList");

    }

    @Command
    @NotifyChange("externalAgencyList")
    public void deleteSelected( ){
        deleteExtAgService.deleteExternalAgency(selectedExternalAgency);
        externalAgencyList.remove(selectedExternalAgency);
    }
    /*
    @Command @NotifyChange("*")
    public void toggleSelected( ){
        selectedExternalAgency.setEnable(!selectedExternalAgency.isEnable());
        externalAgencyDAO.saveOrUpdate(selectedExternalAgency);
        refresh();
    }
*/

    @Command
    public void enableSave(@BindingParam("externalAgency") ExternalAgency extAg){
        externalAgencyDAO.merge(extAg);
        checkOnlyIncludedExternalAgency();
        refresh();
    }

    public List<ExternalAgency> getExternalAgencyList() {
        return externalAgencyList;
    }

    public ExternalAgency getSelectedExternalAgency() {
        return selectedExternalAgency;
    }

    public void setSelectedExternalAgency(ExternalAgency selectedExternalAgency) {
        this.selectedExternalAgency = selectedExternalAgency;
    }

    public boolean isOnlyEnable() {
        return onlyEnable;
    }

    public void setOnlyEnable(boolean onlyEnable) {
        this.onlyEnable = onlyEnable;
    }
}
