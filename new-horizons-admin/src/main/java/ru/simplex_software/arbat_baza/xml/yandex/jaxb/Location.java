package ru.simplex_software.arbat_baza.xml.yandex.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by dima on 12.07.16.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Location {

    @XmlElement
    private String country="Россия";
    @XmlElement
    private String region;
    @XmlElement
    private String district;
    @XmlElement(name = "locality-name")
    private String localityName;
    @XmlElement(name="sub-locality-name")
    private String subLocalityName;
    @XmlElement
    private String address;
    @XmlElement
    private String direction;
    @XmlElement
    private String distance;
    @XmlElement
    private String latitude;
    @XmlElement
    private String longitude;
    @XmlElement
    private Metro metro;
    @XmlElement(name = "railway-station")
    private String railwayStation;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLocalityName() {
        return localityName;
    }

    public void setLocalityName(String localityName) {
        this.localityName = localityName;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Metro getMetro() {
        return metro;
    }

    public void setMetro(Metro metro) {
        this.metro = metro;
    }

    public String getRailwayStation() {
        return railwayStation;
    }

    public void setRailwayStation(String railwayStation) {
        this.railwayStation = railwayStation;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSubLocalityName() {
        return subLocalityName;
    }

    public void setSubLocalityName(String subLocalityName) {
        this.subLocalityName = subLocalityName;
    }
}
