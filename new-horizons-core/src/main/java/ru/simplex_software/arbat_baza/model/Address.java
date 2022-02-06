package ru.simplex_software.arbat_baza.model;

import ru.simplex_software.arbat_baza.model.fias.FiasAddressVector;
import ru.simplex_software.arbat_baza.model.fias.FiasObject;
import ru.simplex_software.zkutils.entity.LongIdPersistentEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

/**
 * Местонахождение объекта.
 */
@Entity
public class Address extends LongIdPersistentEntity {
    /**
     * Числовой идентификатор административной области.
     * Справочник можно получить по адресу http://www.cian.ru/admin_areas.php
     * Значение по умолчанвнию: Москва.
     */
    private long admin_area=1;
    /**
     * Название населенного пункта.
     * Обязательно указывайте район области, если он есть, например: locality="Раменский район, деревня Столбово".
     * Обязательно указывайте топонимы, например: locality="Одинцовский район, поселок Ершово",
     * вместо locality="Одинцовский район, Ершово"
     */
    private String locality="Москва";

    /**
     * Название улицы.
     * обязательно указывайте топонимы, например: street="Лесная улица" или street="переулок Сивцев Вражек",
     * вместо street="Лесная" и street="Сивцев Вражек"
     */
    @ManyToOne
    private Street street;

    private String house_str;// 	строковое представление номера дома («55/3», «55с3», «55к3»)

    private String kvartira;//Номер квартиры

    private String district;//район города

    @OneToOne(cascade = CascadeType.ALL)
    private GeoCoordinates geoCoordinates= new GeoCoordinates();

    @OneToOne (cascade = CascadeType.ALL)
    private FiasAddressVector fiasAddressVector= new FiasAddressVector();

    public long getAdmin_area() {
        return admin_area;
    }

    public void setAdmin_area(long admin_area) {
        this.admin_area = admin_area;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public Street getStreet() {
        return street;
    }

    public void setStreet(Street street) {
        this.street = street;
    }

    public String getHouse_str() {
        return house_str;
    }

    public void setHouse_str(String house_str) {
        this.house_str = house_str;
    }

    public String getKvartira() {
        return kvartira;
    }

    public void setKvartira(String kvartira) {
        this.kvartira = kvartira;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public GeoCoordinates getGeoCoordinates() {
        return geoCoordinates;
    }

    public void setGeoCoordinates(GeoCoordinates geoCoordinates) {
        this.geoCoordinates = geoCoordinates;
    }

    public FiasAddressVector getFiasAddressVector() {
        return fiasAddressVector;
    }

    public void setFiasAddressVector(FiasAddressVector fiasAddressVector) {
        this.fiasAddressVector = fiasAddressVector;
    }
}
