package ru.simplex_software.arbat_baza.viewmodel;

import org.springframework.util.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;
import ru.simplex_software.arbat_baza.dao.JobPositionDAO;
import ru.simplex_software.arbat_baza.model.clients.Client;
import ru.simplex_software.arbat_baza.model.clients.JobPosition;
import ru.simplex_software.arbat_baza.model.clients.JuridicalClient;
import ru.simplex_software.arbat_baza.model.clients.NaturalClient;
import ru.simplex_software.ord.FormJuridicalClientValidator;

import java.util.ArrayList;
import java.util.List;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ManageJuridicalClientVM extends ManageClient<JuridicalClient> {

    @WireVariable
    private JobPositionDAO jobPositionDAO;

    private List<Client> employees;
    private NaturalClient employee;
    private String position;
    private String jobPositionError;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window window) {
        super.afterCompose(window);

        String clientId = Executions.getCurrent().getParameter("clientId");
        this.client = (clientId.isEmpty()) ? new JuridicalClient() :
                (JuridicalClient) clientDAO.get(Long.parseLong(clientId));

        this.validator = new FormJuridicalClientValidator(clientDAO, client);
        this.employees = new ArrayList<>();
    }

    /**
     * Установка короткого названия клиента по умолчанию.
     */
    @Command
    public void changeShortName(@BindingParam("event") InputEvent event) {
        if (client.getShortName() == null || client.getShortName().isEmpty()) {
            client.setName(event.getValue());
            client.setShortName(event.getValue());
            BindUtils.postNotifyChange(null, null, this, "client");
        }
    }

    /**
     * Ввод имени сотрудника.
     */
    @Command
    public void changing(@BindingParam("event") InputEvent event) {
        if (event.getValue().length() > 2) {
            employees = clientDAO.findNaturalByNameLike(event.getValue());
            BindUtils.postNotifyChange(null, null, this, "employees");
        }
    }

    /**
     * Добавление сотрудника.
     */
    @Command
    @NotifyChange("jobPositionError")
    public void addEmployee() {

        // Валидация.
        jobPositionError = "";
        if (employee == null) {
            jobPositionError = "Нужно выбрать сотрудника.";
            return;
        }
        if (position == null || !StringUtils.hasLength(position.trim())) {
            jobPositionError = "Нужно указать должность.";
            return;
        }
        if (jobPositionDAO.hasDuplicate(employee, client, position)) {
            jobPositionError = employee.getName() + " : " + position + " уже зарегистрирован.";
            return;
        }

        // Добавление позиции.
        JobPosition jobPosition = new JobPosition();
        jobPosition.setEmployee(employee);
        jobPosition.setName(position.trim());
        jobPosition.setCompany(client);
        jobPositionDAO.saveOrUpdate(jobPosition);
        client.addJobPosition(jobPosition);
        BindUtils.postNotifyChange(null, null, this, "jobPositions");
        selectTab(3);
        Clients.showNotification("Должность добавлена.", Clients.NOTIFICATION_TYPE_INFO,
                null, "middle_left", 2000);
    }

    public List<Client> getEmployees() {
        return employees;
    }

    public List<JobPosition> getJobPositions() {
        return client.getJobPositions();
    }

    public NaturalClient getEmployee() {
        return employee;
    }

    public void setEmployee(NaturalClient employee) {
        this.employee = employee;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getJobPositionError() {
        return jobPositionError;
    }
}
