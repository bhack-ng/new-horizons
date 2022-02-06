package ru.simplex_software.arbat_baza.dao;

import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import net.sf.autodao.Named;
import ru.simplex_software.arbat_baza.model.Agent;

import java.util.List;


/**
 * Поиск Анентов недвижимости.
 */
public interface AgentDAO extends Dao<Agent,String> {
    @Finder(query = "from Agent")
    public List<Agent> findAll();

    @Finder(query =" from Agent where blocked = false")
    public  List<Agent> findVisible();

    @Finder(query = "from Agent where login= :login")
    public Agent findByLogin(@Named("login") String login);
}
