package ru.simplex_software.arbat_baza.dao;

import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import net.sf.autodao.Named;
import ru.simplex_software.arbat_baza.model.FloorScheme;

import java.util.List;

public interface FloorSchemeDAO extends Dao<FloorScheme, Long> {

    @Finder(query = "from FloorScheme")
    List<FloorScheme> findAll();

    @Finder(query = "FROM FloorScheme WHERE :name = name")
    FloorScheme findByName(@Named("name") String name);
}
