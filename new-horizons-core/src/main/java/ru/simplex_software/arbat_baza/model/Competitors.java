package ru.simplex_software.arbat_baza.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplex_software.zkutils.entity.LongIdPersistentEntity;

import javax.persistence.Entity;

/**
 * Данные конкурентов.
 */
@Entity
public class Competitors extends LongIdPersistentEntity{
    private static final Logger LOG = LoggerFactory.getLogger(Competitors.class);
    private String bl_f;
    private String int_f;
    private String ch_f;
    private String r_f;

    public String getBl_f() {
        return bl_f;
    }

    public void setBl_f(String bl_f) {
        this.bl_f = bl_f;
    }

    public String getCh_f() {
        return ch_f;
    }

    public void setCh_f(String ch_f) {
        this.ch_f = ch_f;
    }

    public String getInt_f() {
        return int_f;
    }

    public void setInt_f(String int_f) {
        this.int_f = int_f;
    }

    public String getR_f() {
        return r_f;
    }

    public void setR_f(String r_f) {
        this.r_f = r_f;
    }
}
