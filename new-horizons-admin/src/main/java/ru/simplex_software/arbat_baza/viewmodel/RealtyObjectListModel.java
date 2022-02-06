package ru.simplex_software.arbat_baza.viewmodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.AbstractListModel;
import org.zkoss.zul.event.ListDataEvent;
import ru.simplex_software.arbat_baza.AuthService;
import ru.simplex_software.arbat_baza.dao.RealtyObjectByFilterDAOImpl;
import ru.simplex_software.arbat_baza.dao.RealtyObjectDAO;
import ru.simplex_software.arbat_baza.model.RealtyFilter;
import ru.simplex_software.arbat_baza.model.RealtyObject;

/**
 * Model for Listbox which support paging.
 */

public class RealtyObjectListModel extends AbstractListModel<RealtyObject> {
    private static final Logger LOG = LoggerFactory.getLogger(RealtyObjectListModel.class);
    @WireVariable
    private RealtyObjectByFilterDAOImpl realtyObjectByFilterDAOImpl;

    @WireVariable
    private RealtyObjectDAO realtyObjectDAO;
    @WireVariable
    private AuthService authService;

    private Long[] realtyObjectIds;

    private int PAGE_SIZE = 10;

    public RealtyObjectListModel(AuthService authService, RealtyObjectDAO realtyObjectDAO, RealtyObjectByFilterDAOImpl realtyObjectByFilterDAOImpl) {
        this.authService = authService;
        this.realtyObjectDAO = realtyObjectDAO;
        this.realtyObjectByFilterDAOImpl = realtyObjectByFilterDAOImpl;
    }

    @Override
    public RealtyObject getElementAt(int index) {
        RealtyFilter filter = authService.getLogginedAgent().getFilter();
        if(realtyObjectIds[index]==null){
            int i=0;
            for(Long id:realtyObjectByFilterDAOImpl.findIdByFilter(filter,index, PAGE_SIZE)){
                realtyObjectIds[index+(i++)]=id;
            }
        }
        RealtyObject realtyObject= realtyObjectDAO.get(realtyObjectIds[index]);//suppose that hibernate cahe is turned on.
        return realtyObject;
    }

    @Override
    public int getSize() {
        if(realtyObjectIds==null){
            RealtyFilter filter = authService.getLogginedAgent().getFilter();
            long l = realtyObjectByFilterDAOImpl.countByFilter(filter);
            realtyObjectIds=new Long[(int)l];
        }
        return realtyObjectIds.length;
    }
    public void refresh(){
        realtyObjectIds=null;
        fireEvent(ListDataEvent.CONTENTS_CHANGED, -1, -1);
    }
}
