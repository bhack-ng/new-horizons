package ru.simplex_software.arbat_baza.model.clients;

import ru.simplex_software.arbat_baza.model.Agent;
import ru.simplex_software.zkutils.entity.LongIdPersistentEntity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.Date;

/**
 * Задача, связанная с клиентом.
 */
@Entity
public class ClientTask extends LongIdPersistentEntity {
    /**
     * Дата и время выполнения задания
     */
    private Date executionDatetime;

    /**
     * Создатель задачи
     */
    @OneToOne
    private Agent creator;

    /**
     * Исполнитель задачи
     */
    @OneToOne
    private Agent executor;

    /**
     * Клиент, связанный с задачей
     */
    @ManyToOne
    private Client client;

    /**
     * Описание задачи
     */
    private String description;

    /**
     * Статус задачи.
     */
    @Enumerated(EnumType.STRING)
    private ClientTaskStatus status;

    public ClientTask() {
    }

    public Date getExecutionDatetime() {
        return executionDatetime;
    }

    public void setExecutionDatetime(Date executionDatetime) {
        this.executionDatetime = executionDatetime;
    }

    public Agent getCreator() {
        return creator;
    }

    public void setCreator(Agent creator) {
        this.creator = creator;
    }

    public Agent getExecutor() {
        return executor;
    }

    public void setExecutor(Agent executor) {
        this.executor = executor;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ClientTaskStatus getStatus() {
        return status;
    }

    public void setStatus(ClientTaskStatus status) {
        this.status = status;
    }
}
