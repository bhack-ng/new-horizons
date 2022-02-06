package ru.simplex_software.arbat_baza.model.commerce;

/**
 * Тип помещения.
 */
public enum CommerceType {
    OFFICE ("O"), //   O,// – офис ;
    WAREHOUSE("W"),// – склад
    TRADE("T"),//// – торговая площадь
    FOOD_SERVICES("F"),//– под общепит
    FREE_PURPOSE("FP"),//– помещение свободного назначения
    GARAGE("G"),//– гараж
    CAR_SERVICE_CENTER("AU"),//– автосервис
    MANUFACTURING_AREA("WP"),//– производственное помещение
    DETACHED_BUILDING("B"),//– отдельно стоящее здание
    LEGAL_ADDRESS("UA"), //– юридический адрес
    BUSINESS_FOR_SALE("SB"),// – продажа бизнеса
    CONSUMERSEARVICE("BU")//под бытовые услуги (салон красоты и т.д.)
    ;
    private final String cianChars;

    public static CommerceType findByCianId(String id){
        for(CommerceType ct:CommerceType.values()){
            if( ct.getCianChars().equalsIgnoreCase(id)){
                return ct;
            }
        }
        return null;
    }
    CommerceType(String cianChars) {
        this.cianChars = cianChars;
    }

    public String getCianChars() {
        return cianChars;
    }
}
