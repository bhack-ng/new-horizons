package ru.simplex_software.arbat_baza.viewmodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.AbstractListModel;
import org.zkoss.zul.Messagebox;
import ru.simplex_software.arbat_baza.dao.PhotoDAO;
import ru.simplex_software.arbat_baza.dao.RealtyObjectDAO;
import ru.simplex_software.arbat_baza.model.Photo;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.zkutils.DetachableModel;

import java.util.HashMap;

/**
 * ...
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ShowPhotoSubPanelVM {

    @WireVariable
    private PhotoDAO photoDAO;
    @WireVariable
    RealtyObjectDAO realtyObjectDAO;

    @DetachableModel
    private RealtyObject realtyObject;
    private static final Logger LOG= LoggerFactory.getLogger(ShowPhotoSubPanelVM.class);

    private Component editWin;
    private Photo selectedPhoto;                                    //выбраное фото
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW)  Component view
            ,@ExecutionArgParam("realtyObject") RealtyObject realtyObject){
        this.realtyObject=realtyObjectDAO.get(realtyObject.getId());
        realtyObjectDAO.refresh(realtyObject);
        this.editWin=view;

    }


    /**
     * Показ фотографий в две колонки.
     * Количество строк в половину меньше чем фотографий. Каждые 2 фотографии собираются строки.
     */
    private AbstractListModel model=new AbstractListModel(){
        @Override
        public Object getElementAt(int index) {
            final Photo[] twoPhoto = new Photo[2];
            twoPhoto[0]=realtyObject.getPhotos().get(index*2);
            int secondPhotoIndex=index*2+1;
            if(secondPhotoIndex<realtyObject.getPhotos().size()){
                twoPhoto[1]=realtyObject.getPhotos().get(index*2+1);
            }
            return twoPhoto;
        }

        @Override
        public int getSize() {
            return realtyObject.getPhotos().size()/2+realtyObject.getPhotos().size()%2;
        }
    };


    @Command
    @NotifyChange("*")
    public void editPhotoOnClick(@BindingParam("photo") Photo photo){
        HashMap params = new HashMap();
        params.put("photo", photo);
        Component editPhoto = Executions.createComponents("/WEB-INF/zul/editPhoto.zul", editWin, params);
    }



    @Command
    public void selected(@BindingParam("selectedPhoto") Photo selected){
        selectedPhoto= selected;
    }
    @Command
    @NotifyChange("*")
    public void editPhoto(){
        HashMap params = new HashMap();
        params.put("photo", selectedPhoto);
        Component editPhoto = Executions.createComponents("/WEB-INF/zul/editPhoto.zul", editWin, params);
    }





    @Command
    @NotifyChange("*")
        public void deleteSelectedPhoto(){
        /**слушатель для окна подтверждения удаления */
        EventListener<Messagebox.ClickEvent> listener = new EventListener<Messagebox.ClickEvent>() {
            @Override
            public void onEvent(Messagebox.ClickEvent event) throws Exception {
                if (event.getName().equals("onOK")) {
                    selectedPhoto=photoDAO.get(selectedPhoto.getPrimaryKey());
                    realtyObject.getPhotos().remove(selectedPhoto);
                    photoDAO.delete(selectedPhoto);
                    LOG.debug("foto deleted/ photo.Id={}",selectedPhoto.getId());
                    refresh();
                }
            }
        };
        Messagebox.Button[] buttons = {Messagebox.Button.OK ,Messagebox.Button.NO};
        Messagebox.show("Уверены , что хотите удалить фото?",buttons, listener);

    }

    private  void refresh(){
        BindUtils.postNotifyChange(null,null,this,"*");

    }

    public AbstractListModel getModel() {
        return model;
    }
}
