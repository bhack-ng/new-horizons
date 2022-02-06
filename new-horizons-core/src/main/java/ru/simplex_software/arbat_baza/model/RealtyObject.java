package ru.simplex_software.arbat_baza.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OrderBy;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplex_software.arbat_baza.AbstractArea;
import ru.simplex_software.arbat_baza.model.clients.Client;
import ru.simplex_software.arbat_baza.model.commerce.Commerce;
import ru.simplex_software.arbat_baza.model.live.LiveLeaseRealty;
import ru.simplex_software.arbat_baza.model.live.LiveSaleRealty;
import ru.simplex_software.arbat_baza.model.price.SimplePrice;
import ru.simplex_software.arbat_baza.model.privateHouses.PrivateHouseRent;
import ru.simplex_software.arbat_baza.model.privateHouses.PrivateHouseSale;
import ru.simplex_software.arbat_baza.model.stead.SteadRent;
import ru.simplex_software.arbat_baza.model.stead.SteadSale;
import ru.simplex_software.zkutils.entity.LongIdPersistentEntity;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * .
 */
@Entity @Inheritance(strategy = InheritanceType.JOINED)
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE) @Cacheable
public class RealtyObject extends LongIdPersistentEntity{
    private static final Logger LOG = LoggerFactory.getLogger(RealtyObject.class);

    public static RealtyObject create(@NotNull RealtyObjectType type){
        RealtyObject ret;
        if(type==RealtyObjectType.COMMERCE_LEASE || type==RealtyObjectType.COMMERCE_SALE){
            ret=new Commerce();
        }else if(type==RealtyObjectType.LIVE_LEASE ){
            ret=new LiveLeaseRealty();
        }else if (type==RealtyObjectType.LIVE_SALE) {
            ret = new LiveSaleRealty();
        }else if (type ==RealtyObjectType.STEAD_RENT){
            ret = new SteadRent();
        }else if(type == RealtyObjectType.STEAD_SALE){
            ret = new SteadSale();
        }else if (type ==RealtyObjectType.PRIVATE_HOUSE_SALE){
            ret = new PrivateHouseSale();
        }else if (type == RealtyObjectType.PRIVATE_HOUSE_RENT){
            ret = new PrivateHouseRent();
        }
        else {
            throw new RuntimeException();
        }
        ret.setRealtyObjectType(type);
        return ret;
    }

    /**
     * Дата создания объекта
     */
    private Instant creationTime = Instant.now();


    @OneToOne(cascade = CascadeType.ALL)
    private AbstractOptions options ;

    @OneToOne(cascade = CascadeType.ALL)
    private AbstractBuilding building;

    /**
     * A floorScheme of floor.
     */
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private FloorScheme floorScheme;

    @OneToOne(cascade = CascadeType.ALL)
    private MetroLocation metroLocation= new MetroLocation ();

    @ManyToOne
    private ObjectStatus objectStatus;


    @Enumerated(value = EnumType.STRING)
    @NotNull
    private RealtyObjectType realtyObjectType;


    /**Object owner.*/
    @ManyToOne
    private Agent agent;

    /**
     * Арендатор
     */
    @ManyToOne
    private Client renter;


    @OneToOne(cascade = CascadeType.ALL)
    private AbstractArea area;


    @OneToOne(cascade = CascadeType.ALL)
    private SimplePrice price;

    private LocalDate nextCall;
    private LocalDateTime changed=LocalDateTime.now();

    @OneToOne (cascade = CascadeType.ALL)
    private Address address=new Address();

    @OneToOne(cascade = CascadeType.ALL)
    private Commission commission=new Commission();

    private  boolean publishCian=false;
    private  boolean publishAvito = false ;
    private  boolean publishExternal = false;
    private  boolean publishSite=false;
    private boolean premiumInCian=false;
    private boolean publishExternalAvito= false;

    /**for publishing to realty.yandex */
    private boolean publishYandexRealty = false;

    private String linkToCian;
    @Column(length = 4096)  @Length(max = 4096)
    private String note;

    @OneToMany(mappedBy = "realtyObject" , fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @OrderBy(clause = "main desc, id")
    private List<Photo> photos=new ArrayList<Photo>();

    /**for external objects impoted form other agency .*/
    @OneToOne(cascade = CascadeType.ALL)
    private ExternalObectExt externalObectExt;

    @OneToMany (mappedBy = "realtyObject" , fetch = FetchType.LAZY)
    private List<ContactsOfOwner> contactsOfOwners= new ArrayList<ContactsOfOwner>();

    @OneToMany (mappedBy = "realtyObject" , fetch = FetchType.LAZY)
    private List<Recommendation> recommendations = new ArrayList<Recommendation>();


    @OneToMany (mappedBy = "realtyObject",cascade = CascadeType.ALL,fetch = FetchType.LAZY)  @OrderBy(clause = "created desc")
    private List<Comment> comments = new ArrayList<Comment>();

    /**
     * iditifier in old database (on .Net application). Only for imported objects. null for objects created in this application.
     */
    private Long oldDbIdentifier;

    /**
     * Коорлинаты на плане.
     */
    private String floorSchemeCoordinates;

    public FloorScheme getFloorScheme() {
        return floorScheme;
    }

    public void setFloorScheme(FloorScheme floorScheme) {
        this.floorScheme = floorScheme;
    }

    public Long getOldDbIdentifier() {
        return oldDbIdentifier;
    }

    public void setOldDbIdentifier(long oldDbIdentifier) {
        this.oldDbIdentifier = oldDbIdentifier;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Commission getCommission() {
        return commission;
    }

    public void setCommission(Commission commission) {
        this.commission = commission;
    }

    public MetroLocation getMetroLocation() {
        return metroLocation;
    }

    public void setMetroLocation(MetroLocation metroLocation) {
        this.metroLocation = metroLocation;
    }

    public LocalDate getNextCall() {
        return nextCall;
    }

    public void setNextCall(LocalDate nextCall) {
        this.nextCall = nextCall;
    }

    public ObjectStatus getObjectStatus() {
        return objectStatus;
    }

    public void setObjectStatus(ObjectStatus objectStatus) {
        this.objectStatus = objectStatus;
    }

    public boolean isPremiumInCian() {
        return premiumInCian;
    }

    public void setPremiumInCian(boolean premiumInCian) {
        this.premiumInCian = premiumInCian;
    }

    public boolean isPublishSite() {
        return publishSite;
    }

    public void setPublishSite(boolean publishSite) {
        this.publishSite = publishSite;
    }

    public boolean isPublishCian() {
        return publishCian;
    }

    public void setPublishCian(boolean publishCian) {
        this.publishCian = publishCian;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public RealtyObjectType getRealtyObjectType() {
        return realtyObjectType;
    }

    public void setRealtyObjectType(RealtyObjectType realtyObjectType) {
        this.realtyObjectType = realtyObjectType;
    }

    public String getLinkToCian() {
        return linkToCian;
    }

    public void setLinkToCian(String linkToCian) {
        this.linkToCian = linkToCian;
    }

    public Client getRenter() {
        return renter;
    }

    public void setRenter(Client renter) {
        this.renter = renter;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<ContactsOfOwner> getContactsOfOwners() {
        return contactsOfOwners;
    }

    public void setContactsOfOwners(List<ContactsOfOwner> contactsOfOwners) {
        this.contactsOfOwners = contactsOfOwners;
    }

    public List<Recommendation> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<Recommendation> recommendations) {
        this.recommendations = recommendations;
    }

    public AbstractArea getArea() {
        return area;
    }

    public void setArea(AbstractArea area) {
        this.area = area;
    }

    public SimplePrice getPrice() {
        return price;
    }

    public void setPrice(SimplePrice price) {
        this.price = price;
    }

    public AbstractOptions getOptions() {
        return options;
    }

    public void setOptions(AbstractOptions options) {
        this.options = options;
    }

    public AbstractBuilding getBuilding() {
        return building;
    }

    public void setBuilding(AbstractBuilding building) {
        this.building = building;
    }

    public LocalDateTime getChanged() {
        return changed;
    }

    public void setChanged(LocalDateTime changed) {
        this.changed = changed;
    }

    public ExternalObectExt getExternalObectExt() {
        return externalObectExt;
    }

    public void setExternalObectExt(ExternalObectExt externalObectExt) {
        this.externalObectExt = externalObectExt;
    }

    public boolean getPublishExternal() {
        return publishExternal;
    }

    public void setPublishExternal(boolean publishExternal) {
        this.publishExternal = publishExternal;
    }

    public void setPublishAvito(boolean publishAvito) {
        this.publishAvito = publishAvito;
    }

    public boolean isPublishExternalAvito() {
        return publishExternalAvito;
    }

    public void setPublishExternalAvito(boolean publishExternalAvito) {
        this.publishExternalAvito = publishExternalAvito;
    }

    public boolean isPublishExternal() {
        return publishExternal;
    }

    public boolean isPublishAvito() {
        return publishAvito;
    }

    public Instant getCreationTime() {
        return creationTime;
    }

   public boolean isPublishYandexRealty () {return publishYandexRealty; }


    public void setPublishYandexRealty (boolean publishYandexRealty) {
        this.publishYandexRealty = publishYandexRealty;
    }

    public String getFloorSchemeCoordinates() {
        return floorSchemeCoordinates;
    }

    public void setFloorSchemeCoordinates(String floorSchemeCoordinates) {
        this.floorSchemeCoordinates = floorSchemeCoordinates;
    }
}
