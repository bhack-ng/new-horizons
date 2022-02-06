package ru.simplex_software.arbat_baza.xml;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})

public class TestYandexExportServlet {
    private static final Logger LOG = LoggerFactory.getLogger(TestYandexExportServlet.class);

    @Resource
    YandexLeaseExportServlet yandexLeaseExportServlet;

    @Resource
    YandexSaleExportServlet yandexSaleExportServlet;

    static HttpServletRequest request = createMock(HttpServletRequest.class);
    static HttpServletResponse response = createMock(HttpServletResponse.class);

    @Before
    public void init() throws IOException {

        expect(request.getScheme()).andReturn("http").anyTimes();
        expect(request.getServerName()).andReturn("hornsandhooves").anyTimes();
        expect(request.getServerPort()).andReturn(8080).anyTimes();
        expect(request.getContextPath()).andReturn("new-horizons").anyTimes();
        expect(response.getWriter()).andReturn(new PrintWriter(System.out));
        replay(request);
        replay(response);
    }

    @After
    public void afterTest() {
        reset(request);
        reset(response);
    }

    @Transactional
    @Test
    public void testYandexLeaseExportServlet() throws ServletException, IOException {
        LOG.info("\n testYandexLeaseExportServlet \n");
        yandexLeaseExportServlet.handleRequest(request, response);
    }

    @Transactional
    @Test
    public void testYandexSaleExportServlet() throws ServletException, IOException {
        LOG.info("\n testYandexSaleExportServlet \n");
        yandexSaleExportServlet.handleRequest(request, response);
    }
}

