package ru.simplex_software.arbat_baza.viewmodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import ru.simplex_software.arbat_baza.dao.PhotoDAO;
import ru.simplex_software.arbat_baza.dao.PhotoDataDAO;
import ru.simplex_software.arbat_baza.dao.RealtyObjectDAO;
import ru.simplex_software.arbat_baza.model.Photo;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.zkutils.DetachableModel;

import java.util.HashMap;

/**
 * .
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class EditPhotoSubPanelVM {
    private static final Logger LOG= LoggerFactory.getLogger(EditPhotoSubPanelVM.class);
    @WireVariable
    private PhotoDAO photoDAO;
    @WireVariable
    private RealtyObjectDAO realtyObjectDAO;
    @WireVariable
    PhotoDataDAO photoDataDAO;
    @DetachableModel
    private RealtyObject realtyObject;
    private Component editWin;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view
            ,@ExecutionArgParam("realtyObject") RealtyObject realtyObject) {
        realtyObject=realtyObjectDAO.get(realtyObject.getId());
        this.realtyObject=realtyObject;
        editWin=view;

    }

    @Command
    public void editPhoto(@BindingParam("photo") Photo photo){
        HashMap params = new HashMap();
        params.put("photo", photo);
        Component editPhoto = Executions.createComponents("/WEB-INF/zul/editPhoto.zul", editWin, params);
        editPhoto.addEventListener("onChangeList", e-> refresh());
    }

    @Command @NotifyChange("*")
    public void deletePhoto(@BindingParam("photo") Photo photo){
        photo=photoDAO.get(photo.getPrimaryKey());
        realtyObject.getPhotos().remove(photo);
        photoDAO.delete(photo);
        refresh();
    }



    private  void refresh(){
        realtyObject=realtyObjectDAO.get(getRealtyObject().getId());
        //todo bug in zk?


    }


    public RealtyObject getRealtyObject() {
        return realtyObject;
    }

    public void setRealtyObject(RealtyObject realtyObject) {
        this.realtyObject = realtyObject;
    }
}
