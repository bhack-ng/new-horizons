package ru.simplex_software.arbat_baza.model.price;

import ru.simplex_software.arbat_baza.model.CianIdContainer;

/**
 * .
 */
public enum Period implements CianIdContainer {
    MONTH("month"), //В месяц
    M2_PER_YEAR("year");//За кв.м/год
    private final String cianId;

    Period(String cianId) {
        this.cianId = cianId;
    }

    @Override
    public String getCianId() {
        return cianId;
    }
}
