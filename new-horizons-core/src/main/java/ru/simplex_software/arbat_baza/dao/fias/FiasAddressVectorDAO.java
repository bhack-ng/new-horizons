package ru.simplex_software.arbat_baza.dao.fias;

import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import net.sf.autodao.Named;
import ru.simplex_software.arbat_baza.model.fias.FiasAddressVector;
import ru.simplex_software.arbat_baza.model.fias.FiasObject;

import java.util.List;


/**
 * Адрес с точностью до улицы: методы загрузки из БД.
 */
public interface FiasAddressVectorDAO extends Dao<FiasAddressVector,Long > {

}
