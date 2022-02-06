package ru.simplex_software.arbat_baza.model;

/**
 * Тип дома.
 */
public enum BuildingType implements CianIdContainer {
    PANEL(1),//панельный
    BRICK(2),//кирпичный
    MONOLITE(3),//монолитный
    BRICK_MONOLITE(4),// кирпично-монолитный
    BLOCK(5),//блочный
    WOOD(6),//деревянный
    STALIN(7),//«сталинский»
    OLD_FOND(9);//старый фонд (для Санкт-Петербурга)

    private final int cianId;

    BuildingType(int id) {
        this.cianId = id;
    }

    @Override
    public Integer getCianId() {
        return cianId;
    }
    public static BuildingType findByCianId(Integer id){
        for(BuildingType bt:BuildingType.values()){
            if(bt.getCianId()==id){
                return bt;
            }
        }
        return null;
    }
}
