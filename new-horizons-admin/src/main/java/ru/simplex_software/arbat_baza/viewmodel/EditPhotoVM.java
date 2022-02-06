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
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import ru.simplex_software.arbat_baza.PhotoService;
import ru.simplex_software.arbat_baza.PhotoUtils;
import ru.simplex_software.arbat_baza.dao.PhotoDAO;
import ru.simplex_software.arbat_baza.dao.PhotoDataDAO;
import ru.simplex_software.arbat_baza.dao.RealtyObjectDAO;
import ru.simplex_software.arbat_baza.model.Photo;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.zkutils.DetachableModel;

import java.io.IOException;

/**
 * .
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class EditPhotoVM {
    private static final Logger LOG= LoggerFactory.getLogger(EditPhotoVM.class);
    @WireVariable
    private PhotoDAO  photoDAO;
    @WireVariable
    private PhotoDataDAO photoDataDAO;
    @WireVariable
    private RealtyObjectDAO realtyObjectDAO;
    @WireVariable
    PhotoService photoService;
    private Photo photo;
    @DetachableModel
    private RealtyObject realtyObject;

    private Window editWin;
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window view
                                ,@ExecutionArgParam("photo") Photo photo) {
        this.editWin=view;
        this.photo=photo;
        //read before edit
        photo.getData();
        photo.getPreview();
        realtyObject= realtyObjectDAO.get(photo.getRealtyObject().getPrimaryKey());
    }
    @Command
    public void save(){

        editWin.detach();
        if(photo.getId()!=null) {
            photo=photoDAO.merge(photo);
        }
        photoDAO.saveOrUpdate(photo);

        Events.postEvent("onChangeList", editWin, null);
    }
    @Command
    public void cancel(){
        editWin.detach();
    }


    @Command("upload")
    @NotifyChange("*")
    public void onImageUpload(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx) {
        LOG.debug("Upload Event Is Coming");
        UploadEvent upEvent = null;
        Object objUploadEvent = ctx.getTriggerEvent();
        if (objUploadEvent != null && (objUploadEvent instanceof UploadEvent)) {
            upEvent = (UploadEvent) objUploadEvent;
        }
        if (upEvent != null) {
            Media media = upEvent.getMedia();
            if(photo.getId()!=null) {
                photoDAO.refresh(photo);
            }
            photo.setContentType(media.getContentType());
            photo.getData().setData(media.getByteData());
            makePreview();
            photoDataDAO.saveOrUpdate(photo.getData());
            photoDataDAO.saveOrUpdate(photo.getPreview());
            photoDAO.saveOrUpdate(photo);
        }
    }

    public void makePreview() {
        try{
            PhotoUtils.makePreviewImageInmem(photo);
        }catch (IOException ex){
            Messagebox.show(ex.getMessage(),"Error",Messagebox.OK,Messagebox.ERROR);
        }

    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

}
