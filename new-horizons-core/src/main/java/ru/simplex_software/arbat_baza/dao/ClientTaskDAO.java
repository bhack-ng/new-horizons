package ru.simplex_software.arbat_baza.dao;

import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import net.sf.autodao.Limit;
import net.sf.autodao.Named;
import net.sf.autodao.Offset;
import ru.simplex_software.arbat_baza.model.Agent;
import ru.simplex_software.arbat_baza.model.clients.Client;
import ru.simplex_software.arbat_baza.model.clients.ClientTask;

import java.util.Date;
import java.util.List;

public interface ClientTaskDAO extends Dao<ClientTask, Long> {

    @Finder(query = "FROM ClientTask")
    List<ClientTask> findAll();

    @Finder(query = "FROM ClientTask WHERE client = :client")
    List<ClientTask> findByClient(@Named("client") Client client);

    @Finder(query = "SELECT id FROM ClientTask WHERE creator = :creator")
    List<Long> findIdsByCreator(@Named("creator") Agent agent, @Limit int limit, @Offset int offset);

    @Finder(query = "SELECT id FROM ClientTask WHERE executor = :executor")
    List<Long> findIdsByExecutor(@Named("executor") Agent agent, @Limit int limit, @Offset int offset);

    @Finder(query = "SELECT id FROM ClientTask WHERE executor = :executor " +
            "AND executionDatetime < :tomorrow AND status = 'ACTUAL'")
    List<Long> findIdsByExecutorToday(@Named("executor") Agent agent,
                                      @Named("tomorrow") Date tomorrow,
                                      @Limit int limit, @Offset int offset);

    @Finder(query = "SELECT COUNT(*) FROM ClientTask WHERE creator = :creator")
    long countByCreator(@Named("creator") Agent agent);

    @Finder(query = "SELECT COUNT(*) FROM ClientTask WHERE executor = :executor")
    long countByExecutor(@Named("executor") Agent agent);

    @Finder(query = "SELECT COUNT(*) FROM ClientTask WHERE executor = :executor " +
            "AND executionDatetime < :tomorrow AND status = 'ACTUAL'")
    long countByExecutorToday(@Named("executor") Agent agent, @Named("tomorrow") Date tomorrow);
}
