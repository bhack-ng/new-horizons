package ru.simplex_software.arbat_baza.dao;

import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import net.sf.autodao.Named;
import ru.simplex_software.arbat_baza.model.clients.JobPosition;
import ru.simplex_software.arbat_baza.model.clients.JuridicalClient;
import ru.simplex_software.arbat_baza.model.clients.NaturalClient;

public interface JobPositionDAO extends Dao<JobPosition, Long> {

    @Finder(query = "SELECT COUNT(*) > 0 FROM JobPosition WHERE " +
            "employee = :employee AND company = :company " +
            "AND name = :name")
    boolean hasDuplicate(@Named("employee") NaturalClient employee,
                         @Named("company") JuridicalClient company,
                         @Named("name") String name);
}
