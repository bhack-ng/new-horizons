package ru.simplex_software.arbat_baza.dao;

import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import net.sf.autodao.Named;
import ru.simplex_software.arbat_baza.model.CianAdminArea;
import ru.simplex_software.arbat_baza.model.MetroStation;

import java.util.List;

/**
 * Доступ к объектами CianAdminArea.
 */
public interface CianAdminAreaDAO extends Dao<CianAdminArea,Long> {
    @Finder(query = "from CianAdminArea")
    public List<CianAdminArea> findAll();

    /**
     * Получение CianAdminArea по имени.
     * @param name название CianAdminArea.
     * @return станция метро
     */
    @Finder(query = "from CianAdminArea where name like :name")
    public CianAdminArea getStationByName(@Named("name") String name);


    /**
     * Получение CianAdminArea по aoguid .
     * @param aoguid id объекта из ФИАС
     * @return станция метро
     */
    @Finder(query = "from CianAdminArea where fiasAoguid= :aoguid")
    public CianAdminArea findByFiasAOGUID(@Named("aoguid") String aoguid);


}
