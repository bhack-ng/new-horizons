package ru.simplex_software.arbat_baza.dao;

import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import net.sf.autodao.Limit;
import net.sf.autodao.Named;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.arbat_baza.model.odor.Feed;

import java.util.List;


/**
 * Поиск объектов недвижимости.
 */
public interface RealtyObjectDAO extends Dao<RealtyObject,Long> {
    @Finder(query = "from RealtyObject")
    public List<RealtyObject> findAll();

    @Finder(query = "from RealtyObject where oldDbIdentifier is not null")
    public List<RealtyObject> findImportedfromOldDB();

    @Finder(query = "from RealtyObject where oldDbIdentifier = :oldId")
    public RealtyObject findImportedfromOldDB(@Named("oldId") long oldId);

    @Finder(query = "select ro from RealtyObject ro where ro.externalObectExt.feed=:feed AND ro.externalObectExt.externalId=:externalId")
    public RealtyObject findExternal(@Named("feed")Feed feed,@Named("externalId")String externalId);

    @Finder(query = "SELECT ro from RealtyObject ro , ExternalObectExt e WHERE ro.externalObectExt = e AND e.feed=:feed  and e.updated<e.feed.lastUpdate")
    public List<RealtyObject> findOutdatedObjects(@Limit int limit,@Named("feed") Feed feed);


}
