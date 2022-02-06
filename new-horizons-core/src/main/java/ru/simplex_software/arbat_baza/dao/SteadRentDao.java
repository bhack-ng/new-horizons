package ru.simplex_software.arbat_baza.dao;

import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import ru.simplex_software.arbat_baza.model.stead.SteadRent;

import java.util.List;
/**
 * .
 */
public interface SteadRentDao extends Dao<SteadRent, Long> {

    @Finder(query = "FROM SteadRent")
    List<SteadRent> findAll();

    @Finder(query = "select distinct c from SteadRent c, Photo p where" +
            " c.objectStatus.id=1 AND c=p.realtyObject AND p.advertise=true AND c.publishYandexRealty=true")
    List<SteadRent> findToYandexExport();
}
