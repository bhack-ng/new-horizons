package ru.simplex_software.arbat_baza.viewmodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.Media;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Include;
import ru.simplex_software.arbat_baza.PhotoService;
import ru.simplex_software.arbat_baza.PhotoUtils;
import ru.simplex_software.arbat_baza.dao.PhotoDAO;
import ru.simplex_software.arbat_baza.dao.PhotoDataDAO;
import ru.simplex_software.arbat_baza.dao.RealtyObjectDAO;
import ru.simplex_software.arbat_baza.model.Photo;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.zkutils.DetachableModel;

import java.io.IOException;
import java.util.HashMap;

/**
 * .
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class PhotoPanelVM  {
    private static final Logger LOG= LoggerFactory.getLogger(PhotoPanelVM.class);
    @WireVariable
    private PhotoDAO photoDAO;
    @WireVariable
    private RealtyObjectDAO realtyObjectDAO;
    @WireVariable
    PhotoDataDAO photoDataDAO;
    @DetachableModel
    private RealtyObject realtyObject;
    private Component editWin;
    private String subPanelLink ;
    @WireVariable
    private PhotoService photoService;
    private Include include;


    private boolean editMode = true;


    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view
            ,@ExecutionArgParam("realtyObject") RealtyObject realtyObject) {
        this.realtyObject=realtyObject;
        editWin=view;
        include=(Include)Selectors.find(editWin,"include").get(0);
        refresh();

    }

    @Command
    @NotifyChange("*")
    public void addNewPhoto(){
        HashMap params = new HashMap();
        Photo photo = new Photo();
        photo.setRealtyObject(realtyObject);
        params.put("photo", photo);
        Component editPhoto = Executions.createComponents("/WEB-INF/zul/editPhoto.zul", editWin, params);
        editPhoto.addEventListener("onChangeList", e-> refresh() );
}

    private  void refresh(){
        if (editMode){
            subPanelLink = "/WEB-INF/zul/editPhotoSubPanel.zul";
        }else {
            subPanelLink ="/WEB-INF/zul/showPhotoSubPanel.zul";
        }
        include.invalidate();
    }





    @Command @NotifyChange("*")
    public void setEditMode(){
        editMode=true;
        refresh();
    }
    @Command @NotifyChange("*")
    public void setViewMode(){
        editMode=false;
        refresh();
    }

    @Command("upload")
    @NotifyChange("*")
    public void onUpload(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx){
        UploadEvent e = null;
        Object objUploadEvent = ctx.getTriggerEvent();
        if (objUploadEvent != null && (objUploadEvent instanceof UploadEvent)) {
            e = (UploadEvent) objUploadEvent;
        }
        if (e.getMedias() != null)
        {
            StringBuilder sb = new StringBuilder("Вы загрузили: \n");

            for (Media m : e.getMedias()) {

                Photo photo = new Photo();
                photo.setContentType(m.getContentType());
                photo.getData().setData(m.getByteData());
                makePreview(photo);
                photo.setRealtyObject(realtyObject);
                photoService.savePhoto(photo);

                sb.append(m.getName());
                sb.append(" (");
                sb.append(m.getContentType());
                sb.append(")\n");

            }
            Messagebox.show(sb.toString());
            refresh();
        }
        else
        {
            Messagebox.show("Нет файлов для згрузки!");
        }

        refresh();
    }

    public void makePreview(Photo photo) {

        try{
            PhotoUtils.makePreviewImageInmem(photo);
        }catch (IOException ex){
            org.zkoss.zul.Messagebox.show(ex.getMessage(), "Error", org.zkoss.zul.Messagebox.OK, org.zkoss.zul.Messagebox.ERROR);
        }

    }


    public RealtyObject getRealtyObject() {
        return realtyObject;
    }

    public String getSubPanelLink() {
        return subPanelLink;
    }

    public void setSubPanelLink(String subPanelLink) {
        this.subPanelLink = subPanelLink;
    }
}
