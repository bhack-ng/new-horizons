package ru.simplex_software.arbat_baza.dao;

import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import ru.simplex_software.arbat_baza.model.Address;
import ru.simplex_software.arbat_baza.model.Agency;


/**
 * Поиск адреса объекта недвижимости.
 */
public interface AddressDAO extends Dao<Address,Long> {

}
