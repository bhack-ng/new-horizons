package ru.simplex_software.arbat_baza.model;

import ru.simplex_software.zkutils.entity.LongIdPersistentEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Контакт (телефон) собственника.
 */
@Entity
public class ContactsOfOwner extends LongIdPersistentEntity {
    private String name;
    private String phone;
    @ManyToOne()
    private RealtyObject realtyObject;

    public  boolean equalsName(String name){
        if ((this.name!=null)&&(this.name.equals(name))) return true;
        else return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public RealtyObject getRealtyObject() {
        return realtyObject;
    }

    public void setRealtyObject(RealtyObject realtyObject) {
        this.realtyObject = realtyObject;
    }
}
