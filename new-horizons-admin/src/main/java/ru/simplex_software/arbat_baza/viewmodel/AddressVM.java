package ru.simplex_software.arbat_baza.viewmodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import ru.simplex_software.arbat_baza.AuthService;
import ru.simplex_software.arbat_baza.dao.RealtyFilterDAO;
import ru.simplex_software.arbat_baza.dao.fias.FiasAddressVectorDAO;
import ru.simplex_software.arbat_baza.dao.fias.FiasObjectDAO;
import ru.simplex_software.arbat_baza.model.RealtyFilter;
import ru.simplex_software.arbat_baza.model.fias.FiasAddressVector;
import ru.simplex_software.arbat_baza.model.fias.FiasObject;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

/**
 *
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class AddressVM {
    private static final Logger LOG= LoggerFactory.getLogger(AddressVM.class);
    @WireVariable
    private FiasObjectDAO fiasObjectDAO;
    @WireVariable
    private FiasAddressVectorDAO fiasAddressVectorDAO;
    @WireVariable
    private RealtyFilterDAO realtyFilterDAO;

    @WireVariable
    private AuthService authService;

    private List<FiasObject>[] levels = new List[7];
    private FiasObject[] selections = new FiasObject[7];
    private RealtyFilter filter;

    @AfterCompose
    public void afterCompose(){
        this.filter=authService.getLogginedAgent().getFilter();
        levels[0]=fiasObjectDAO.findRegion();
        FiasAddressVector addr = filter.getFiasAddressVector();
        if(addr ==null){
            addr = new FiasAddressVector();
            fiasAddressVectorDAO.saveOrUpdate(addr);
            filter.setFiasAddressVector(addr);
            realtyFilterDAO.saveOrUpdate(filter);
        }
        selections=addr.toArray();
        IntStream.range(1,7).forEach(i->levels[i]=fillLevel(i));
    }



    @Command @NotifyChange("*")
    public void selectionChanged(@BindingParam("zLevel") int zLevel ){
        int level=zLevel+1;
        LOG.info("level="+zLevel);
        for(int i = zLevel+1; i<7; i++){
            levels[i]= fillLevel(i);
            if(levels[i]==null || !levels[i].contains(selections[i])){
                selections[i]=null;
            }
        }
        final FiasAddressVector fiasAddressVector = filter.getFiasAddressVector();
        fiasAddressVector.setLevels(selections);
        fiasAddressVectorDAO.saveOrUpdate(fiasAddressVector);

    }
    public List<FiasObject> fillLevel(int queryLevel ){

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

    public FiasObject[] getSelections() {
        return selections;
    }

    public void setSelections(FiasObject[] selections) {
        this.selections = selections;
    }

    public List<FiasObject>[] getLevels() {
        return levels;
    }

    public RealtyFilter getFilter() {
        return filter;
    }

    public void setFilter(RealtyFilter filter) {
        this.filter = filter;
    }
}
