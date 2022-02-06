package ru.simplex_software.arbat_baza.viewmodel;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.Converter;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;
import ru.simplex_software.arbat_baza.AuthService;
import ru.simplex_software.arbat_baza.EnumConverter;
import ru.simplex_software.arbat_baza.dao.AgentDAO;
import ru.simplex_software.arbat_baza.dao.ClientDAO;
import ru.simplex_software.arbat_baza.dao.ClientTaskDAO;
import ru.simplex_software.arbat_baza.model.Agent;
import ru.simplex_software.arbat_baza.model.clients.Client;
import ru.simplex_software.arbat_baza.model.clients.ClientTask;
import ru.simplex_software.arbat_baza.model.clients.ClientTaskStatus;
import ru.simplex_software.ord.FormClientTasksValidator;

import java.util.ArrayList;
import java.util.List;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ManageTaskVM {

    @WireVariable
    private AgentDAO agentDAO;

    @WireVariable
    private ClientDAO clientDAO;

    @WireVariable
    private ClientTaskDAO clientTaskDAO;

    @WireVariable
    private AuthService authService;

    private Window window;
    private ClientTask task;
    private Validator validator;
    private List<Client> clients;
    private Converter statusConverter;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window window,
                             @ExecutionArgParam("task") ClientTask task) {

        this.validator = new FormClientTasksValidator();
        this.window = window;
        this.task = task;
        this.statusConverter = new EnumConverter("ru.simplex_software.arbat_baza.model.clients");

        clients = new ArrayList<>();
        clients.add(task.getClient());
    }

    /**
     * Добавление задачи.
     */
    @Command
    public void submit(@ContextParam(ContextType.VIEW) Component component) {
        clientTaskDAO.merge(task);
        Events.postEvent("onChange", window, null);
        component.detach();
    }

    /**
     * Ввод текста для поиска клиентов.
     * Поиск выполняется если введено от 3 символов.
     */
    @Command
    public void changing(@BindingParam("event") InputEvent event) {
        if (event.getValue().length() > 2) {
            clients = clientDAO.findByNameLike(event.getValue());
            BindUtils.postNotifyChange(null, null, this, "clients");
        }
    }

    /**
     * Закрытие окна.
     */
    @Command
    public void close(@ContextParam(ContextType.VIEW) Component component) {
        component.detach();
    }

    public List<Agent> getAgents() {
        return agentDAO.findAll();
    }

    public ClientTask getTask() {
        return task;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void setTask(ClientTask task) {
        this.task = task;
    }

    public Validator getValidator() {
        return validator;
    }

    public ClientTaskStatus[] getStatuses() {
        return ClientTaskStatus.values();
    }

    public Converter getStatusConverter() {
        return statusConverter;
    }
}
