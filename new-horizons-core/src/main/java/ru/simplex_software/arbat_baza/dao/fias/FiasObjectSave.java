package ru.simplex_software.arbat_baza.dao.fias;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.simplex_software.arbat_baza.model.fias.FiasObject;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Save bulk lost of FiasObject in database.
 */
public class FiasObjectSave  {
    @Resource
    private FiasObjectDAO fiasObjectDAO;
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void bulkSave(Collection<FiasObject> list){
        for(FiasObject fiasObject:list) {
            fiasObjectDAO.saveOrUpdate(fiasObject);
        }
    }
}
