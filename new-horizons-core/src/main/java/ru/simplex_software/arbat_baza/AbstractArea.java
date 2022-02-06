package ru.simplex_software.arbat_baza;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplex_software.zkutils.entity.LongIdPersistentEntity;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * Общая информация о помещениях площади объекта.
 */
@Entity @Inheritance(strategy = InheritanceType.JOINED)
public class AbstractArea extends LongIdPersistentEntity {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractArea.class);
    /**общая.*/
    private Double total;
    /**по комнатам (текстовое поле). Например "20+15"*/
    private String rooms;
    private Integer roomsCount; //количество комнат

    public String getRooms() {
        return rooms;
    }

    public void setRooms(String rooms) {
        this.rooms = rooms;
    }

    public Integer getRoomsCount() {
        return roomsCount;
    }

    public void setRoomsCount(Integer roomsCount) {
        this.roomsCount = roomsCount;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
