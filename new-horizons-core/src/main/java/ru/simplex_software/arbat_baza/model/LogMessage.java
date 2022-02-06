package ru.simplex_software.arbat_baza.model;

import ru.simplex_software.zkutils.entity.LongIdPersistentEntity;

import javax.persistence.Entity;
import java.time.Instant;

/**
 * this class for writing logs to DB
 */
@Entity
public class LogMessage extends LongIdPersistentEntity {
    /**
     * Time creation log
     */
    private Instant creationDate;

    /**
     * Log message
     */
    private String message;
    /**
     * This is priority of Log message
     */
    private String priority;

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
