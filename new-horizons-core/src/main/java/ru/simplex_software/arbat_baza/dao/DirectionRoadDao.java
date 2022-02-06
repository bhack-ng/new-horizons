package ru.simplex_software.arbat_baza.dao;

import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import net.sf.autodao.Named;
import ru.simplex_software.arbat_baza.model.DirectionRoad;

import java.util.List;

/**
 * Содержит функции для обращения к списку шоссе ,
 * необходмых для экспорта участков в avito
 */
public interface DirectionRoadDao extends Dao<DirectionRoad,Long> {
    @Finder(query = "SELECT dr FROM DirectionRoad dr WHERE dr.cityName=:city AND dr.regionName=:region")
    public List<DirectionRoad> findByCityAndRegion(@Named("city") String city, @Named("region") String region);

    @Finder(query = "SELECT dr FROM DirectionRoad dr WHERE dr.regionName=:region")
    public List<DirectionRoad> findByRegion(@Named("region")String region );

    @Finder(query = "FROM DirectionRoad")
    public List<DirectionRoad> findAll();

}
