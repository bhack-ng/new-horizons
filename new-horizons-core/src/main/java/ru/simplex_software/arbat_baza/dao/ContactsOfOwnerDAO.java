package ru.simplex_software.arbat_baza.dao;

import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import net.sf.autodao.Named;
import ru.simplex_software.arbat_baza.model.Agency;
import ru.simplex_software.arbat_baza.model.Agent;
import ru.simplex_software.arbat_baza.model.ContactsOfOwner;
import ru.simplex_software.arbat_baza.model.RealtyObject;

import java.util.List;

/**
 * .
 */
public interface ContactsOfOwnerDAO extends Dao<ContactsOfOwner, Long>{

    @Finder(query = "SELECT distinct c FROM ContactsOfOwner c where c.realtyObject =:realtyObject AND c.name=:name")
    public List<ContactsOfOwner> findByRealtyObject(@Named("realtyObject") RealtyObject realtyObject,
                                                    @Named("name") String name);

    @Finder(query = "from ContactsOfOwner where realtyObject.agent=:agent")
    List<ContactsOfOwner> findAll(@Named("agent") Agent agent);
}
