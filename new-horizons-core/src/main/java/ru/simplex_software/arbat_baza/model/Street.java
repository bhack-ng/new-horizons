package ru.simplex_software.arbat_baza.model;

import net.sf.autodao.PersistentEntity;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * .
 */
@Entity
public class Street implements PersistentEntity<Long> {
    @Id
    private Long id;

    private String streetName;

    @Override
    public Long getPrimaryKey() {
        return id;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }
}
