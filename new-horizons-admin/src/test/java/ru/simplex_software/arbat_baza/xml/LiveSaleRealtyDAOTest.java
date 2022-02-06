package ru.simplex_software.arbat_baza.xml;

import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import ru.simplex_software.arbat_baza.dao.LiveSaleRealtyDAO;
import ru.simplex_software.arbat_baza.model.Agency;
import ru.simplex_software.arbat_baza.model.Agent;
import ru.simplex_software.arbat_baza.model.RealtyObjectType;
import ru.simplex_software.arbat_baza.model.live.LiveSaleRealty;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.slf4j.LoggerFactory.getLogger;

@RunWith(EasyMockRunner.class)
public class LiveSaleRealtyDAOTest {
    private static final Logger LOG = getLogger(LiveSaleRealtyDAOTest.class);

    @Mock
    private static HttpServletRequest request;

    @Mock
    private static HttpServletResponse response;

    @Mock
    private LiveSaleRealtyDAO liveSaleRealtyDAO;

    @TestSubject
    private YandexSaleExportServlet yandexSaleExportServlet = new YandexSaleExportServlet();

    private LiveSaleRealty liveSaleRealty;
    private List<LiveSaleRealty> list;

    @Before
    public void init() throws IOException {
        list = new ArrayList<>();
        expect(request.getScheme()).andReturn("http").anyTimes();
        expect(request.getServerName()).andReturn("hornsandhooves").anyTimes();
        expect(request.getServerPort()).andReturn(8080).anyTimes();
        expect(request.getContextPath()).andReturn("new-horizons").anyTimes();

        liveSaleRealty = new LiveSaleRealty();
        Agency agency = new Agency();
        Agent agent = new Agent();
        liveSaleRealty.setId(1111111111111L);
        agent.setAgency(agency);
        liveSaleRealty.setAgent(agent);
        liveSaleRealty.setRealtyObjectType(RealtyObjectType.COMMERCE_LEASE);

        list.add(liveSaleRealty);

        expect(response.getWriter()).andReturn(new PrintWriter(System.out));
        replay(request);
        replay(response);

        expect(liveSaleRealtyDAO.findToYandexExport()).andReturn(list);
        replay(liveSaleRealtyDAO);
    }

    @Test
    public void testLiveSaleRealtyDAO() throws IOException {
        yandexSaleExportServlet.handleRequest(request, response);
    }
}
