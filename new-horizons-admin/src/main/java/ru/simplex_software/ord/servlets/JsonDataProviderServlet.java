package ru.simplex_software.ord.servlets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestHandler;
import ru.simplex_software.arbat_baza.dao.FloorSchemeDAO;
import ru.simplex_software.arbat_baza.model.FloorScheme;
import ru.simplex_software.ord.utils.JsonBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Сервлет, который отправляет данные в JSON'е.
 */
public class JsonDataProviderServlet implements HttpRequestHandler {

    // Имя функции.
    private static final String FUNC_NAME = "back";

    @Autowired
    private FloorSchemeDAO floorSchemeDAO;

    @Autowired
    private JsonBuilder jsonBuilder;

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // Получение JSON.
        String json = getJsonFromRequestParameter(request.getParameter("floorSchemeId"));
        String back = request.getParameter("callback");

        // Установка статуса.
        if (json.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        // Получение имени функции.
        String callbackName = (back == null) ? FUNC_NAME : back;
        json = callbackName + "(" + json + ");";

        // Ответ.
        response.setContentType("application/javascript;charset=UTF-8");
        response.getWriter().println(json);
    }

    // Получение JSON'a из запроса.
    private String getJsonFromRequestParameter(String parameter) {

        // Все схемы.
        if (parameter == null) {
            return jsonBuilder.buildAllFloorSchemesJson();
        }

        if (parameter.isEmpty() || parameter.equals("null")) return "";

        // Массив Id.
        if (parameter.contains("|")) {
            return jsonBuilder.buildFloorSchemesJson(parameter);

        } else {
            // Один Id.
            long id = Long.parseLong(parameter);

            FloorScheme floorScheme = floorSchemeDAO.get(id);

            return (floorScheme == null) ? "" : jsonBuilder.buildFloorSchemeJson(floorScheme);
        }
    }
}
