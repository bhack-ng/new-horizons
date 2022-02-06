package ru.simplex_software.arbat_baza.dao.commerce;

import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import net.sf.autodao.Named;
import ru.simplex_software.arbat_baza.model.commerce.Commerce;

import java.util.List;

/**
 * Коммерческие объекты недвижимости.
 */
public interface CommerceDAO extends Dao<Commerce,Long> {
    @Finder(query = "from Commerce")
    public List<Commerce> findAll();

    @Finder(query = "select c from Commerce c, RealtyFilter filter where filter.id=:filterId AND " +
            " c.realtyObjectType=filter.realtyObjectType  AND "+
            "(c.id=filter.realtyId OR filter.realtyId is null) AND "+
            "(c.agent=filter.agentToFilter OR filter.agentToFilter is null) AND "+
            "(c.objectStatus=filter.objectStatus OR filter.objectStatus is null) AND "+
            "(c.metroLocation.metroStation=filter.metroStation OR filter.metroStation is null) AND "+
            "(c.address.street=filter.street OR filter.street is null) AND "+
            "(c.address.house_str=filter.buildingNumber OR filter.buildingNumber is null OR filter.buildingNumber ='') AND "+
            "(c.building.buildingType=filter.buildingType OR filter.buildingType is null) AND "+
            "(c.building.floor=filter.floor OR filter.floor is null) AND "+
            "(c.price.value >= filter.priceMin OR filter.priceMin is null) AND "+
            "(c.price.value <= filter.priceMax OR filter.priceMax is null) AND "+
            "(c.price.currency=filter.currency OR filter.currency is null) AND "+
            "(c.price.period=filter.period OR filter.period is null ) AND "+
            "(c.area.roomsCount=filter.roomCount OR filter.roomCount is null) AND "+
            "(c.nextCall= current_date() OR filter.callToday = false) " +
            "order by c.id "


    )
    public List<Commerce> findByFilter(@Named("filterId") Long filterId);

    @Finder(query = "select distinct c from Commerce c, Photo p where c.objectStatus.id=1 AND c=p.realtyObject AND p.advertise=true AND c.publishCian=true")
    public List<Commerce> findToCianExport();

    @Finder(query = "select distinct c from Commerce c, Photo p where c.objectStatus.id=1 AND c=p.realtyObject AND p.advertise=true AND c.publishExternal=true")
    public List<Commerce> findToExternalExport();

    @Finder(query = "select distinct c from Commerce c, Photo p where c.objectStatus.id=1 AND c=p.realtyObject AND p.advertise=true AND c.publishYandexRealty=true")
    public List<Commerce> findToYandexExport() ;
}
