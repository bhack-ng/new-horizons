package ru.simplex_software.arbat_baza.model.live;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplex_software.arbat_baza.model.AbstractOptions;

import javax.persistence.Entity;

/**
 * LiveLease options.
 */
@Entity (name= "liveoptions")
public class LiveLeaseOptions extends AbstractOptions{
    private static final Logger LOG = LoggerFactory.getLogger(LiveLeaseOptions.class);
    /**Наличие мебели.*/
    private Boolean mebel;
    /**наличие ванны.*/
    private  Boolean haveBath;
    /**Наличие мебели.*/
    private Boolean mebelInKitchen;
    /**Наличие  гардероб.*/
    private Boolean cloakroom;
    /**Паркинг дома.*/
    private Boolean parking;
    /**Охрана.*/
    private Boolean security;
    /**Огороженная территория.*/
    private Boolean fencedTerritory;
    /**Консьерж.*/
    private Boolean concierge;
    /**Домашние животные.*/
    private Boolean pets;
    /**Дети.*/
    private Boolean children;
    /**Курение.*/
    private Boolean smoke;
    /**наличие телефона.*/
    private Boolean phone;
    /**Наличие стир. машины.*/
    private Boolean washMashine;
    /**Наличие холодильника.*/
    private Boolean refrigerator;
    /**наличие ТВ.*/
    private Boolean tv;

    public Boolean getMebel() {
        return mebel;
    }

    public void setMebel(Boolean mebel) {
        this.mebel = mebel;
    }

    public Boolean getCloakroom() {
        return cloakroom;
    }

    public void setCloakroom(Boolean cloakroom) {
        this.cloakroom = cloakroom;
    }

    public Boolean getParking() {
        return parking;
    }

    public void setParking(Boolean parking) {
        this.parking = parking;
    }

    public Boolean getSecurity() {
        return security;
    }

    public void setSecurity(Boolean security) {
        this.security = security;
    }

    public Boolean getFencedTerritory() {
        return fencedTerritory;
    }

    public void setFencedTerritory(Boolean fencedTerritory) {
        this.fencedTerritory = fencedTerritory;
    }

    public Boolean getConcierge() {
        return concierge;
    }

    public void setConcierge(Boolean concierge) {
        this.concierge = concierge;
    }

    public Boolean getChildren() {
        return children;
    }

    public void setChildren(Boolean children) {
        this.children = children;
    }

    public Boolean getPets() {
        return pets;
    }

    public void setPets(Boolean pets) {
        this.pets = pets;
    }

    public Boolean getSmoke() {
        return smoke;
    }

    public void setSmoke(Boolean smoke) {
        this.smoke = smoke;
    }

    public Boolean getMebelInKitchen() {
        return mebelInKitchen;
    }

    public void setMebelInKitchen(Boolean mebelInKitchen) {
        this.mebelInKitchen = mebelInKitchen;
    }

    public Boolean getPhone() {
        return phone;
    }

    public void setPhone(Boolean phone) {
        this.phone = phone;
    }

    public Boolean getRefrigerator() {
        return refrigerator;
    }

    public void setRefrigerator(Boolean refrigerator) {
        this.refrigerator = refrigerator;
    }

    public Boolean getTv() {
        return tv;
    }

    public void setTv(Boolean tv) {
        this.tv = tv;
    }

    public Boolean getWashMashine() {
        return washMashine;
    }

    public void setWashMashine(Boolean washMashine) {
        this.washMashine = washMashine;
    }

    public Boolean getHaveBath() {
        return haveBath;
    }

    public void setHaveBath(Boolean haveBath) {
        this.haveBath = haveBath;
    }

}
