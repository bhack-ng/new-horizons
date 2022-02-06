package ru.simplex_software.arbat_baza.model.stead;

import ru.simplex_software.arbat_baza.model.CianIdContainer;

/**
 * .
 */
public enum ObjectType implements CianIdContainer {
    /** Учаски поселений (ИЖС).*/
    SETTLEMENTS(1),
    /**Участки сельхозназначения .*/
    AGRICULTURAL(2),
    /**Участки промышленногог назначения .*/
    PROM_DESTINATION(3);

    private final int cianId;

    ObjectType(int cianId){
        this.cianId = cianId;
    }

    @Override
    public Integer getCianId() {
        return null;
    }
}
