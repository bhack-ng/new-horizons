package ru.simplex_software.arbat_baza.dao;

import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import net.sf.autodao.Named;
import ru.simplex_software.arbat_baza.model.MetroStation;

import java.util.List;

/**
 * Доступ к объектами "станция метро".
 */
public interface MetroStationDAO extends Dao<MetroStation,Long> {
    @Finder(query = "from MetroStation")
    public List<MetroStation> findAll();

    /**
     * Получение станции метро по имени.
     * @param stationName название станции.
     * @return станция метро
     */
    @Finder(query = "from MetroStation where stationName= :stationName")
    public MetroStation getStationByName(@Named("stationName")String stationName);

}
