package ru.simplex_software.ord.servlets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestHandler;
import ru.simplex_software.arbat_baza.dao.PhotoDataDAO;
import ru.simplex_software.arbat_baza.model.PhotoData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Сервлет для загрузки изображений из базы.
 */
public class ImageDownloadServlet implements HttpRequestHandler {

    @Autowired
    private PhotoDataDAO photoDataDAO;

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // Параметры.
        byte[] data = new byte[0];

        // Получение изображения.
        String idString = request.getParameter("imageId");
        if (idString != null) {
            long id = Long.parseLong(idString);
            PhotoData photoData = photoDataDAO.get(id);

            if (photoData != null) {
                data = photoData.getData();
            }
        }

        // Проверка на наличие данных.
        if (data.length == 0) {
            response.setStatus(404);
        }

        // Кеширование.
        long cacheAge = 60 * 60 * 24 * 30 * 12; // 1 год.
        long expiry = new Date().getTime() + cacheAge * 1000;
        response.setDateHeader("Expires", expiry);
        response.setHeader("Cache-Control", "max-age=" + cacheAge);

        // Отправка ответа.
        response.setContentLength(data.length);
        response.setContentType("image/png;");
        response.getOutputStream().write(data);
    }
}
