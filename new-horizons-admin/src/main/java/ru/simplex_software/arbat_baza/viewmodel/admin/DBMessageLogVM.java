package ru.simplex_software.arbat_baza.viewmodel.admin;

import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import ru.simplex_software.arbat_baza.dao.LogMessageDAO;
import ru.simplex_software.arbat_baza.model.LogMessage;

import javax.annotation.Resource;
import java.util.List;
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class DBMessageLogVM {
    @WireVariable
    private List<LogMessage> logMessage;
    @WireVariable
    private LogMessageDAO logMessageDAO;
    private int amountLogMessage=20;


    @AfterCompose
    public void afterCompose(){
        logMessage = logMessageDAO.getLastMessage(amountLogMessage);
    }

    @Command @NotifyChange("*")
    public void update(){
        logMessage = logMessageDAO.getLastMessage(amountLogMessage);
    }

    public List<LogMessage> getLogMessage() {
        return logMessage;
    }

    /*
    @Command @NotifyChange(".")
    public void setAmountLogMessage(int amountLogMessage) {
        this.amountLogMessage = amountLogMessage;
        update();
    }

    */
}
