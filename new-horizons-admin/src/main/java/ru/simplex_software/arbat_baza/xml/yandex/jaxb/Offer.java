package ru.simplex_software.arbat_baza.xml.yandex.jaxb;

import ru.simplex_software.arbat_baza.xml.yandex.jaxb.adapters.CategoryAdapter;
import ru.simplex_software.arbat_baza.xml.yandex.jaxb.adapters.CommercialTypeAdapter;
import ru.simplex_software.arbat_baza.xml.yandex.jaxb.adapters.InstantAdapter;

import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.Instant;
import java.util.List;

/**
 * Created by dima on 11.07.16.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Offer {

    @XmlAttribute(name = "internal-id")
    private String internalId;

    @XmlElement
    private Type type;

    @XmlElement(name = "property-type")
    private PropertyType propertyType;

    @XmlJavaTypeAdapter(value = CategoryAdapter.class)
    @XmlElement(name = "category")
    private  Category category;

    @XmlJavaTypeAdapter(value = CommercialTypeAdapter.class)
    @XmlElement(name = "commercial-type")
    private CommercialType commercialType;
    @XmlElement(name = "commercial-building-type")
    private CommercialBuildingType commercialBuildingType;

    @XmlElement(name = "purpose")
    private List<Purpose> purposeList;

    @XmlElement(name = "purpose-warehouse")
    private List<PurposeWarehouse> purposeWarehouses;

    @XmlElement (name = "lot-number")
    private String lotNumber;

    @XmlElement(name = "url")
    private String url;

    @XmlElement(name = "creation-date") @XmlJavaTypeAdapter(InstantAdapter.class)
    private Instant creationDate;

    @XmlElement(name = "last-update-date") @XmlJavaTypeAdapter(InstantAdapter.class)
    private Instant lastUpdateDate;


    @XmlElement(name = "expire-date") @XmlJavaTypeAdapter(InstantAdapter.class)
    private Instant expireDate;


    @XmlElement(name = "payed-adv")
    private boolean payedAdv;

    @XmlElement(name = "manually-added")
    private boolean manuallyAdded;

    @XmlElement
    private Location location;

    @XmlElement(name = "sales-agent")
    private SalesAgent salesAgent;

    @XmlElement
    private Price price;







    ///////////////////////Информация об объекте
    /** Общая площадь. Обязательное поле для всех объявлений, кроме участков (см. элемент lot-area).*/
    @XmlElement
    private Square area;

    @XmlElement(name = "room-space")
    private Square  roomSpace;

    @XmlElement(name = "living-space")
    private Square  livingSpace;


    @XmlElement(name = "kitchen-space")
    private Square  kitchenSpace;


    @XmlElement(name = "lot-area")
    private Square  lotArea;
    @XmlElement
    @Size(min = 1)
    private List<String> image;

    /**
     * Ремонт.
     */
    @XmlElement
    private Renovation renovation;

    @XmlElement
    private Quality quality;

    /**Дополнительная информация (описание в свободной форме).*/
    @XmlElement
    private String description;

    private LotType lotType;




//Описание нежилого помещения https://yandex.ru/support/webmaster/realty/requirements.xml#non-residential




    public Square getArea() {
        return area;
    }

    public void setArea(Square area) {
        this.area = area;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public String getInternalId() {
        return internalId;
    }

    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public CommercialBuildingType getCommercialBuildingType() {
        return commercialBuildingType;
    }

    public void setCommercialBuildingType(CommercialBuildingType commercialBuildingType) {
        this.commercialBuildingType = commercialBuildingType;
    }

    public CommercialType getCommercialType() {
        return commercialType;
    }

    public void setCommercialType(CommercialType commercialType) {
        this.commercialType = commercialType;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Instant expireDate) {
        this.expireDate = expireDate;
    }

    public Instant getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Instant lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
    }

    public boolean isManuallyAdded() {
        return manuallyAdded;
    }

    public void setManuallyAdded(boolean manuallyAdded) {
        this.manuallyAdded = manuallyAdded;
    }

    public boolean isPayedAdv() {
        return payedAdv;
    }

    public void setPayedAdv(boolean payedAdv) {
        this.payedAdv = payedAdv;
    }

    public List<Purpose> getPurposeList() {
        return purposeList;
    }

    public void setPurposeList(List<Purpose> purposeList) {
        this.purposeList = purposeList;
    }

    public List<PurposeWarehouse> getPurposeWarehouses() {
        return purposeWarehouses;
    }

    public void setPurposeWarehouses(List<PurposeWarehouse> purposeWarehouses) {
        this.purposeWarehouses = purposeWarehouses;
    }

    public SalesAgent getSalesAgent() {
        return salesAgent;
    }

    public void setSalesAgent(SalesAgent salesAgent) {
        this.salesAgent = salesAgent;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Square getRoomSpace() {
        return roomSpace;
    }

    public void setRoomSpace(Square roomSpace) {
        this.roomSpace = roomSpace;
    }

    public Square getLivingSpace() {
        return livingSpace;
    }

    public void setLivingSpace(Square livingSpace) {
        this.livingSpace = livingSpace;
    }

    public Square getKitchenSpace() {
        return kitchenSpace;
    }

    public void setKitchenSpace(Square kitchenSpace) {
        this.kitchenSpace = kitchenSpace;
    }

    public Square getLotArea() {
        return lotArea;
    }

    public void setLotArea(Square lotArea) {
        this.lotArea = lotArea;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public Renovation getRenovation() {
        return renovation;
    }

    public void setRenovation(Renovation renovation) {
        this.renovation = renovation;
    }

    public Quality getQuality() {
        return quality;
    }

    public void setQuality(Quality quality) {
        this.quality = quality;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LotType getLotType() {
        return lotType;
    }

    public void setLotType(LotType lotType) {
        this.lotType = lotType;
    }


}
