package ru.simplex_software.arbat_baza.model.fias;

import net.sf.autodao.PersistentEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Типы адресных объектов. Из таблицы SOCRBASE.
 */
@Entity
public class FiasSocr implements PersistentEntity<String>{
    @Id
    private String id;
    private String socrName;
    private String fullName;
    private int level;

    @Override
    public String getPrimaryKey() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getSocrName() {
        return socrName;
    }

    public void setSocrName(String socrName) {
        this.socrName = socrName;
    }
}
