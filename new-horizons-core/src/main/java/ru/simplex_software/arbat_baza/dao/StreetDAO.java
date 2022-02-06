package ru.simplex_software.arbat_baza.dao;

import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import net.sf.autodao.Named;
import ru.simplex_software.arbat_baza.model.Street;

import java.util.List;

/**
 * Dao для доступа к Улицам (объект Street).
 */
public interface StreetDAO extends Dao<Street,Long> {
    @Finder(query = "from Street order by streetName")
    public List<Street> findAll();

    @Finder(query = "from Street where streetName like :startChars")
    public List<Street> findByFilter(@Named("startChars")String startChars);

    @Finder(query = "from Street where upper(streetName) like upper(:startChars)")
    public List<Street> findByFilterCaseInsensetive(@Named("startChars")String startChars);

    /**
     * Получение улицы по имени.
     * @param startChars название улицы.
     * @return
     */
    @Finder(query = "from Street where streetName= :streetName")
    public Street getStreetByName(@Named("streetName")String startChars);

}
