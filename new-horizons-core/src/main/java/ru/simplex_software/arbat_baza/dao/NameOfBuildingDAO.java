package ru.simplex_software.arbat_baza.dao;

import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import ru.simplex_software.arbat_baza.model.MetroStation;
import ru.simplex_software.arbat_baza.model.PhotoData;
import ru.simplex_software.arbat_baza.model.live.NameOfBuilding;

import java.util.List;

/**
 * Data access object for PhotoData.
 */
public interface NameOfBuildingDAO extends Dao<NameOfBuilding,Long> {


    @Finder(query = "from NameOfBuilding")
    public List<NameOfBuilding> findAll();
}
