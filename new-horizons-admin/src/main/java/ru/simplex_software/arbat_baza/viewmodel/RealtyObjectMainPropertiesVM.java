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
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import ru.simplex_software.arbat_baza.dao.AddressDAO;
import ru.simplex_software.arbat_baza.dao.DirectionRoadDao;
import ru.simplex_software.arbat_baza.dao.fias.FiasAddressVectorDAO;
import ru.simplex_software.arbat_baza.dao.fias.FiasObjectDAO;
import ru.simplex_software.arbat_baza.model.DirectionRoad;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.arbat_baza.model.fias.FiasObject;
import ru.simplex_software.arbat_baza.model.privateHouses.PrivateHouseRent;
import ru.simplex_software.arbat_baza.model.privateHouses.PrivateHouseSale;
import ru.simplex_software.arbat_baza.model.stead.SteadRent;
import ru.simplex_software.arbat_baza.model.stead.SteadSale;

import java.util.Collections;
import java.util.List;

/**
 * FIAS address for  realty objects (not for filter)/
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class RealtyObjectMainPropertiesVM{
    private static final Logger LOG= LoggerFactory.getLogger(RealtyObjectMainPropertiesVM.class);

    @WireVariable
    private FiasObjectDAO fiasObjectDAO;

    @WireVariable
    private FiasAddressVectorDAO fiasAddressVectorDAO;

    @WireVariable
    private AddressDAO addressDAO;

    @WireVariable
    private DirectionRoadDao directionRoadDao;

    private List<DirectionRoad> directionRoadList = null;
    @SuppressWarnings("unchecked")
    private List<FiasObject>[] levels = new List[7];
    private FiasObject[] selections = new FiasObject[7];
    private RealtyObject realtyObject;
    public boolean stead = false;

    @AfterCompose
    public void afterCompose(@ExecutionArgParam("realtyObject")RealtyObject realtyObject){
        this.realtyObject = realtyObject;
        levels[0]=fiasObjectDAO.findRegion();
        if ((realtyObject instanceof SteadSale)||(realtyObject instanceof SteadRent)||
                (realtyObject instanceof PrivateHouseRent)||(realtyObject instanceof PrivateHouseSale)){
            stead = true;
        }
        selections =realtyObject.getAddress().getFiasAddressVector().toArray();
        selectionChanged(0);
    }

    /** Clear deeper level.*/
    @Command @NotifyChange({"*"})
    public void selectionChanged(@BindingParam("zLevel") int zLevel ){
        LOG.info("zLevel="+zLevel);
        for (int zIndex = zLevel + 1; zIndex < selections.length; zIndex++) {
            if(zIndex==6){
                if(selections[6]==null){
                    levels[6]=findStreet("");//clear streets
                }else{
                    levels[6]=findStreet(selections[6].getFORMALNAME());//clear streets
                }

                if(!hasStreet()){
                    selections[6]= null;
                    BindUtils.postNotifyChange(null, null, selections, "[6]");
                }
            }else{
                levels[zIndex]=findByNearestLevel(zIndex);
                if (levels[zIndex] == null || !levels[zIndex].contains(selections[zIndex])) {
                    selections[zIndex] = null;
                    //todo bug in zk?
                    BindUtils.postNotifyChange(null, null, selections, "[" + zIndex + "]");
                }
            }

        }

        realtyObject.getAddress().getFiasAddressVector().setLevels(selections);

    }

    public boolean hasStreet(){
        final FiasObject street = selections[6];
        if(street==null){
            return false;
        }
        final String parent = street.getPARENTGUID();
        if(parent==null){
            return false;
        }

        for(int i=5;i>=0;i--){
            if(selections[i]==null) {
                continue;
            }
            if(fiasObjectDAO.getChildCount(selections[i].getAOGUID(), 7)==0){
                continue;
            }
            return (parent.equals(selections[i].getAOGUID()));
        }
        return false;
    }

    @Command @NotifyChange("*")
    public void findStreet(@ContextParam(ContextType.TRIGGER_EVENT) InputEvent event){
        String text = event.getValue();
        levels[6]=findStreet(text);
        BindUtils.postNotifyChange(null, null, selections, "[" + 6 + "]");

    }

    private List<FiasObject> findStreet(String text) {
        List<FiasObject> result;
        text = text.toUpperCase();
        for(int i=5;i>=0;i--){
            if(selections[i]==null) {
                continue;
            }
            if(fiasObjectDAO.getChildCount(selections[i].getAOGUID(), 7)==0){
                continue;
            }
            result = fiasObjectDAO.findStreet(selections[i].getAOGUID(), 7, 100,"%"+text+"%");

            if(result.size()==100){
                FiasObject fiasObject = new FiasObject();
                fiasObject.setOFFNAME("...");
                fiasObject.setSHORTNAME("");
                result.add(fiasObject);
            }
            return result;
        }
        return Collections.EMPTY_LIST;
    }

    public List<FiasObject> findByNearestLevel(int queryZLevel ){
        for(int i=queryZLevel;i>=0;i--){
            if(selections[i]!=null){
                List<FiasObject> result=fiasObjectDAO.findChild(selections[i].getAOGUID(), queryZLevel+1);
                if(result!=null && result.size()>0){
                    return result;
                }
            }
        }
        return Collections.EMPTY_LIST;
    }

    public FiasObject[] getSelections() {
        return selections;
    }

    public void setSelections(FiasObject[] selections) {
        this.selections = selections;
    }

    public List<FiasObject>[] getLevels() {
        return levels;
    }

    public RealtyObject getRealtyObject() {
        return realtyObject;
    }

    public void setRealtyObject(RealtyObject realtyObject) {
        this.realtyObject = realtyObject;
    }

    public boolean isStead() {
        return stead;
    }

    public List<DirectionRoad> getDirectionRoadList() {
        return directionRoadList;
    }

    public boolean isViewMode() {
        return realtyObject.getExternalObectExt() != null;
    }
}
