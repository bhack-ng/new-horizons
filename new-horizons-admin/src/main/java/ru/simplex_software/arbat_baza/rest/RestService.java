package ru.simplex_software.arbat_baza.rest;

import org.springframework.stereotype.Service;
import ru.simplex_software.arbat_baza.dao.ClientDAO;
import ru.simplex_software.arbat_baza.model.clients.Client;

import javax.annotation.Resource;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Service
@Path("/")
public class RestService {

    @Resource
    private ClientDAO clientDAO;

    @POST
    @Path("client/{name}/{email}/{telephone}/{comment}")
    public Response createClient(@PathParam("name") String name, @PathParam("email") String email,
                                 @PathParam("telephone") String telephone, @PathParam("comment") String comment) {
        Client client = new Client();
        client.setName(name);
        client.setEmail(email);
        client.setMobilePhone(telephone);
        client.setComment(comment);
        clientDAO.saveOrUpdate(client);

        return Response.status(Response.Status.CREATED).build();
    }
}
