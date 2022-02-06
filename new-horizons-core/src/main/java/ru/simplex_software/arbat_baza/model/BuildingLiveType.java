package ru.simplex_software.arbat_baza.model;

/**
 *   Тип здания,
 *   1 – нежилое
 *   2 – жилое.
 */
public enum BuildingLiveType  implements CianIdContainer{
    NOT_LIVING(1),//    1 – нежилое
    LIVING(2);//    2 – жилое.

    private final int cianId;


    BuildingLiveType(int cianId) {
        this.cianId = cianId;
    }

    @Override
    public Integer getCianId() {
        return cianId;
    }
}
