package ru.simplex_software.arbat_baza.model.fias;

import ru.simplex_software.zkutils.entity.LongIdPersistentEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Path address object to root. more duplicatoins, but faster search.
 */
@Entity
public class FiasAddressVector extends LongIdPersistentEntity{
    @ManyToOne
    private FiasObject level1;
    /**empty in xml*/
    @ManyToOne
    private FiasObject level2;
    @ManyToOne
    private FiasObject level3;
    @ManyToOne
    private FiasObject level4;
    @ManyToOne
    private FiasObject level5;
    @ManyToOne
    private FiasObject level6;

    /**Уровень улиц.*/
    @ManyToOne
    private FiasObject level7;

    public FiasAddressVector() {
    }

    public FiasAddressVector(FiasObject[] levels) {
        level1 = levels[0];
        level2 = levels[1];
        level3 = levels[2];
        level4 = levels[3];
        level5 = levels[4];
        level6 = levels[5];
        level7 = levels[6];
    }

    /**
     * Возвращает самый нижний уровень адреса.
     */
    public FiasObject getLastLevel() {
        for (int i = 6; i >= 0; i--) {
              final FiasObject[] fiasObjects = toArray();
            if (fiasObjects[i] != null) {
                return fiasObjects[i];
              }
          }
        return null;
    }

    public FiasObject getLevel1() {
        return level1;
    }

    public void setLevel1(FiasObject level1) {
        this.level1 = level1;
    }

    public FiasObject getLevel2() {
        return level2;
    }

    public void setLevel2(FiasObject level2) {
        this.level2 = level2;
    }

    public FiasObject getLevel3() {
        return level3;
    }

    public void setLevel3(FiasObject level3) {
        this.level3 = level3;
    }

    public FiasObject getLevel4() {
        return level4;
    }

    public void setLevel4(FiasObject level4) {
        this.level4 = level4;
    }

    public FiasObject getLevel5() {
        return level5;
    }

    public void setLevel5(FiasObject level5) {
        this.level5 = level5;
    }

    public FiasObject getLevel6() {
        return level6;
    }

    public void setLevel6(FiasObject level6) {
        this.level6 = level6;
    }

    public FiasObject getLevel7() {
        return level7;
    }

    public void setLevel7(FiasObject level7) {
        this.level7 = level7;
    }

    public void setLevels(FiasObject[] levels) {
        level1 = levels[0];
        level2 = levels[1];
        level3 = levels[2];
        level4 = levels[3];
        level5 = levels[4];
        level6 = levels[5];
        level7 = levels[6];
    }

    public FiasObject[] toArray() {
        return new FiasObject[]{level1, level2, level3, level4, level5, level6, level7};
    }

    @Override
    public String toString() {
        StringBuilder sb= new StringBuilder();
        for(FiasObject fo:this.toArray()){
            if(fo!=null){
                sb.append(fo.toShortString());
                sb.append(", ");
            }
        }
        if(sb.length()>2){
            sb.delete(sb.length()-2, sb.length());
        }
        return sb.toString();
    }
}
