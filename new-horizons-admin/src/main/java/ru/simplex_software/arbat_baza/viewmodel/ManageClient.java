package ru.simplex_software.arbat_baza.viewmodel;

import org.joda.time.DateTime;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.Converter;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Window;
import ru.simplex_software.arbat_baza.AuthService;
import ru.simplex_software.arbat_baza.EnumConverter;
import ru.simplex_software.arbat_baza.dao.ClientDAO;
import ru.simplex_software.arbat_baza.dao.ClientTaskDAO;
import ru.simplex_software.arbat_baza.dao.DbFileDao;
import ru.simplex_software.arbat_baza.dao.RealtyObjectDAO;
import ru.simplex_software.arbat_baza.model.DbFile;
import ru.simplex_software.arbat_baza.model.DbFileContent;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.arbat_baza.model.clients.AdvertisingCampaignType;
import ru.simplex_software.arbat_baza.model.clients.Client;
import ru.simplex_software.arbat_baza.model.clients.ClientTask;
import ru.simplex_software.arbat_baza.model.clients.ClientTaskStatus;
import ru.simplex_software.ord.utils.RealtyObjectTypeUtils;
import ru.simplex_software.zkutils.EnumPropertiesConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Управление клиентом.
 */
@VariableResolver(DelegatingVariableResolver.class)
public class ManageClient<T extends Client> {

    @WireVariable
    protected RealtyObjectDAO realtyObjectDAO;

    @WireVariable
    protected AuthService authService;

    @WireVariable
    protected ClientTaskDAO clientTaskDAO;

    @WireVariable
    protected ClientDAO clientDAO;

    @WireVariable
    protected DbFileDao dbFileDao;

    protected T client;
    protected Window window;
    protected boolean viewMode;
    protected Validator validator;
    protected int selectedTabIndex;
    protected long realtyObjectId;
    protected String realtyObjectError;
    protected Converter converter;

    /**
     * Сохранение клиента.
     */
    @Command
    public void saveClient(@ContextParam(ContextType.VIEW) Component component) {
        // Удаление пробелов с телефонов.
        if (client.getPhone() != null) {
            client.setPhone(client.getPhone().trim());
        }
        if (client.getMobilePhone() != null) {
            client.setMobilePhone(client.getMobilePhone().trim());
        }

        clientDAO.merge(client);
        Executions.sendRedirect("/owners.zul");
        component.detach();
    }

    public void afterCompose(Window window) {
        this.viewMode = Boolean.parseBoolean(Executions.getCurrent().getParameter("viewMode"));
        this.window = window;
        converter = new EnumConverter("ru.simplex_software.arbat_baza.model.clients");
        selectTab(0);
    }

    /**
     * Добавление задачи.
     */
    @Command
    public void addTask() {
        ClientTask task = new ClientTask();
        task.setExecutionDatetime(new DateTime().plusDays(1).toDate());
        task.setClient(client);
        task.setCreator(authService.getLogginedAgent());
        task.setExecutor(authService.getLogginedAgent());
        task.setStatus(ClientTaskStatus.ACTUAL);

        Map<String, ClientTask> map = new HashMap<>();
        map.put("task", task);

        Component component = Executions.getCurrent().createComponents("/manageTask.zul", window, map);

        component.addEventListener("onChange", e -> {
            BindUtils.postNotifyChange(null, null, this, "tasks");
            selectTab(0);
        });
    }

    /**
     * Изменение задачи.
     */
    @Command
    public void editTask(@BindingParam("task") ClientTask task) {
        Map<String, Object> map = new HashMap<>();
        map.put("client", client);
        map.put("task", task);

        Component component = Executions.getCurrent().createComponents("/manageTask.zul", window, map);

        component.addEventListener("onChange", e -> BindUtils.postNotifyChange(null, null, this, "tasks"));
    }

    /**
     * Удаление задачи.
     */
    @Command
    @NotifyChange("tasks")
    public void deleteTask(@BindingParam("taskId") long taskId) {
        ClientTask task = clientTaskDAO.get(taskId);
        client.getTasks().remove(task);
        clientTaskDAO.delete(task);
    }

    /**
     * Добавление коммерческой недвижимости.
     */
    @Command
    @NotifyChange("realtyObjectError")
    public void addRealtyObject() {

        // Валидация.
        realtyObjectError = "";
        if (realtyObjectId < 0) {
            realtyObjectError = "ID не должно быть меньше нуля.";
            return;
        }

        RealtyObject realtyObject = realtyObjectDAO.get(realtyObjectId);
        if (realtyObject == null) {
            realtyObjectError = "Объект недвижимости не найден.";
            return;
        }

        if (realtyObject.getRenter() != null) {
            realtyObjectError = "Объект недвижимости уже назначен.";
            return;
        }

        // Добавление недвижимости.
        realtyObject.setRenter(client);
        client.getRealtyObjects().add(realtyObject);
        selectTab(1);
        realtyObjectId = 0L;
        BindUtils.postNotifyChange(null, null, this, "realtyObjects");
        BindUtils.postNotifyChange(null, null, this, "realtyObjectId");
        Clients.showNotification("Объект недвижимости добавлен.", Clients.NOTIFICATION_TYPE_INFO,
                null, "middle_left", 2000);
    }

    /**
     * Загрузка файла.
     */
    @Command
    public void uploadFile(@ContextParam(ContextType.BIND_CONTEXT) BindContext context) {

        UploadEvent uploadEvent = (UploadEvent) context.getTriggerEvent();
        Media media = uploadEvent.getMedia();
        byte[] data = (media.getContentType().equals("text/plain")) ?
                media.getStringData().getBytes() : media.getByteData();

        // Загрузка.
        DbFileContent content = new DbFileContent();
        content.setData(data);

        DbFile file = new DbFile();
        file.setContentType(media.getContentType());
        file.setLength(data.length);
        file.setContent(content);
        dbFileDao.saveOrUpdate(file);

        client.getFiles().add(file);
        clientDAO.saveOrUpdate(client);

        selectTab(2);
        BindUtils.postNotifyChange(null, null, this, "files");
        Clients.showNotification("Файл загружен.", Clients.NOTIFICATION_TYPE_INFO,
                null, "middle_left", 2000);
    }

    /**
     * Закрытие окна.
     */
    @Command
    public void close(@ContextParam(ContextType.VIEW) Component component) {
        Executions.sendRedirect("/owners.zul");
        component.detach();
    }

    public T getClient() {
        return client;
    }

    public void setClient(T client) {
        this.client = client;
    }

    /**
     * Получение списка задач клиента.
     */
    public List<ClientTask> getTasks() {
        if (client.getId() == null) return new ArrayList<>();
        return clientTaskDAO.findByClient(client);
    }

    /**
     * Изменение вкладки.
     */
    protected void selectTab(int index) {
        selectedTabIndex = index;
        BindUtils.postNotifyChange(null, null, this, "isTabSelected");
    }

    public Converter getConverter() {
        return converter;
    }

    public Validator getValidator() {
        return validator;
    }

    public boolean getViewMode() {
        return viewMode;
    }

    public long getRealtyObjectId() {
        return realtyObjectId;
    }

    public void setRealtyObjectId(long realtyObjectId) {
        this.realtyObjectId = realtyObjectId;
    }

    public String getRealtyObjectError() {
        return realtyObjectError;
    }

    public List<RealtyObject> getRealtyObjects() {
        return client.getRealtyObjects();
    }

    public String getRealtyObjectType(String rawType) {
        return RealtyObjectTypeUtils.getRealtyObjectType(rawType);
    }

    public String getOfferType(String rawType) {
        return RealtyObjectTypeUtils.getOfferType(rawType);
    }

    public boolean isTabSelected(int index) {
        return index == selectedTabIndex;
    }

    public List<DbFile> getFiles() {
        return client.getFiles();
    }

    public AdvertisingCampaignType[] getAdvertisingCampaignTypes() {
        return AdvertisingCampaignType.values();
    }
}
