package ru.simplex_software.arbat_baza.model.privateHouses;

import ru.simplex_software.arbat_baza.model.CianIdContainer;

/** Взято из Avito .*/
public enum WallsType implements CianIdContainer{
    BRICK(1),                   //Кирпич
    TIMBER(2),                  //брус
    BULK(3),                    //бревно
    METAL(4),                   //металл
    AIR_BLOCKS(5),              //пеноблоки
    SANDWICH_PANELS(6),         //Сендвич-панели
    EXPERIMENTAL_MATERIAL(7),   //Эксперементальные материалы
    FERROCONCRETE(8);           //Ж/Б панели

    private final int cianId;

    WallsType(int cianId){
       this.cianId =cianId;

    }
    @Override
    public Integer getCianId(){
        return cianId;
    }
}
