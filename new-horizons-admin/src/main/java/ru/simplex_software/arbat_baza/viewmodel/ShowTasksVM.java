package ru.simplex_software.arbat_baza.viewmodel;

import org.joda.time.DateTime;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;
import ru.simplex_software.arbat_baza.AuthService;
import ru.simplex_software.arbat_baza.dao.ClientTaskDAO;
import ru.simplex_software.arbat_baza.model.Agent;
import ru.simplex_software.arbat_baza.model.clients.ClientTask;
import ru.simplex_software.arbat_baza.model.clients.ClientTaskStatus;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ShowTasksVM {

    @WireVariable
    private ClientTaskDAO clientTaskDAO;

    @WireVariable
    private AuthService authService;

    private AbstractPageableListModel<ClientTask> todayTasksListModel;
    private AbstractPageableListModel<ClientTask> myTasksListModel;
    private AbstractPageableListModel<ClientTask> createdTasksListModel;

    private Agent user;
    private Window window;
    private Date tomorrow;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window window) {
        this.tomorrow = Date.from(LocalDateTime.now().plusDays(1).with(LocalTime.MIN)
                .atZone(ZoneId.systemDefault()).toInstant());
        this.window = window;
        this.user = authService.getLogginedAgent();

        // Инициализация моделей.
        todayTasksListModel = new AbstractPageableListModel<ClientTask>() {
            @Override
            ClientTask getElementById(Long id) {
                return clientTaskDAO.get(id);
            }

            @Override
            List<Long> getIds(int cacheSize, int index) {
                return clientTaskDAO.findIdsByExecutorToday(user, tomorrow, cacheSize, index);
            }

            @Override
            int getElementsCount() {
                return (int) clientTaskDAO.countByExecutorToday(user, tomorrow);
            }
        };
        myTasksListModel = new AbstractPageableListModel<ClientTask>() {
            @Override
            ClientTask getElementById(Long id) {
                return clientTaskDAO.get(id);
            }

            @Override
            List<Long> getIds(int cacheSize, int index) {
                return clientTaskDAO.findIdsByExecutor(user, cacheSize, index);
            }

            @Override
            int getElementsCount() {
                return (int) clientTaskDAO.countByExecutor(user);
            }
        };
        createdTasksListModel = new AbstractPageableListModel<ClientTask>() {
            @Override
            ClientTask getElementById(Long id) {
                return clientTaskDAO.get(id);
            }

            @Override
            List<Long> getIds(int cacheSize, int index) {
                return clientTaskDAO.findIdsByCreator(user, cacheSize, index);
            }

            @Override
            int getElementsCount() {
                return (int) clientTaskDAO.countByCreator(user);
            }
        };
    }

    /**
     * Добавление задачи.
     */
    @Command
    public void addTask() {
        ClientTask task = new ClientTask();
        task.setExecutionDatetime(new DateTime().plusDays(1).toDate());
        task.setCreator(authService.getLogginedAgent());
        task.setExecutor(authService.getLogginedAgent());
        task.setStatus(ClientTaskStatus.ACTUAL);

        Map<String, ClientTask> map = new HashMap<>();
        map.put("task", task);

        Component component = Executions.getCurrent().createComponents("/manageTask.zul", window, map);

        // Обновление моделей.
        component.addEventListener("onChange", e -> {
            todayTasksListModel.refreshModel();
            myTasksListModel.refreshModel();
            createdTasksListModel.refreshModel();

            BindUtils.postNotifyChange(null, null, this, "todayTasksListModel");
            BindUtils.postNotifyChange(null, null, this, "myTasksListModel");
            BindUtils.postNotifyChange(null, null, this, "createdTasksListModel");
        });
    }

    /**
     * Изменение задачи.
     */
    @Command
    public void editTask(@BindingParam("task") ClientTask task) {
        Map<String, Object> map = new HashMap<>();
        map.put("task", task);

        Component component = Executions.getCurrent().createComponents("/manageTask.zul", window, map);

        // Обновление моделей.
        component.addEventListener("onChange", e -> {
            todayTasksListModel.refreshModel();
            myTasksListModel.refreshModel();
            createdTasksListModel.refreshModel();

            BindUtils.postNotifyChange(null, null, this, "todayTasksListModel");
            BindUtils.postNotifyChange(null, null, this, "myTasksListModel");
            BindUtils.postNotifyChange(null, null, this, "createdTasksListModel");
        });
    }

    /**
     * Удаление задачи.
     */
    @Command
    @NotifyChange({"todayTasksListModel", "myTasksListModel", "createdTasksListModel"})
    public void deleteTask(@BindingParam("taskId") long taskId) {
        ClientTask task = clientTaskDAO.get(taskId);
        if (task.getClient() != null) {
            task.getClient().getTasks().remove(task);
        }
        clientTaskDAO.delete(task);

        // Обновление моделей.
        todayTasksListModel.refreshModel();
        myTasksListModel.refreshModel();
        createdTasksListModel.refreshModel();
    }

    public AbstractPageableListModel getTodayTasksListModel() {
        return todayTasksListModel;
    }

    public AbstractPageableListModel getMyTasksListModel() {
        return myTasksListModel;
    }

    public AbstractPageableListModel getCreatedTasksListModel() {
        return createdTasksListModel;
    }
}
