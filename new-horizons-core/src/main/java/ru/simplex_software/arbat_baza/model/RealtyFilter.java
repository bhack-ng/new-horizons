package ru.simplex_software.arbat_baza.model;

import ru.simplex_software.arbat_baza.model.fias.FiasAddressVector;
import ru.simplex_software.arbat_baza.model.fias.FiasObject;
import ru.simplex_software.arbat_baza.model.odor.ExternalAgency;
import ru.simplex_software.arbat_baza.model.odor.Feed;
import ru.simplex_software.arbat_baza.model.price.Currency;
import ru.simplex_software.arbat_baza.model.price.Period;
import ru.simplex_software.zkutils.entity.LongIdPersistentEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

/**
 * .
 */
@Entity
public class RealtyFilter extends LongIdPersistentEntity{

    public RealtyFilter() {
    }

    public RealtyFilter(Agent agentToFilter) {
        this.agentToFilter = agentToFilter;
    }

    private Long realtyId;

    @ManyToOne
    private Agent agentToFilter;

    @ManyToOne
    private ObjectStatus objectStatus;

    @ManyToOne
    private ExternalAgency externalAgency;

    @ManyToOne
    private  MetroStation metroStation;

    @ManyToOne
    private Street street;

    private String buildingNumber;

    @Enumerated(EnumType.STRING)
    private BuildingType buildingType;

    private Integer floor;

    private Integer priceMin;

    private Integer priceMax;

    private Integer roomCount;

    private String phone;

    private boolean external=false;

    @Column(columnDefinition = "boolean default false")
    private boolean callToday;

    @Enumerated(EnumType.STRING)
    private SortByField sortField=SortByField.ID_UP;

    @Enumerated(EnumType.STRING) @NotNull
    private RealtyObjectType realtyObjectType=RealtyObjectType.LIVE_LEASE;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    private Period period;

    @OneToOne (cascade = CascadeType.ALL)
    private FiasAddressVector fiasAddressVector= new FiasAddressVector();

    /**
     * Clear almost all , except agentToFilter, realtyObjectType , sortField.
     */
    public void clearUserFields(){
        realtyId=null;
        objectStatus=null;
        metroStation=null;
        street=null;
        buildingNumber=null;
        buildingType=null;
        floor=null;
        priceMax=null;
        priceMin=null;
        roomCount=null;
        phone=null;
        callToday=false;
        currency=null;
        period=null;
    }




    public Agent getAgentToFilter() {
        return agentToFilter;
    }

    public void setAgentToFilter(Agent agentToFilter) {
        this.agentToFilter = agentToFilter;
    }

    public Long getRealtyId() {
        return realtyId;
    }

    public void setRealtyId(Long realtyId) {
        this.realtyId = realtyId;
    }

    public ObjectStatus getObjectStatus() {
        return objectStatus;
    }

    public void setObjectStatus(ObjectStatus objectStatus) {
        this.objectStatus = objectStatus;
    }

    public MetroStation getMetroStation() {
        return metroStation;
    }

    public void setMetroStation(MetroStation metroStation) {
        this.metroStation = metroStation;
    }

    public Street getStreet() {
        return street;
    }

    public void setStreet(Street street) {
        this.street = street;
    }

    public String getBuildingNumber() {
        return buildingNumber;
    }

    public void setBuildingNumber(String buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    public BuildingType getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(BuildingType buildingType) {
        this.buildingType = buildingType;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Integer getPriceMin() {
        return priceMin;
    }

    public void setPriceMin(Integer priceMin) {
        this.priceMin = priceMin;
    }

    public Integer getPriceMax() {
        return priceMax;
    }

    public void setPriceMax(Integer priceMax) {
        this.priceMax = priceMax;
    }

    public Integer getRoomCount() {
        return roomCount;
    }

    public void setRoomCount(Integer roomCount) {
        this.roomCount = roomCount;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isCallToday() {
        return callToday;
    }

    public void setCallToday(boolean callToday) {
        this.callToday = callToday;
    }

    public SortByField getSortField() {
        return sortField;
    }

    public void setSortField(SortByField sortField) {
        this.sortField = sortField;
    }

    public RealtyObjectType getRealtyObjectType() {
        return realtyObjectType;
    }

    public void setRealtyObjectType(RealtyObjectType realtyObjectType) {
        this.realtyObjectType = realtyObjectType;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public FiasAddressVector getFiasAddressVector() {
        return fiasAddressVector;
    }

    public void setFiasAddressVector(FiasAddressVector fiasAddressVector) {
        this.fiasAddressVector = fiasAddressVector;
    }

    public boolean isExternal() {
        return external;
    }

    public void setExternal(boolean external) {
        this.external = external;
    }

    public ExternalAgency getExternalAgency() {
        return externalAgency;
    }

    public void setExternalAgency(ExternalAgency externalAgency) {
        this.externalAgency = externalAgency;
    }
}
