package ru.simplex_software.arbat_baza.dao;

import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import ru.simplex_software.arbat_baza.model.stead.SteadSale;

import java.util.List;
/**
 * .
 */
public interface SteadSaleDao extends Dao<SteadSale, Long> {

    @Finder(query = "FROM SteadSale")
    List<SteadSale> findAll();

    @Finder(query = "select distinct c from SteadSale c, Photo p where" +
            " c.objectStatus.id=1 AND c=p.realtyObject AND p.advertise=true AND c.publishYandexRealty=true")
    List<SteadSale> findToYandexExport();
}
