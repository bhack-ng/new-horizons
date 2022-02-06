package ru.simplex_software.arbat_baza.xml;

import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplex_software.arbat_baza.dao.PrivateHouseRentDao;
import ru.simplex_software.arbat_baza.dao.PrivateHouseSaleDao;
import ru.simplex_software.arbat_baza.dao.fias.FiasSocrDAO;
import ru.simplex_software.arbat_baza.model.AbstractBuilding;
import ru.simplex_software.arbat_baza.model.AbstractOptions;
import ru.simplex_software.arbat_baza.model.Address;
import ru.simplex_software.arbat_baza.model.Agency;
import ru.simplex_software.arbat_baza.model.Agent;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.arbat_baza.model.RealtyObjectType;
import ru.simplex_software.arbat_baza.model.fias.FiasAddressVector;
import ru.simplex_software.arbat_baza.model.fias.FiasObject;
import ru.simplex_software.arbat_baza.model.fias.FiasSocr;
import ru.simplex_software.arbat_baza.model.price.LiveRentPrice;
import ru.simplex_software.arbat_baza.model.price.SimplePrice;
import ru.simplex_software.arbat_baza.model.privateHouses.PrivateHouseRent;
import ru.simplex_software.arbat_baza.model.privateHouses.PrivateHouseSale;
import ru.simplex_software.arbat_baza.model.privateHouses.WallsType;
import ru.simplex_software.arbat_baza.model.stead.SteadArea;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

@RunWith(EasyMockRunner.class)
public class PrivateHouseRentTester {
    private static final Logger LOG = LoggerFactory.getLogger(PrivateHouseRentTester.class);

    @Mock
    private static HttpServletRequest request;

    @Mock
    private static HttpServletResponse response;

    @Mock
    private static FiasSocrDAO fiasSocrDAO;

    @Mock
    private PrivateHouseRentDao privateHouseRentDao;

    @Mock
    private PrivateHouseSaleDao privateHouseSaleDao;

    @TestSubject
    private YandexPrivateHouseServlet yandexPrivateHouseServlet = new YandexPrivateHouseServlet();

    private PrintWriter printWriter = new PrintWriter(System.out);

    private SteadArea steadArea = new SteadArea();
    private AbstractOptions options = new AbstractOptions();
    private AbstractBuilding abstractBuilding = new AbstractBuilding();
    private SimplePrice simplePrice = new SimplePrice(); // price for sale
    private LiveRentPrice liveRentPrice = new LiveRentPrice(); // price for rent
    private Agency agency = new Agency();
    private Agent agent = new Agent();

    @Before
    public void init() throws IOException {
        List<? extends RealtyObject> rentList = createRentList();
        List<? extends RealtyObject> saleList = createSaleList();

        FiasSocr fiasSocr = new FiasSocr();
        fiasSocr.setFullName("FullName");

        initHandlerMock();

        expect(privateHouseRentDao.findToYandexExport()).andReturn((List<PrivateHouseRent>) rentList);
        expect(privateHouseSaleDao.findToYandexExport()).andReturn((List<PrivateHouseSale>) saleList);
        expect(fiasSocrDAO.findRegionFullName(0, "ShortName")).andReturn(fiasSocr).times(2);

        replay(privateHouseRentDao);
        replay(privateHouseSaleDao);
        replay(fiasSocrDAO);
        LOG.info("Before is done ");
    }

    @Test
    public void testLiveSaleRealtyDAO() throws IOException {
        yandexPrivateHouseServlet.handleRequest(request, response);
    }

    private void initHandlerMock() throws IOException {
        expect(request.getScheme()).andReturn("http").anyTimes();
        expect(request.getServerName()).andReturn("hornsandhooves").anyTimes();
        expect(request.getServerPort()).andReturn(8080).anyTimes();
        expect(request.getContextPath()).andReturn("new-horizons").anyTimes();
        expect(response.getWriter()).andReturn(printWriter);

        replay(request);
        replay(response);
    }

    private List<? extends RealtyObject> createRentList() {
        List<PrivateHouseSale> list = new ArrayList<>();

        agent.setAgency(agency);
        liveRentPrice.setValue(11111111);
        steadArea.setLendArea((double) 111111111);
        steadArea.setTotal((double) 222222222);

        PrivateHouseSale privateHouseSale = new PrivateHouseSale();
        privateHouseSale.setId(11111L);
        privateHouseSale.setWallsType(WallsType.BRICK);
        privateHouseSale.setRealtyObjectType(RealtyObjectType.PRIVATE_HOUSE_SALE);
        privateHouseSale.setAgent(agent);
        privateHouseSale.setArea(steadArea);
        privateHouseSale.setOptions(options);
        privateHouseSale.setBuilding(abstractBuilding);
        privateHouseSale.setPrice(simplePrice);
        privateHouseSale.setAddress(createAddress());

        list.add(privateHouseSale);
        return list;
    }

    private List<? extends RealtyObject> createSaleList() {
        List<PrivateHouseRent> list = new ArrayList<>();

        agent.setAgency(agency);
        simplePrice.setValue(222222222);
        steadArea.setLendArea((double) 222222222);
        steadArea.setTotal((double) 222222222);

        PrivateHouseRent privateHouseRent = new PrivateHouseRent();
        privateHouseRent.setId(2222L);
        privateHouseRent.setWallsType(WallsType.EXPERIMENTAL_MATERIAL);
        privateHouseRent.setRealtyObjectType(RealtyObjectType.PRIVATE_HOUSE_RENT);
        privateHouseRent.setAgent(agent);
        privateHouseRent.setArea(steadArea);
        privateHouseRent.setOptions(options);
        privateHouseRent.setBuilding(abstractBuilding);
        privateHouseRent.setPrice(liveRentPrice);
        privateHouseRent.setAddress(createAddress());

        list.add(privateHouseRent);
        return list;
    }

    private Address createAddress() {
        Address address = new Address();
        FiasAddressVector addressVector = new FiasAddressVector();
        FiasObject fiasObject = new FiasObject();
        fiasObject.setAOLEVEL(0);
        fiasObject.setSHORTNAME("ShortName");
        fiasObject.setFORMALNAME("FormalName");
        addressVector.setLevel1(fiasObject);
        address.setFiasAddressVector(addressVector);

        return address;
    }
}
