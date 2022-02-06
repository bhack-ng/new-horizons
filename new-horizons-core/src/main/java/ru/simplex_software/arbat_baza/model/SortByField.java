package ru.simplex_software.arbat_baza.model;

/**
 * Сортировка объектов недвижимости.
 */

public enum SortByField {
    ID_UP ("ro.id asc"),ID_DOWN("ro.id desc"),
    CALL_DATE_UP("ro.nextCall asc"),CALL_DATE_DOWN("ro.nextCall desc"),
    ROOM_COUNT_UP("aa.roomsCount asc"),ROOM_COUNT_DOWN("aa.roomsCount desc"),
    CHANGED_UP("ro.changed asc"),CHANGED_DOWN("ro.changed desc"),

    PRICE_UP("sp.value asc"), PRICE_DOWN("sp.value desc")
    ;
    private String hqlStr;

    SortByField(String hqlStr) {
        this.hqlStr = hqlStr;
    }

    public String getHqlStr() {
        return hqlStr;
    }
}
