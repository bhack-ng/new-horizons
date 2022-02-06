package ru.simplex_software.arbat_baza.model.commerce;

import ru.simplex_software.arbat_baza.model.AbstractOptions;
import ru.simplex_software.arbat_baza.model.CianIdContainer;

import javax.persistence.Entity;

/**
 * Дополнительные параметры.
 */
@Entity(name = "commerceoptions")
public class CommerceOptions extends AbstractOptions{

    /**
     * Наличие мебели.
     */
    public enum MebelExistance implements CianIdContainer {
        /** Нет.*/
        NO(0),
        /** Есть.*/
        YES(1),
        /** По желанию клиента.*/
        CLIENT_WISH(2);
        /** Value to export to cian.*/
        private final int cianId;

        MebelExistance(int cianId) {
            this.cianId = cianId;
        }

        @Override
        public Integer getCianId() {
            return cianId;
        }
    }


    /**
     * Количество телефонных линий.
     */
    private Integer phones;

    /**
     * Возможность проведения дополнительных телефонных линий.
     * При экспорте в циан:
     *  0 – нет
     *  1 – да
     */
    private Boolean add_phones;



//    	наличие мебели
    private MebelExistance mebel= MebelExistance.CLIENT_WISH;

    /**
     * Электричество.
     * При экспорте в циан:
     *  yes – да
     *  no – нет
     */
    private Boolean elect;

    /**
     * 	Отопление.
     * При экспорте в циан:
     * 	yes – да
     * 	no – нет
     */
    private Boolean heat;

    /**
     * Канализация.
     * При экспорте в циан:
     * yes – да
     * no – нет
     */
    private Boolean canal;

    /**
     *	Газ.
     *  При экспорте в циан:
     *  yes – да
     *  no – нет

     */
    private Boolean gas;

    /**
     * Водоснабжение.
     * При экспорте в циан:
     * yes – да
     * no – нет
     */
    private Boolean water;

    /**
     * Лифт.
     * При экспорте в циан:
     * yes – да
     * no – нет
     */
    private Boolean lift;

    /**
     * Парковка.
     * При экспорте в циан:
     * yes – да
     * no – нет
     */
    private Boolean parking;
    /**
     * Охрана.
     * При экспорте в циан:
     * yes – да
     * no – нет
     */
    private Boolean security;
    /**
     *  Интернет.
     *  При экспорте в циан:
     *  yes – да
     *  no – нет
     */
    private Boolean internet;

    public Integer getPhones() {
        return phones;
    }

    public void setPhones(Integer phones) {
        this.phones = phones;
    }

    public Boolean getAdd_phones() {
        return add_phones;
    }

    public void setAdd_phones(Boolean add_phones) {
        this.add_phones = add_phones;
    }

    public MebelExistance getMebel() {
        return mebel;
    }

    public void setMebel(MebelExistance mebel) {
        this.mebel = mebel;
    }

    public Boolean getElect() {
        return elect;
    }

    public void setElect(Boolean elect) {
        this.elect = elect;
    }

    public Boolean getHeat() {
        return heat;
    }

    public void setHeat(Boolean heat) {
        this.heat = heat;
    }

    public Boolean getCanal() {
        return canal;
    }

    public void setCanal(Boolean canal) {
        this.canal = canal;
    }

    public Boolean getGas() {
        return gas;
    }

    public void setGas(Boolean gas) {
        this.gas = gas;
    }

    public Boolean getWater() {
        return water;
    }

    public void setWater(Boolean water) {
        this.water = water;
    }

    public Boolean getLift() {
        return lift;
    }

    public void setLift(Boolean lift) {
        this.lift = lift;
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

    public Boolean getInternet() {
        return internet;
    }

    public void setInternet(Boolean internet) {
        this.internet = internet;
    }
}
