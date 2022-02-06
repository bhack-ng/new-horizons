package ru.simplex_software.arbat_baza.dao;

import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import ru.simplex_software.arbat_baza.model.Agency;


/**
 * Поиск Аненства недвижимости.
 */
public interface AgencyDAO extends Dao<Agency,Long> {
    @Finder(query = "from Agency")
    public Agency findOne();

}
