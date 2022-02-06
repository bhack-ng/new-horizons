package ru.simplex_software.arbat_baza.dao.fias;

import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import net.sf.autodao.Limit;
import net.sf.autodao.Named;
import ru.simplex_software.arbat_baza.model.fias.FiasObject;

import java.util.List;


/**
 * Внешние агентства недвижимости.
 */
public interface FiasObjectDAO extends Dao<FiasObject,String> {
    @Finder(query = "from FiasObject where aolevel=1 AND CURRSTATUS=0 ORDER BY FORMALNAME")
    public List<FiasObject> findRegion();

    /**
     * @param cianId cian id from http://www.cian.ru/admin_areas.php
     * @return fiasObject level 1
     */
    @Finder(query = "select fo  from FiasObject  fo, CianAdminArea  caa where caa.cianId=:cianId AND fo.AOGUID=caa.fiasAoguid")
    public List<FiasObject> findRegionByCianId(@Named("cianId")int cianId);

    @Finder(query = "from FiasObject where aolevel=:level  AND parentguid=:id AND CURRSTATUS=0 ORDER BY FORMALNAME" )
    public List<FiasObject>  findChild(@Named("id")String id, @Named("level")int level);


    @Finder(query = "select count (*) from FiasObject where aolevel=:level  AND parentguid=:id AND CURRSTATUS=0" )
    public Long  getChildCount(@Named("id")String id, @Named("level")int level);


    @Finder(query = "from FiasObject where parentguid=:id AND CURRSTATUS=0" )
    public List<FiasObject>  findChild(@Named("id")String aoguid);

    @Finder (query = "from FiasObject where aolevel=:level  AND parentguid=:id AND CURRSTATUS=0 AND upper (FORMALNAME) LIKE :str ORDER BY FORMALNAME ")
    public List<FiasObject> findStreet(@Named("id")String id , @Named("level")int level,@Limit int limit,@Named("str")String str);

    @Finder(query = "from  FiasObject  where aoguid=:aoguid AND CURRSTATUS=0 ")
    FiasObject findByAOGUID(@Named("aoguid")String aoguid) ;

    @Finder(query = "from FiasObject where regioncode=:regionCode AND CURRSTATUS=0 AND AOLEVEL=1 ")
    public FiasObject findRegionByRegionCode(@Named("regionCode") String regionCode);


    /**
     * @param level level starting from 1
     * @param socrName сокращённое наименование типа объекта. Наприме г.б городб гор.
     * @param name Имня объекта (OFFNAME)
     * @param parent родительский объект
     * @return List with size 0 or 1 if database of FiasObject correct.
     */
    @Finder(query = "select distinct(fo) from FiasObject fo, FiasSocr fs1, FiasSocr fs2 " +
            " where fo.FORMALNAME=:name AND fo.PARENTGUID=:parent AND fo.AOLEVEL=:level AND fo.SHORTNAME= fs1.socrName  " +
                        " AND fs2.fullName=fs1.fullName AND fs2.socrName=:socrName AND CURRSTATUS=0")
    List<FiasObject> findByLevelAndSockr(@Named("level") int level, @Named("socrName")String socrName,
                                   @Named("name")String name, @Named("parent")String parent);




    /**
     * @param level level starting from 1
     * @param name Имня объекта (OFFNAME)
     * @param parent родительский объект
     * @return List with size 0 or 1 if database of FiasObject correct.
     */
    @Finder(query = "select fo from FiasObject fo" +
            " where fo.FORMALNAME=:name AND fo.PARENTGUID=:parent AND fo.AOLEVEL=:level AND CURRSTATUS=0")
    List<FiasObject> findByLevelAndSockr(@Named("level") int level,  @Named("name")String name,
                                         @Named("parent")String parent);


    /**
     * Поск региона по его имени
     * @param name region name(FORMALNAME)
     * @return
     */

    @Finder(query = "from FiasObject  where FORMALNAME =:name AND CURRSTATUS=0 AND AOLEVEL=1 " )
    public FiasObject findRegionByName(@Named("name") String name);

    @Finder (query = "from FiasObject  where FORMALNAME = :name AND CURRSTATUS=0 AND AOLEVEL=4")
    public FiasObject findCityByName(@Named("name") String name);

    @Finder(query = "from FiasObject where FORMALNAME = :name AND CURRSTATUS = 0 AND AOLEVEL = 1")
    public FiasObject findReionByName(@Named("name") String name);

    @Finder(query = "select fo from FiasObject fo where fo.REGIONCODE =:regionCode AND fo.AOLEVEL=4 AND CURRSTATUS=0")
    public List<FiasObject> findCityByRegion(@Named("regionCode") String regionCode);

}
