package ru.simplex_software.arbat_baza.viewmodel.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;
import ru.simplex_software.arbat_baza.AuthService;
import ru.simplex_software.arbat_baza.dao.AgencyDAO;
import ru.simplex_software.arbat_baza.dao.DbFileDao;
import ru.simplex_software.arbat_baza.dao.fias.FiasObjectDAO;
import ru.simplex_software.arbat_baza.model.Agency;
import ru.simplex_software.arbat_baza.model.DbFile;
import ru.simplex_software.arbat_baza.model.fias.FiasObject;

import java.util.Collections;
import java.util.List;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class EditAgencySettingsVM {
    private static final Logger LOG= LoggerFactory.getLogger(EditAgencySettingsVM.class);



    @WireVariable
    private AuthService authService;
    @WireVariable
    private DbFileDao dbFileDao;
    @WireVariable
    private AgencyDAO agencyDAO;
    @WireVariable
    private FiasObjectDAO fiasObjectDAO;
    private Agency  agency;
    private Window win;
    String imageURL;


    private List<FiasObject>[] levels = new List[7];
    private FiasObject[] selections = new FiasObject[7];

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window view){
        agency = authService.getLogginedAgent().getAgency();
        win=view;
        levels[0]=fiasObjectDAO.findRegion();
        selections=agency.getDefaultAddress().toArray();
        selectionChanged(0);
    }

    @Command
    public void save(){
        agencyDAO.saveOrUpdate(agency);
        win.detach();

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
            final DbFile dbFile = new DbFile();
            dbFile.setContentType(media.getContentType());
            dbFile.setLength(media.getByteData().length);
            dbFile.getContent().setData(media.getByteData());
            dbFileDao.saveOrUpdate(dbFile);
            agency.setWatermark(dbFile);
            agencyDAO.saveOrUpdate(agency);
        }
    }

    @Command @NotifyChange({"*"})
    public void selectionChanged(@BindingParam("zLevel") int zLevel ){
        LOG.info("zLevel="+zLevel);
        for(int i = zLevel+1; i<selections.length; i++){
            levels[i]=combineLevels(i);
            if(levels[i]==null || !levels[i].contains(selections[i])){
                selections[i]=null;
                //todo bug in zk?
                BindUtils.postNotifyChange(null,null,selections,"["+i+"]");
            }
        }
        agency.getDefaultAddress().setLevels(selections);
    }
    public List<FiasObject> combineLevels(int queryLevel ){

        for(int i=queryLevel;i>=0;i--){
            if(selections[i]!=null){
                List<FiasObject> result=fiasObjectDAO.findChild(selections[i].getAOGUID(), queryLevel+1);
                if(result!=null && result.size()>0){
                    return result;
                }
            }
        }
        return Collections.EMPTY_LIST;
    }

    public String getImageUrl(){
        final DbFile watermark = authService.getLogginedAgent().getAgency().getWatermark();
        if(watermark ==null) {
            return "";
        }else{
            return "/innerFiles?id="+ watermark.getId();
        }
    }


    public Agency getAgency(){
        return agency;
    }

    public List<FiasObject>[] getLevels() {
        return levels;
    }

    public void setLevels(List<FiasObject>[] levels) {
        this.levels = levels;
    }

    public FiasObject[] getSelections() {
        return selections;
    }

    public void setSelections(FiasObject[] selections) {
        this.selections = selections;
    }
}
