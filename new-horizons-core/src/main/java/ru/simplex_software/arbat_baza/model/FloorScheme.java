package ru.simplex_software.arbat_baza.model;

import ru.simplex_software.zkutils.entity.LongIdPersistentEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.HashSet;
import java.util.Set;

@Entity
public class FloorScheme extends LongIdPersistentEntity {

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "floorScheme", fetch = FetchType.EAGER,
        cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<RealtyObject> realtyObjects = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private PhotoData data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<RealtyObject> getRealtyObjects() {
        return realtyObjects;
    }

    public void setRealtyObjects(Set<RealtyObject> realtyObjects) {
        this.realtyObjects = realtyObjects;
    }

    public void addRealtyObject(RealtyObject realtyObject) {
        realtyObjects.add(realtyObject);
        realtyObject.setFloorScheme(this);
    }

    public void removeRealtyObject(RealtyObject realtyObject) {
        realtyObjects.remove(realtyObject);
        realtyObject.setFloorScheme(null);
    }

    public PhotoData getData() {
        return data;
    }

    public void setData(PhotoData data) {
        this.data = data;
    }
}
