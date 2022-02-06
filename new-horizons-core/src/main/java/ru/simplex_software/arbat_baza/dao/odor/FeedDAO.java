package ru.simplex_software.arbat_baza.dao.odor;

import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import ru.simplex_software.arbat_baza.model.odor.Feed;

import java.util.List;


/**
 * Внешние агентства недвижимости.
 */
public interface FeedDAO extends Dao<Feed,Long> {
    @Finder(query = "from Feed where enable=true AND externalAgency.enable=true")
    public List<Feed> findAllActive();

}
