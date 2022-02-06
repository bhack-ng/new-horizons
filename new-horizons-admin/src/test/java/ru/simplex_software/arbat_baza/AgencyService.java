package ru.simplex_software.arbat_baza;

import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.simplex_software.arbat_baza.dao.AgencyDAO;
import ru.simplex_software.arbat_baza.model.Agency;

import javax.annotation.Resource;
import javax.annotation.Resources;


public class AgencyService {

    public AgencyService(){}

     @Resource
    private AgencyDAO agencyDAO;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(Agency agency){
        agencyDAO.saveOrUpdate(agency);
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void delete(Agency agency){
        agencyDAO.delete(agencyDAO.get(agency.getId()));
    }

}
