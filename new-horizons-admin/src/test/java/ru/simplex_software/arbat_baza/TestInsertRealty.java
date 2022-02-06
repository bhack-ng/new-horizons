package ru.simplex_software.arbat_baza;

import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ru.simplex_software.arbat_baza.dao.AgentDAO;
import ru.simplex_software.arbat_baza.dao.MetroStationDAO;
import ru.simplex_software.arbat_baza.dao.RealtyFilterDAO;
import ru.simplex_software.arbat_baza.dao.RealtyObjectByFilterDAOImpl;
import ru.simplex_software.arbat_baza.dao.StreetDAO;
import ru.simplex_software.arbat_baza.model.Agent;
import ru.simplex_software.arbat_baza.model.MetroStation;
import ru.simplex_software.arbat_baza.model.RealtyFilter;
import ru.simplex_software.arbat_baza.model.RealtyObjectType;
import ru.simplex_software.arbat_baza.model.SortByField;
import ru.simplex_software.arbat_baza.model.Street;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.anyInt;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

/**
 * .
 */
@RunWith(EasyMockRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class TestInsertRealty {
    private static final Logger LOG = LoggerFactory.getLogger(TestInsertRealty.class);

    @Mock
    private RealtyObjectByFilterDAOImpl realtyObjectByFilterDAO;

    @Mock
    private StreetDAO streetDAO;

    @Mock
    private MetroStationDAO metroStationDAO;

    @Mock
    private AgentDAO agentDAO;

    @Mock
    private RealtyFilterDAO realtyFilterDAO;

    @Before
    public void init() {
        expect(agentDAO.get(anyString())).andReturn(getAgent());
        expect(streetDAO.getStreetByName(anyString())).andReturn(getStreet()).anyTimes();
        expect(realtyObjectByFilterDAO.countByFilter(anyObject())).andReturn(10L).anyTimes();
        expect(realtyObjectByFilterDAO.findIdByFilter(anyObject(), anyInt(), anyInt())).andReturn(makeLongList()).anyTimes();
        expect(metroStationDAO.getStationByName(anyString())).andReturn(makeMetroStation());

        replay(agentDAO);
        replay(streetDAO);
        replay(realtyObjectByFilterDAO);
        replay(metroStationDAO);
    }

    @Test()
    @Transactional
    public void doTest() {
        Agent admin = agentDAO.get("admin");
        RealtyFilter filter = admin.getFilter();

        long x = realtyObjectByFilterDAO.countByFilter(filter);
        List<Long> idByFilter = realtyObjectByFilterDAO.findIdByFilter(filter, 0, 100000000);
        Assert.assertEquals(x, idByFilter.size());
        LOG.info("object count=" + x);

        for (RealtyObjectType rot : RealtyObjectType.values())
            for (SortByField sf : SortByField.values()) {
                filter.setSortField(sf);
                filter.setRealtyObjectType(rot);
                realtyFilterDAO.saveOrUpdate(filter);

                long y = realtyObjectByFilterDAO.countByFilter(filter);
                List<Long> list = realtyObjectByFilterDAO.findIdByFilter(filter, 0, 100000000);
                LOG.info("object count=" + y);
                Assert.assertEquals("count not equals list.size sort field=" + sf, y, list.size());
            }
    }

    @Test
    @Transactional
    public void getStreetTest() {
        Street street = streetDAO.getStreetByName("Новый Арбат");
        Assert.assertNotNull("Улица не найдена", street);
        LOG.info("setreet.id=" + street.getId());

        Street street01 = streetDAO.getStreetByName("Рублево-Успенское шоссе");

    }

    @Test
    @Transactional
    public void getMetroStationTest() {
        MetroStation station = metroStationDAO.getStationByName("Арбатская");
        Assert.assertNotNull("Станция метро не найдена", station);
        LOG.info("station.id=" + station.getId());
    }

    private Agent getAgent() {
        return new Agent();
    }

    private Street getStreet() {
        return new Street();
    }

    private List<Long> makeLongList() {
        List<Long> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            list.add((long) i);
        }
        return list;
    }

    private MetroStation makeMetroStation() {
        MetroStation station = new MetroStation();
        station.setId(10L);
        return station;
    }
}
