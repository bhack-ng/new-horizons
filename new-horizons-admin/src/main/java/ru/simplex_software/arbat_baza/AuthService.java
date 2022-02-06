package ru.simplex_software.arbat_baza;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.simplex_software.arbat_baza.dao.AgentDAO;
import ru.simplex_software.arbat_baza.model.Agent;

import javax.annotation.Resource;

/**.*/
public class AuthService {
    @Resource
    AgentDAO agentDAO;
    public Agent getLogginedAgent(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        return agentDAO.findByLogin(name);
    }

}
