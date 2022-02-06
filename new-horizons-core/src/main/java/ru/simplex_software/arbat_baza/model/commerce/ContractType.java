package ru.simplex_software.arbat_baza.model.commerce;

import ru.simplex_software.arbat_baza.model.CianIdContainer;

/**
 * Тип договора.
 */
public enum ContractType implements CianIdContainer {
    DIRECT_LEASING(1),//    1 – прямая аренда
    SUBLEASE(2),//    2 – субаренда
    SALE_OF_LEASEHOLD(3),//    3 – продажа права аренды (ППА)
    SALE_REALTY_OBJECT(4),//    4 – продажа объекта
    CONTRACT_OF_JOINT_ACTIVITY(5);    //    5 – договор совместной деятельности ;

    private final int cianId;

    public static ContractType findByCianId(Integer id){
        for(ContractType rnr:ContractType.values()){
            if(rnr.getCianId()==id){
                return rnr;
            }
        }
        return null;
    }

    public Integer getCianId() {
        return cianId;
    }

    ContractType(int cianId) {
        this.cianId = cianId;
    }
}
