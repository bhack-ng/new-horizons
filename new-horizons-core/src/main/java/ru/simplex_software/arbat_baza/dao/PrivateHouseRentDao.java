package ru.simplex_software.arbat_baza.dao;

import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import ru.simplex_software.arbat_baza.model.privateHouses.PrivateHouseRent;

import java.util.List;

/**
 * .
 */

public interface PrivateHouseRentDao extends Dao<PrivateHouseRent, Long> {

    @Finder(query = "FROM PrivateHouseRent")
    List<PrivateHouseRent> findAll();

    @Finder(query = "select distinct c from PrivateHouseRent c, Photo p where" +
            " c.objectStatus.id=1 AND c=p.realtyObject AND p.advertise=true AND c.publishYandexRealty=true")
    List<PrivateHouseRent> findToYandexExport();
}
