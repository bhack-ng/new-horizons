package ru.simplex_software.arbat_baza.dao;

import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import ru.simplex_software.arbat_baza.model.live.LiveLeaseRealty;

import java.util.List;

/**
 * Dao для аренды жилой недвижимасти.
 */
public interface LiveLeaseRealtyDAO extends Dao<LiveLeaseRealty,Long> {
    @Finder(query = "from LiveLeaseRealty")
    public List<LiveLeaseRealty> findAll();


    @Finder(query = "select distinct c from LiveLeaseRealty c, Photo p where " +
            " c.objectStatus.id=1 AND c=p.realtyObject AND p.advertise=true AND c.publishCian=true order by c.id")
    public List<LiveLeaseRealty> findToCianExport();

    @Finder(query = "select distinct c from LiveLeaseRealty c, Photo p where " +
            " c.objectStatus.id=1 AND c=p.realtyObject AND p.advertise=true AND c.publishExternal=true order by c.id")
    public List<LiveLeaseRealty> findToExternalExport();

    @Finder(query = "select distinct c from LiveLeaseRealty c, Photo p where" +
            " c.objectStatus.id=1 AND c=p.realtyObject AND p.advertise=true AND c.publishAvito=true")
    public List<LiveLeaseRealty> findToAvitoExport();

    @Finder(query = "select distinct c from LiveLeaseRealty c, Photo p where" +
            " c.objectStatus.id=1 AND c=p.realtyObject AND p.advertise=true AND c.publishSite=true")
    public List<LiveLeaseRealty> findToSiteExport();

    @Finder(query = "select distinct c from LiveLeaseRealty c, Photo p where" +
            " c.objectStatus.id=1 AND c=p.realtyObject AND p.advertise=true AND c.publishExternalAvito=true")
    public List<LiveLeaseRealty> findToExternalAvitoExport();

    @Finder(query ="select distinct c from LiveLeaseRealty c, Photo p where" +
            " c.objectStatus.id=1 AND c=p.realtyObject AND p.advertise=true AND c.publishYandexRealty=true")
    public List<LiveLeaseRealty> findToYandexExport();
}

