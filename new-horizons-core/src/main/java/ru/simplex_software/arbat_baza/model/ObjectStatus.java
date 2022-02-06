package ru.simplex_software.arbat_baza.model;

import net.sf.autodao.PersistentEntity;
import org.hibernate.Hibernate;
import ru.simplex_software.zkutils.entity.LongIdPersistentEntity;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Статусы.
 */
@Entity
public class ObjectStatus  implements PersistentEntity<Long> {
    private String name;
    private String color;


    public Long getPrimaryKey() {
        return id;
    }

    @Id
    private Long id;
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

        if(obj instanceof LongIdPersistentEntity){
            LongIdPersistentEntity pe = (LongIdPersistentEntity) obj;
            if(id==null) {
                return false;
            }
            if (!Hibernate.getClass(this).equals(Hibernate.getClass(pe))) {
                return false;
            }

            return id.equals(pe.getId());

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

    @Override
    public String toString() {
        return getClass().getName()+"{id="+id+"}";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
