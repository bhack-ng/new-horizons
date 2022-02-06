package ru.simplex_software.arbat_baza.dao;

import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import net.sf.autodao.Named;
import ru.simplex_software.arbat_baza.model.ObjectStatus;
import ru.simplex_software.arbat_baza.model.RealtyFilter;
import ru.simplex_software.arbat_baza.model.odor.ExternalAgency;

import java.util.List;

/**
 * .
 */
public interface RealtyFilterDAO extends Dao<RealtyFilter,Long> {
    @Finder(query = "from RealtyFilter")
    public List<RealtyFilter> findAll();
    @Finder(query = "SELECT rf FROM RealtyFilter rf WHERE rf.externalAgency = :externalAgency")
    public List<RealtyFilter> deleteExternalAgency(@Named("externalAgency") ExternalAgency externalAgency);

}
