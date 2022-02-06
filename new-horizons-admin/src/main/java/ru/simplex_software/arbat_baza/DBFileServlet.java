package ru.simplex_software.arbat_baza;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestHandler;
import ru.simplex_software.arbat_baza.dao.DbFileDao;
import ru.simplex_software.arbat_baza.model.DbFile;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * return content of file.
 */
public class DBFileServlet implements HttpRequestHandler {
    private static final Logger LOG = LoggerFactory.getLogger(DBFileServlet.class);
    @Resource
    private DbFileDao dbFileDao;
    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String idStr = request.getParameter("id");


        final DbFile dbFile = dbFileDao.get(Long.parseLong(idStr));
        if (dbFile == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
            return;
        }


        response.setContentType(dbFile.getContentType());
        response.setContentLength((int) dbFile.getLength());
        response.getOutputStream().write(dbFile.getContent().getData());
    }

}
