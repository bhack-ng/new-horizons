package ru.simplex_software.arbat_baza.model.live;

import ru.simplex_software.arbat_baza.model.CianIdContainer;

/**
 * Количество комнат в объекте недвижимости сдающегося в аренду.
 */

public enum RoomsNumRent implements CianIdContainer{
    /**
     * Сдаётся комната.
     */
    ROOM_ONLY(0),
    /**Однокомнатная квартира.*/
    ROOMS_1(1),
    /** 2-х комнатная квартира.*/
    ROOMS_2(2),
    /** 3-х комнатная квартира.*/
    ROOMS_3(3),
    /** 4-х комнатная квартира.*/
    ROOMS_4(4),
    /** 5-ти комнатная квартира.*/
    ROOMS_5(5),
    /**многокомнатная квартира (более 5 комнат).*/
    ROOMS_MANY(6),
    /**свободная планировка.*/
    OPEN_PLAN_APARTMENT(7),
    /**доля в квартире.*/
    SHARE_APARTMENT(8),
    /** студия.*/
    STUDIO(9),
    /**койко-место.*/
    BED(10),
    ;
    public static RoomsNumRent findByCianId(Integer id){
        for(RoomsNumRent rnr:RoomsNumRent.values()){
            if(rnr.getCianId()==id){
                return rnr;
            }
        }
        return null;
    }
   private final int cianId;

    public Integer getCianId() {
        return cianId;
    }

    RoomsNumRent(int cianId) {
        this.cianId = cianId;
    }
}
