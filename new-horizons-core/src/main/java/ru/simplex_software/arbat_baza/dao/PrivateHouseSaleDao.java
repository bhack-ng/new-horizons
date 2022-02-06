package ru.simplex_software.arbat_baza.dao;

import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import ru.simplex_software.arbat_baza.model.privateHouses.PrivateHouseSale;

import java.util.List;

/**
 * .
 */
public interface PrivateHouseSaleDao extends Dao<PrivateHouseSale, Long> {

    @Finder(query = "FROM PrivateHouseSale")
    List<PrivateHouseSale> findAll();

    @Finder(query = "select distinct c from PrivateHouseSale c, Photo p where" +
            " c.objectStatus.id=1 AND c=p.realtyObject AND p.advertise=true AND c.publishYandexRealty=true")
    List<PrivateHouseSale> findToYandexExport();
}
