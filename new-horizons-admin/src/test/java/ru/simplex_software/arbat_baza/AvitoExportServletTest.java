package ru.simplex_software.arbat_baza;

import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ru.simplex_software.arbat_baza.dao.RealtyObjectDAO;
import ru.simplex_software.arbat_baza.dao.fias.FiasObjectDAO;
import ru.simplex_software.arbat_baza.model.Address;
import ru.simplex_software.arbat_baza.model.GeoCoordinates;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.arbat_baza.model.fias.FiasAddressVector;
import ru.simplex_software.arbat_baza.model.fias.FiasObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.easymock.EasyMock.anyLong;
import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

/**
 * .
 */
@RunWith(EasyMockRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class AvitoExportServletTest {

    @Mock
    private static RealtyObjectDAO realtyObjectDAO;

    @Mock
    private FiasObjectDAO fiasObjectDAO;

    @Mock
    private GeocodeService geocodeService;

    private GeoCoordinates coord;
    private GeoCoordinates coordRealtyObject;
    private RealtyObject realtyObject;

    @Before
    public void init() throws Exception {
        RealtyObject object = makeRealtyObject();
        List<FiasObject> cities = makeCityList();
        GeoCoordinates coordinates = makeGeoCoordinates();

        expect(realtyObjectDAO.get(anyLong())).andReturn(object);
        expect(fiasObjectDAO.findCityByRegion(anyString())).andReturn(cities);
        expect(fiasObjectDAO.findRegionByRegionCode(anyString())).andReturn(cities.get(0));
        expect(fiasObjectDAO.findByAOGUID(anyString())).andReturn(cities.get(0));
        expect(geocodeService.getGeoCoordinates(anyString())).andReturn(coordinates);

        replay(realtyObjectDAO);
        replay(fiasObjectDAO);
        replay(geocodeService);
    }

    @Test
    @Transactional
    public void findNearestCity() throws Exception {
        Long startTime = System.currentTimeMillis();
        Long endTime;
        Long executingTme;
        realtyObject = realtyObjectDAO.get(21L);                // получим rialtyObject с тествовым id
        FiasObject targetCity = null;                           // это будет ближацщей город , который мы и хотим найти
        FiasObject region;                                      // регион , поп которому мы юудем браь выборку городов
        String regionName = "";                                 // название треуемого региона

        if (realtyObject.getAddress().getFiasAddressVector().getLevel7() != null) {            //седьмой только для примера
            List<FiasObject> cityList;  //сяда мы будем записывать все города региона ,искомого объекта
            String address;             //строка адреса по каторой будем искать координаты городов в яндексе
            //это название региона , которое будет входить в строку поиска координат города
            /*так можно получить id региона */
            String regionCode = realtyObject.getAddress().getFiasAddressVector().getLevel1().getREGIONCODE();
            cityList = fiasObjectDAO.findCityByRegion(regionCode);

            if ((cityList != null) && (cityList.size() != 0)) {
                region = fiasObjectDAO.findRegionByRegionCode(regionCode);       //получим сем регион
                regionName = region.toShortString();                        //получим название региона
            }
            /**
             * координаты у городов присваиваются при ипотре в avito , так что они
             * Фиас оъекта(города) отсутствуют,если этот город никогда прежде
             * не рассматривался как кондидат на то  ,чтобы быть ближайшим к населенному пунку при импорте
             *
             */
            for (FiasObject city : cityList) {

                address = "";
                address = address + regionName;
                /**проверка на наличие координа у Фиас объекта(города)
                 * в случае отсутствия присвоим координаты  ,которые будут получены с помощью
                 * функции getGeoCoordinates класса GeocodeService
                 */
                if ((city.getLongitude() == null) || (city.getLongitude() == null)) {
                    /**получение координат города.*/
                    address = makeAddress(city, address);

                    coord = geocodeService.getGeoCoordinates(address);
                    city.setLatitude(coord.getLatitude());
                    city.setLongitude(coord.getLongitude());

                    fiasObjectDAO.saveOrUpdate(city);
                }
            }
            coordRealtyObject = geocodeService.getGeoCoordinates(realtyObject.getAddress().getFiasAddressVector().toString());

            /**сортировка списка городов по удаленности от текущего обьекта. */
            cityList.sort(new Comparator<FiasObject>() {
                @Override
                public int compare(FiasObject o1, FiasObject o2) {

                    Double distance1;
                    Double distance2;
                    double x = 0;       //квадрат разности коррдинат latitude
                    double y = 0;       //квадрат разности координат longitude

                    x = Math.pow(o1.getLatitude() - coordRealtyObject.getLatitude(), 2);
                    y = Math.pow(o1.getLongitude() - coordRealtyObject.getLongitude(), 2);
                    distance1 = x + y;

                    x = Math.pow(o2.getLatitude() - coordRealtyObject.getLatitude(), 2);
                    y = Math.pow(o2.getLongitude() - coordRealtyObject.getLongitude(), 2);
                    distance2 = x + y;

                    return (distance1 <= distance2) ? -1 : 1;
                }
            });

            targetCity = cityList.get(0);       //выбираем самый близкий к обьекту город
        }

        endTime = System.currentTimeMillis();
        executingTme = endTime - startTime;

        /**созвращаем название ближайшего к объекту города . */
        System.out.println("Изначальный адрес:" + realtyObject.getAddress().getFiasAddressVector().toString());
        System.out.println("для 24 городов заняло :" + executingTme + "милли секунд");
        System.out.println("Полученный город:" + targetCity.getFORMALNAME());
    }

    /**
     * @param fiasObject FiasObject город
     * @param str        строка из которой будем составлять адрес(уже cодержит регион)
     * @return
     */
    public String makeAddress(FiasObject fiasObject, String str) {
        String address = str;
        FiasObject district = fiasObjectDAO.findByAOGUID(fiasObject.getPARENTGUID());

        if (district.getAOLEVEL() == 1)
            address = address + "," + fiasObject.toShortString();
        else if (district.getAOLEVEL() == 3) {
            address = address + "," + district.toShortString() + "," + fiasObject.toShortString();
        }
        return address;
    }

    private RealtyObject makeRealtyObject() {
        RealtyObject object = new RealtyObject();
        Address address = new Address();
        FiasAddressVector vector = new FiasAddressVector();
        FiasObject level1 = new FiasObject();
        level1.setREGIONCODE("RegionCode");
        vector.setLevel1(level1);
        FiasObject level7 = new FiasObject();
        vector.setLevel7(level7);
        address.setFiasAddressVector(vector);
        object.setAddress(address);

        return object;
    }

    private List<FiasObject> makeCityList() {
        List<FiasObject> cityList = new ArrayList<>();

        FiasObject first = new FiasObject();
        first.setPARENTGUID("FirstRegionCode");
        first.setFORMALNAME("FirstFormalName");
        first.setSHORTNAME("FirstShortName");
        first.setLongitude(12.5);
        first.setLatitude(3.9);
        cityList.add(first);

        FiasObject second = new FiasObject();
        second.setPARENTGUID("SecondRegionCode");
        second.setFORMALNAME("SecondFormalName");
        second.setSHORTNAME("SecondShortName");
        second.setLongitude(4.9);
        second.setLatitude(1.67);
        cityList.add(second);

        return cityList;
    }

    private GeoCoordinates makeGeoCoordinates() {
        GeoCoordinates coordinates = new GeoCoordinates();

        coordinates.setLatitude(1.8);
        coordinates.setLongitude(3.1);

        return coordinates;
    }
}
