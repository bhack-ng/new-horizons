package ru.simplex_software.ord.servlets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestHandler;
import ru.simplex_software.arbat_baza.dao.FloorSchemeDAO;
import ru.simplex_software.arbat_baza.dao.RealtyObjectDAO;
import ru.simplex_software.arbat_baza.model.FloorScheme;
import ru.simplex_software.arbat_baza.model.RealtyObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Сервлет для привязки объекта к схеме.
 */
public class CoordsSaverServlet implements HttpRequestHandler {

    @Autowired
    private RealtyObjectDAO realtyObjectDAO;

    @Autowired
    private FloorSchemeDAO floorSchemeDAO;

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // Получение строки с данными.
        String[] data = request.getReader().lines().findFirst().get().split(",");

        // Поучение id объекта, id схемы и координат.
        long realtyObjectId = Long.parseLong(data[0]);
        long floorSchemeId = Long.parseLong(data[1]);
        String coords = data[2];
        for (int i = 3; i < data.length; i++) {
            coords += "," + data[i];
        }

        // Привязка.
        RealtyObject realtyObject = realtyObjectDAO.get(realtyObjectId);
        FloorScheme floorScheme = floorSchemeDAO.get(floorSchemeId);
        realtyObject.setFloorScheme(floorScheme);
        realtyObject.setFloorSchemeCoordinates(coords);

        // Сохранение объекта.
        realtyObjectDAO.saveOrUpdate(realtyObject);
    }
}
