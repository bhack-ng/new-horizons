package ru.simplex_software.arbat_baza.model;

/**
 * Состояние строения.
 */
public enum BuildingStatus implements CianIdContainer{
    SATISFACTORY(1),//    1 – удовлетворительное
    GOOD(2),//    2 – хорошее
    EXCELLENY(3);//    3 - отличное
    private final int cianId;

    public static BuildingStatus findByCianId(Integer id){
        for(BuildingStatus bs:BuildingStatus.values()){
            if(bs.getCianId()==id){
                return bs;
            }
        }
        return null;
    }

    BuildingStatus(int cianId) {
        this.cianId = cianId;
    }

    @Override
    public Integer getCianId() {
        return cianId;
    }
}
