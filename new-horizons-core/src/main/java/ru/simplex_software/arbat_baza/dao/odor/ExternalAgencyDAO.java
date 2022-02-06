package ru.simplex_software.arbat_baza.dao.odor;

import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import net.sf.autodao.Named;
import ru.simplex_software.arbat_baza.model.odor.ExternalAgency;

import java.util.List;

/**
 * Внешние агентства недвижимости.
 */
public interface ExternalAgencyDAO extends Dao<ExternalAgency,Long> {
    @Finder(query = "from ExternalAgency")
    public List<ExternalAgency> findAll();

    @Finder(query = "from ExternalAgency where friend=true")
    public List<ExternalAgency> findFriends();

    @Finder(query = "select count(ea)>0 from ExternalAgency ea where ea.site=:site")
    public boolean containAgency(@Named("site") String site);
}
