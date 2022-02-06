package ru.simplex_software.arbat_baza.model.live;

import ru.simplex_software.arbat_baza.model.CianIdContainer;

/**
 * Куда выходят окна.
 */
public enum WindowsPosition implements CianIdContainer{
    /**Двор.*/
    COURTYARD(1),
     /**Улица.*/
    STREET(2),
     /**И двор и улица.*/
    COURTYARD_AND_STREET(3);

    private final int cianId;

    public static WindowsPosition findByCianId(int id){
        for(WindowsPosition wp:WindowsPosition.values()){
            if(wp.getCianId()==id){
                return wp;
            }
        }
        return null;
    }

    WindowsPosition(int cianId) {
        this.cianId = cianId;
    }

    @Override
    public Integer getCianId() {
        return cianId;
    }
}
