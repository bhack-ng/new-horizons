package ru.simplex_software.ord.servlets;

import org.springframework.web.HttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ShowOfficesServlet implements HttpRequestHandler {
    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String path = request.getServletContext().getRealPath(request.getServletPath());
        String newPath = request.getRequestURL().toString().replace(request.getServletPath(), "");

        String data = new String(Files.readAllBytes(Paths.get(path)));
        data = data.replace("${applicationUrl}", newPath);

        response.getWriter().print(data);
    }
}
