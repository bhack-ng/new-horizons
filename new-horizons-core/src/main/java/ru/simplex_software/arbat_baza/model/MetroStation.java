package ru.simplex_software.arbat_baza.model;

import net.sf.autodao.PersistentEntity;
import org.hibernate.Hibernate;
import ru.simplex_software.zkutils.entity.LongIdPersistentEntity;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Станция метро.
 */
@Entity
public class MetroStation implements PersistentEntity<Long> {
    @Id
    private Long id;

    private String stationName;

    @Override
    public Long getPrimaryKey() {
        return id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    @Override
    public int hashCode() {
        if(id==null){
            return 0;
        }else{
            return id.hashCode();
        }
    }


    @Override
    public boolean equals(Object obj) {

        if(obj instanceof PersistentEntity){
            PersistentEntity pe = (PersistentEntity) obj;
            if(id==null) {
                return false;
            }
            if (!Hibernate.getClass(this).equals(Hibernate.getClass(pe))) {
                return false;
            }

            return id.equals(pe.getPrimaryKey());

        }else{
            return false;
        }
    }

}
