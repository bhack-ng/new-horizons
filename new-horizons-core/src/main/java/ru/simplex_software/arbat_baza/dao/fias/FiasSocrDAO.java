package ru.simplex_software.arbat_baza.dao.fias;

import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import net.sf.autodao.Named;
import ru.simplex_software.arbat_baza.model.fias.FiasSocr;

import java.util.List;


/**
 * Dao for FiasSocr.
 */
public interface FiasSocrDAO extends Dao<FiasSocr,String> {
    @Finder(query = "from FiasSocr  where level= :level")
    public List<FiasSocr> findFiasSocrByLevel(@Named("level")int level);

    @Finder (query = "from FiasSocr where level = 1 and socrName=:shortName")
    public FiasSocr findRegionFullName(@Named("shortName") String shortName);

    @Finder (query = "from FiasSocr where level = :level and socrName=:shortName")
    public FiasSocr findRegionFullName(@Named("level") int level, @Named("shortName") String shortName);

}
