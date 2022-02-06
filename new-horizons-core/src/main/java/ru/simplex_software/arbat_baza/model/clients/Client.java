package ru.simplex_software.arbat_baza.model.clients;

import ru.simplex_software.arbat_baza.model.DbFile;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.zkutils.entity.LongIdPersistentEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * Абстрактный класс клиента.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Client extends LongIdPersistentEntity {

    /**
     * ФИО или полное название
     */
    protected String name;

    /**
     * Номер телефона
     */
    @Column(unique = true)
    protected String phone;

    /**
     * Номер мобильного телефона
     */
    @Column(unique = true)
    protected String mobilePhone;

    /**
     * Email
     */
    @Column(unique = true)
    protected String email;

    /**
     * Комментарий
     */
    protected String comment;

    /**
     * Список задач
     */
    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER,
            cascade = CascadeType.REMOVE)
    protected List<ClientTask> tasks = new ArrayList<>();

    /**
     * Список объектов недвижимости.
     */
    @OneToMany(mappedBy = "renter", fetch = FetchType.EAGER,
            cascade = CascadeType.REMOVE)
    protected List<RealtyObject> realtyObjects = new ArrayList<>();

    /**
     * Список файлов
     */
    @OneToMany(cascade = CascadeType.REMOVE)
    protected List<DbFile> files = new ArrayList<>();

    /**
     * Флаг удаления.
     */
    @Column(columnDefinition = "boolean default false")
    protected boolean deleted;

    /**
     * Статус клиента.
     */
    @Enumerated(EnumType.STRING)
    protected Status status = Status.NEW;

    /**
     * Тип рекламной кампании.
     */
    @Enumerated(EnumType.STRING)
    protected AdvertisingCampaignType advertisingCampaignType;

    /**
     * Номер рекламной кампании.
     */
    protected String advertisingCampaignNumber;

    /**
     * Номер рекламного объявления.
     */
    protected String advertisementNumber;

    /**
     * Поисковый запрос.
     */
    protected String searchQuery;

    /**
     * Поисковая фраза.
     */
    protected String searchPhrase;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<ClientTask> getTasks() {
        return tasks;
    }

    public void setTasks(List<ClientTask> tasks) {
        this.tasks = tasks;
    }

    public List<RealtyObject> getRealtyObjects() {
        return realtyObjects;
    }

    public void setRealtyObjects(List<RealtyObject> realtyObjects) {
        this.realtyObjects = realtyObjects;
    }

    public List<DbFile> getFiles() {
        return files;
    }

    public void setFiles(List<DbFile> files) {
        this.files = files;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public AdvertisingCampaignType getAdvertisingCampaignType() {
        return advertisingCampaignType;
    }

    public void setAdvertisingCampaignType(AdvertisingCampaignType advertisingCampaignType) {
        this.advertisingCampaignType = advertisingCampaignType;
    }

    public String getAdvertisingCampaignNumber() {
        return advertisingCampaignNumber;
    }

    public void setAdvertisingCampaignNumber(String advertisingCampaignNumber) {
        this.advertisingCampaignNumber = advertisingCampaignNumber;
    }

    public String getAdvertisementNumber() {
        return advertisementNumber;
    }

    public void setAdvertisementNumber(String advertisementNumber) {
        this.advertisementNumber = advertisementNumber;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public String getSearchPhrase() {
        return searchPhrase;
    }

    public void setSearchPhrase(String searchPhrase) {
        this.searchPhrase = searchPhrase;
    }
}
