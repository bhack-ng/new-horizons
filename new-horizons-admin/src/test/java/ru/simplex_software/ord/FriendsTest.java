package ru.simplex_software.ord;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * .
 */
public class FriendsTest {
    private static final Logger LOG = LoggerFactory.getLogger(FriendsTest.class);

    @Test
    public void testIt() throws Exception {
        final MockHttpServletResponse response = new MockHttpServletResponse();

        new FriendsServlet().handleRequest(null, response);
        System.out.println(response.getContentAsString());
    }
}
