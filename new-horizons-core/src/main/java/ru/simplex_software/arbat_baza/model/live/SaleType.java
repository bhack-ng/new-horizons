package ru.simplex_software.arbat_baza.model.live;

import ru.simplex_software.arbat_baza.model.CianIdContainer;

/**
 * тип продажи.
 */
public enum SaleType implements CianIdContainer {

    //for sacondhand
    FREE_SH("F"),
    ALTERNATIVE_SH("A"),

    //for new building
    DDU("ddu"), //Договор долевого участия
    ZHSK("zhsk"),//Договор ЖСК
    PEREUSTUPKA("pereustupka"),//Договор уступки прав требования
    PDKP("pdkp"),//Предварительный договор купли-продажи
    INVEST("invest"),//Договор инвестирования
    FREE("free"),//Свободная продажа
    ALT("alt");// Альтернатива
    ;
    private final String cianId;
    public static SaleType[] NEW_BUILDING_SALE_TYPE=new SaleType[]{SaleType.DDU,SaleType.ZHSK,SaleType.PEREUSTUPKA,SaleType.PDKP,
            SaleType.INVEST,SaleType.FREE,SaleType.ALT};

    public static SaleType[] SECONDHAND_SALE_TYPE =new SaleType[]{SaleType.ALTERNATIVE_SH,SaleType.FREE_SH};


    SaleType(String cianId) {
        this.cianId = cianId;
    }

    public String getCianId() {
        return cianId;
    }

    public static SaleType findByCianId(String id){
        for(SaleType st:SaleType.values()){
            if(st.getCianId().equalsIgnoreCase(id)){
                return st;
            }
        }
        return null;
    }
}
