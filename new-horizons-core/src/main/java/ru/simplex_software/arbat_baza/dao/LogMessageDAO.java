package ru.simplex_software.arbat_baza.dao;

import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import net.sf.autodao.Limit;
import net.sf.autodao.Named;
import ru.simplex_software.arbat_baza.model.LogMessage;

import java.time.Instant;
import java.util.List;


public interface LogMessageDAO extends Dao<LogMessage,Long>{
    @Finder(query="from LogMessage order by creationDate desc")
    public List<LogMessage> getLastMessage(@Limit int limit);
}
