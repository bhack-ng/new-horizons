package ru.simplex_software.arbat_baza;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import ru.simplex_software.arbat_baza.dao.AgentDAO;
import ru.simplex_software.arbat_baza.model.Agent;

import javax.annotation.Resource;
import java.util.ArrayList;

/**.*/
public class HibernateDaoImpl implements UserDetailsService {
    private static final Logger LOG= LoggerFactory.getLogger(HibernateDaoImpl.class);
    @Resource
    private AgentDAO agentDAO;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Agent byLogin = agentDAO.findByLogin(username);
        if(byLogin==null){
            throw new UsernameNotFoundException(username + " not found");
        }
        ArrayList<GrantedAuthority> grantedAuthorities = getGrantedAuthorities( byLogin );
        User userDetails = new User(username,byLogin.getPassword(),!byLogin.getBlocked(),true,true,true,grantedAuthorities);
        return userDetails;
    }
    private static ArrayList<GrantedAuthority> getGrantedAuthorities( Agent agent) {

        final ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        if(Agent.Role.ADMIN.equals(agent.getRole())){
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_AGENT"));
        return grantedAuthorities;
    }


}
