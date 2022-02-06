package ru.simplex_software.arbat_baza.dao;

import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import net.sf.autodao.Named;
import ru.simplex_software.arbat_baza.model.live.LiveSaleRealty;

import java.util.List;

/**
 * Dao для объектов продажи жилой недвижимасти.
 */
public interface LiveSaleRealtyDAO extends Dao<LiveSaleRealty,Long> {
    @Finder(query = "from LiveSaleRealty order by id")
    public List<LiveSaleRealty> findAll();

    @Finder(query = "from LiveSaleRealty where oldDbIdentifier is not null")
    public List<LiveSaleRealty> findImportedfromOldDB();


    @Finder(query = "select distinct c from LiveSaleRealty c, Photo p where " +
            " c.objectStatus.id=1 AND c=p.realtyObject AND p.advertise=true AND c.publishCian=true order by c.id")
    public List<LiveSaleRealty> findToCianExport();

    @Finder(query = "select distinct c from LiveSaleRealty c, Photo p where " +
            " c.objectStatus.id=1 AND c=p.realtyObject AND p.advertise=true AND c.publishExternal=true order by c.id")
    public List<LiveSaleRealty> findToExternalExport();

    @Finder(query = "select distinct c from LiveSaleRealty c, Photo p where" +
            " c.objectStatus.id=1 AND c=p.realtyObject AND p.advertise=true AND c.publishAvito=true")
    public List<LiveSaleRealty> findToAvitoExport();

    @Finder (query = "select distinct c from LiveSaleRealty c, Photo p where" +
            " c.objectStatus.id=1 AND c=p.realtyObject AND p.advertise=true AND c.publishSite=true")
    public  List<LiveSaleRealty> findToSiteExport();

    @Finder (query = "select distinct c from LiveSaleRealty c, Photo p where" +
            " c.objectStatus.id=1 AND c=p.realtyObject AND p.advertise=true AND c.publishExternalAvito=true")
    public  List<LiveSaleRealty> findToExternalAvitoExport();
    /*так как для внутренних агенств не предусмотрен id
    а нужна уникальность обьектов , то мы используем данное поле , так как оно все равно не используется
     */
    @Finder (query = "select ro from RealtyObject ro WHERE ro.oldDbIdentifier= :id")
    public LiveSaleRealty findByOldDbIdentifier(@Named("id")Long id);

    @Finder (query ="select distinct c from LiveSaleRealty c, Photo p where" +
            " c.objectStatus.id=1 AND c=p.realtyObject AND p.advertise=true AND c.publishYandexRealty=true")
    public List<LiveSaleRealty> findToYandexExport();
}


