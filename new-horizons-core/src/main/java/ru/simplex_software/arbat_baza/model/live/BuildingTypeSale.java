package ru.simplex_software.arbat_baza.model.live;

import ru.simplex_software.arbat_baza.model.CianIdContainer;

/**
 * Новостройка или вторичка.
 */

public enum BuildingTypeSale implements CianIdContainer{
    /**Новостройка.*/
    NEW_BUILDING(2),
    /**Вторичка.*/
    SECONDHAND(1);

    private final int cianId;

    BuildingTypeSale(int cianId) {
        this.cianId = cianId;
    }

    @Override
    public Object getCianId() {
        return cianId;
    }
}
