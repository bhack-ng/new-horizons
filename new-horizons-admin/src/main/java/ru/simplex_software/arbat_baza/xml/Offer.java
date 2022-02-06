package ru.simplex_software.arbat_baza.xml;

/**
 * Created by dima on 11.07.16.
 */
public class Offer {
    /**
     *  Тип сделки: «продажа», «аренда».
     */
    public enum Type{
        продажа, аренда;
        public static Type getValue(String s){
            for (Type t: Type.values()) {
                if(t.name().equalsIgnoreCase(s)){
                    return t;
                }
            }
            return null;
        }
    }
    public enum PropertyType{
        Жилая
    }
    enum Category{
        ROOM("комната"), FLAT("квартира"), TOWNHOUSE("таунхаус"),HOUSE("дом"),
        HOUSE_PART("часть дома"), LOT("участок"), AREA_PART("земельный участок"),
        HOUSE_WITH_LOT("дом с участком"), COTTAGE("дача"), COMMERICAL("коммерческая")
        ;

        Category(String rusDesc) {
            this.rusDesc = rusDesc;
        }

        private String rusDesc;
    }

    enum CommercialType{
        AUTO_REPAIR, //автосервис
        BUSINESS,// (готовый бизнес),
        FREE_PURPOSE, // (помещения свободного назначения)
        HOTEL,// (гостиница),
        LAND, //(земли коммерческого назначения),
        LEGAL_ADRESS,// «legal address» (юридический адрес),
        MANUFACTURING,// (производственное помещение),
        OFFICE,// (офисные помещения),
        PUBLIC_CATERING,// (общепит),
        RETAIL,//(торговые помещения),
        WAREHOUSE //» (склад).
    }
    Type type;
    PropertyType propertyType;

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
