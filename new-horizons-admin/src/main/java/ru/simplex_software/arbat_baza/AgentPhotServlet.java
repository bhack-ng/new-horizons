package ru.simplex_software.arbat_baza;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestHandler;
import ru.simplex_software.arbat_baza.dao.AgentDAO;
import ru.simplex_software.arbat_baza.model.Agent;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * .
 */
public class AgentPhotServlet  implements HttpRequestHandler {
    private static final Logger LOG = LoggerFactory.getLogger(AgentPhotServlet  .class);
    @Resource
    private AgentDAO agentDAO;

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String login = request.getParameter("login");

        Agent agent = agentDAO.get(login);
        if (agent == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
            return;
        }

//        long cacheAge=60*60*24*30*12;//1 год
//        long expiry = new Date().getTime() + cacheAge*1000;
//
//        response.setDateHeader("Expires", expiry);
//        response.setHeader("Cache-Control", "max-age="+ cacheAge);

        response.setContentType(agent.getPhotoContentType());
        response.setContentLength(agent.getPhoto().length);
        response.getOutputStream().write(agent.getPhoto());
    }
}
