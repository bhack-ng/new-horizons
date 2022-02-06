package ru.simplex_software.school_mark.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.simplex_software.arbat_baza.xml.CommercialExportServlet;

import javax.annotation.Resource;

/**
 * CommercialExportServlet test.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CommericalExportTest {
    private static final Logger LOG = LoggerFactory.getLogger(CommericalExportTest.class);

    @Resource
    private CommercialExportServlet commercialExportServlet;


    @Test @Transactional
    public void doIt() throws Exception{
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/cian/commerical.xml");
        final MockHttpServletResponse response = new MockHttpServletResponse();
        commercialExportServlet.handleRequest(request, response);
        System.out.println(response.getContentAsString());
    }
}
