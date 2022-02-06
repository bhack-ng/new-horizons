package ru.simplex_software.arbat_baza.model;

/**
 *   enter 	вход в здание.
 *   1 – свободный
 *   2 – пропускная система.
 */
public enum BuildingEntrance implements CianIdContainer{
    /**
     * Вход свободный.
     */
    FREE(1),
    /**
     * Пропускная система.
     */
    PASS_SYSTEM(2);

    private final int cianId;

    BuildingEntrance(int cianId) {
        this.cianId = cianId;
    }

    @Override
    public Integer getCianId() {
        return cianId;
    }
}
