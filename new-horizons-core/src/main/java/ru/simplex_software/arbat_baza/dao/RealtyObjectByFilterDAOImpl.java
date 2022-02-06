package ru.simplex_software.arbat_baza.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import ru.simplex_software.arbat_baza.model.RealtyFilter;
import ru.simplex_software.arbat_baza.model.fias.FiasObject;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * .
 */

public class RealtyObjectByFilterDAOImpl {
    private static final Logger LOG = LoggerFactory.getLogger(RealtyObjectByFilterDAOImpl.class);
    @Resource
    private SessionFactory sessionFactory;
    @Resource
    private RealtyFilterDAO realtyFilterDAO;



    public long countByFilter(RealtyFilter filter) {
        if(filter.getRealtyObjectType()==null){
            return 0L;
        }
        String header = "select count(ro) from RealtyObject ro ";
        if(StringUtils.hasLength(filter.getPhone())){
            header = header + " inner join ro.contactsOfOwners as owner ";

        }
        header=header+" where ";
        Map<String, ? super Object> parameterMap= new HashMap<>();

        Query hQquery = fillParams(filter, parameterMap, header, false);

        long result = (long)hQquery.uniqueResult();
        return result;
    }



    public List<Long> findIdByFilter(RealtyFilter filter ,int offset, int limit) {
        if(filter.getRealtyObjectType()==null){
            return Collections.emptyList();
        }
        String header = "select ro.id from RealtyObject ro, AbstractArea aa, SimplePrice sp " ;
        if(StringUtils.hasLength(filter.getPhone())){
            header = header + " inner join ro.contactsOfOwners as owner ";

        }
        header=header+" where ro.area=aa AND ro.price=sp AND ";
        Map<String, ? super Object> parameterMap= new HashMap<>();

        Query hQquery = fillParams(filter, parameterMap, header, true);

        hQquery.setFirstResult(offset);
        hQquery.setMaxResults(limit);
        List list = hQquery.list();
        return list;
    }


    private Query fillParams(RealtyFilter filter, Map<String, ? super Object> parameterMap, String header, boolean addOrder) {
        String query=" ";
        {
            query = query + " ro.realtyObjectType='"+filter.getRealtyObjectType()+"' ";
        }

        {
            query = query + " AND (ro.externalObectExt IS " +(filter.isExternal()? " NOT ": " ")+ " null)";
        }
        if(StringUtils.hasLength(filter.getPhone())){
            query = query + " AND owner.phone=:phone";
            parameterMap.put("phone",filter.getPhone());
        }
        if(filter.getRealtyId()!=null){
            query = query + " AND ro.id = "+filter.getRealtyId();
        }
        if(filter.getAgentToFilter()!=null && !filter.isExternal() ){
            query = query + " AND ro.agent=:agent";
            parameterMap.put("agent",filter.getAgentToFilter());
        }
        if(filter.getObjectStatus()!=null){
            query = query + " AND ro.objectStatus=:objectStatus";
            parameterMap.put("objectStatus",filter.getObjectStatus());
        } else {
            query = query + " AND ro.objectStatus.name <> 'Удален'";
        }

        if(filter.getMetroStation()!=null){
            query = query + " AND ro.metroLocation.metroStation=:metroStation";
            parameterMap.put("metroStation",filter.getMetroStation());
        }

        if(filter.getStreet()!=null){
            query = query + " AND ro.address.street=:street ";
            parameterMap.put("street",filter.getStreet());
        }


        IntStream.range(0,7).filter(i->getLevel(filter, i)!=null).forEach(i->{

        });

        for (int i = 6;i>=0;i--){
            final FiasObject oLevel = getLevel(filter, i);
            if(oLevel!=null) {
                query = query + " AND ro.address.fiasAddressVector.level"+(i+1)+"=:level ";
                parameterMap.put("level", getLevel(filter, i));
                break;
            }
        }

        if(StringUtils.hasLength(filter.getBuildingNumber())){
            query = query + " AND ro.address.house_str=:house_str ";
            parameterMap.put("house_str",filter.getBuildingNumber());
        }
        if(filter.getBuildingType()!=null){
            query = query + " AND ro.building.buildingType=:buildingType ";
            parameterMap.put("buildingType",filter.getBuildingType());
        }
        if(filter.getFloor()!=null){
            query = query + " AND ro.building.floor=:floor ";
            parameterMap.put("floor",filter.getFloor());
        }
        if(filter.getFloor()!=null){
            query = query + " AND ro.building.floor=:floor ";
            parameterMap.put("floor",filter.getFloor());
        }
        if(filter.getPriceMin()!=null){
            query = query + " AND ro.price.value>=:priceMin ";
            parameterMap.put("priceMin",filter.getPriceMin());
        }
        if(filter.getPriceMax()!=null){
            query = query + " AND ro.price.value<=:priceMax ";
            parameterMap.put("priceMax",filter.getPriceMax());
        }
        if(filter.getCurrency()!=null){
            query = query + " AND ro.price.currency=:currency ";
            parameterMap.put("currency",filter.getCurrency());
        }
        if(filter.getRoomCount()!=null){
            query = query + " AND ro.area.roomsCount=:roomsCount ";
            parameterMap.put("roomsCount",filter.getRoomCount());
        }
        if(filter.isCallToday()){
            query = query + " AND ro.nextCall= current_date() ";
        }
        if (filter.isExternal() && filter.getExternalAgency()!= null){
            query = query+ " AND ro.externalObectExt.feed.externalAgency=:externalAgency ";
            parameterMap.put("externalAgency",filter.getExternalAgency());
        }


        if(addOrder && filter.getSortField()!=null){
            query = query+ " ORDER BY "+filter.getSortField().getHqlStr()+" NULLS LAST";
        }

         Session session = sessionFactory.getCurrentSession();
        Query hQquery = session.createQuery(header + query);
        for(String key:parameterMap.keySet()){
            hQquery.setParameter(key, parameterMap.get(key));
        }
        return hQquery;
    }


    public FiasObject getLevel(RealtyFilter filter, int zLevel) {
        if (filter.getFiasAddressVector() != null) {
            if (filter.getFiasAddressVector() != null) {
                return filter.getFiasAddressVector().toArray()[zLevel];
            }
        }
        return null;
    }


}
