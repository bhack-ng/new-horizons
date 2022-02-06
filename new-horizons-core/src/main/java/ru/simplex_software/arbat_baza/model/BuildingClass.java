package ru.simplex_software.arbat_baza.model;

/**
 * Класс строения.
 1 – A
 2 – A+
 3 - B
 4 - B+
 5 - C
 6 - C+
 */
public enum BuildingClass implements CianIdContainer {
    A(1),
    A_PLUS(2),
    B(3),
    B_PLUS(4),
    C(5),
    C_PLUS(6);
    private final int cianId;

    public static BuildingClass findByCianId(Integer id){
        for(BuildingClass bc:BuildingClass.values()){
            if(bc.getCianId()==id){
                return bc;
            }
        }
        return null;
    }

    BuildingClass(Integer cianId) {
        this.cianId = cianId;
    }

    @Override
    public Integer getCianId() {
        return cianId;
    }
}
