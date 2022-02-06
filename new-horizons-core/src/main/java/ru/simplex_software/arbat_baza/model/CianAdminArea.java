package ru.simplex_software.arbat_baza.model;

import ru.simplex_software.zkutils.entity.LongIdPersistentEntity;

import javax.persistence.Entity;

/**
 * Регион в Cian http://www.cian.ru/admin_areas.php
 */
@Entity
public class CianAdminArea extends LongIdPersistentEntity{
    /**
     * region name taken from XML
     */
    private String name;
    /**
     * Identifier from fias directory
     */
    private String fiasAoguid;
    /**
     * it is the Cian identifier from id attribute
     */
    private int cianId;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFiasAoguid() {
        return fiasAoguid;
    }

    public void setFiasAoguid(String fiasAoguid) {
        this.fiasAoguid = fiasAoguid;
    }

    public int getCianId(){
        return cianId;
    }

    public void setCianId(int cianId){
        this.cianId=cianId;
    }
}
