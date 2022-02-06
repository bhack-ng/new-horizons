package ru.simplex_software.arbat_baza.rest;

import org.apache.cxf.jaxrs.impl.ContainerRequestContextImpl;
import org.apache.cxf.message.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;

/**
 * Фильтр для авторизации запросов к REST API.
 */
public class AuthFilter implements ContainerRequestFilter {

    @Value("${token}")
    private String token;

    @Override
    public void filter(ContainerRequestContext context) {
        Message message = ((ContainerRequestContextImpl) context).getMessage();
        String url = getUrl(message);
        MultiValueMap<String, String> params = UriComponentsBuilder.fromHttpUrl(url).build().getQueryParams();

        // Прерывание запроса и отправка ответа с требованием авторизации.
        if (!token.equals(params.getFirst("token"))) {
            Response response = Response.status(Response.Status.UNAUTHORIZED).build();
            context.abortWith(response);
        }
    }

    /**
     * Получение полного URL запроса.
     *
     * @param message данные запроса.
     * @return полный URL запроса.
     */
    private String getUrl(Message message) {
        String url = message.get(Message.REQUEST_URL).toString();
        Object query = message.get(Message.QUERY_STRING);
        return url + (query == null ? "" : "?" + query.toString());
    }
}
