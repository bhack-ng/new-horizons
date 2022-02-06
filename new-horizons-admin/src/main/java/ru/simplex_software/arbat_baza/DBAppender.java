package ru.simplex_software.arbat_baza;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.simplex_software.arbat_baza.dao.LogMessageDAO;
import ru.simplex_software.arbat_baza.model.LogMessage;

import javax.annotation.Resource;
import java.time.Instant;

/**
 * save logging messages to DB
 */
public class DBAppender {
    @Resource
    private LogMessageDAO logMessageDAO;


    /**
     * this method wright message with information about  to DB
     * @param massage
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void info(String massage){
        LogMessage logMessage = new LogMessage();
        logMessage.setCreationDate(Instant.now());
        logMessage.setMessage(massage);
        logMessage.setPriority("INFO");
        logMessageDAO.saveOrUpdate(logMessage);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void error(String massage){
        LogMessage logMessage = new LogMessage();
        logMessage.setCreationDate(Instant.now());
        logMessage.setMessage(massage);
        logMessage.setPriority("ERROR");
        logMessageDAO.saveOrUpdate(logMessage);
    }




}
