package ru.simplex_software.arbat_baza.dao;

import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import ru.simplex_software.arbat_baza.model.Agent;
import ru.simplex_software.arbat_baza.model.MetroStation;
import ru.simplex_software.arbat_baza.model.ObjectStatus;

import java.util.List;

/**
 * .
 */
public interface ObjectStatusDAO extends Dao<ObjectStatus,Long> {
    @Finder(query = "from ObjectStatus")
    public List<ObjectStatus> findAll();
}
