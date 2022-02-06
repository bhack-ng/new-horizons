package ru.simplex_software.arbat_baza.xml.yandex.jaxb.adapters;

import ru.simplex_software.arbat_baza.xml.yandex.jaxb.Offer;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Created by dima on 29.07.16.
 */
@XmlRootElement
public class LiveOffer extends Offer{

    //Описание жилого помещения

    @XmlElement(name="new-flat")
    private Boolean newFlat;
    @XmlElement()
    private int rooms;
    @XmlElement(name="rooms-offered")
    private int roomsOffered;


    @XmlJavaTypeAdapter(value = BooleanAdapter.class)
    private Boolean studio;

    @XmlElement(name = "floor")
    private int floor;


    /**
     * Свободная планировка.
     */
    @XmlJavaTypeAdapter(value = BooleanAdapter.class)
    @XmlElement(name = "open-plan")
    private Boolean openPlan;


    /**
     * Апартаменты.
     */
    @XmlJavaTypeAdapter(value = BooleanAdapter.class)
    @XmlElement
    private Boolean apartments;

    /**
     * Рекомендуемые значения: «смежные», «раздельные».
     */
    private String roomsType;

    /**
     * Наличие телефона.
     */
    @XmlJavaTypeAdapter(value = BooleanAdapter.class)
    @XmlElement
    private Boolean phone;



    /**
     * Наличие интернета.
     */
    @XmlJavaTypeAdapter(value = BooleanAdapter.class)
    @XmlElement
    private Boolean internet;


    /**Наличие мебели.*/
    @XmlJavaTypeAdapter(value = BooleanAdapter.class)
    @XmlElement(name = "room-furniture")
    private Boolean roomFurniture;

    /** Наличие мебели на кухне.*/
    @XmlJavaTypeAdapter(value = BooleanAdapter.class)
    @XmlElement(name = "kitchen-furniture")
    private Boolean kitchenFurniture;

    /** Наличие телевизора.*/
    @XmlJavaTypeAdapter(value = BooleanAdapter.class)
    @XmlElement
    private Boolean television;

    /** Наличие стиральной машины.*/
    @XmlJavaTypeAdapter(value = BooleanAdapter.class)
    @XmlElement(name = "washing-machine")
    private Boolean washingMachine;

    /** Наличие посудомоечной машины.*/
    @XmlJavaTypeAdapter(value = BooleanAdapter.class)
    @XmlElement
    private Boolean dishwasher;

    /** Наличие холодильника.*/
    @XmlJavaTypeAdapter(value = BooleanAdapter.class)
    @XmlElement
    private Boolean refrigerator;

    /**Встроенная техника.*/
    @XmlJavaTypeAdapter(value = BooleanAdapter.class)
    @XmlElement(name = "built-in-tech")
    private Boolean builtInTech;

    /**
     * Тип балкона.
     * Возможные значения: «балкон», «лоджия», «2 балкона», «2 лоджии» и т. п.
     */
    @XmlElement
    private String balcony;


    /**
     * Тип санузла.
     * Рекомендуемые значения: «совмещенный», «раздельный», числовое значение — например «2».
     */
    @XmlElement (name = "bathroom-unit")
    private String bathroomUnit;

    /**
     *Покрытие пола.
     * Рекомендуемые значения: «паркет», «ламинат», «ковролин», «линолеум».
     */
    @XmlElement(name = "floor-covering")
    private String floorCovering;

    /**
     * Вид из окон.
     * Рекомендуемые значения: «во двор», «на улицу».
     */
    @XmlElement(name = "window-view")
    private String windowView;



    public Boolean getNewFlat() {
        return newFlat;
    }

    public void setNewFlat(Boolean newFlat) {
        this.newFlat = newFlat;
    }

    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public int getRoomsOffered() {
        return roomsOffered;
    }

    public void setRoomsOffered(int roomsOffered) {
        this.roomsOffered = roomsOffered;
    }

    public Boolean getStudio() {
        return studio;
    }

    public void setStudio(Boolean studio) {
        this.studio = studio;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public Boolean getOpenPlan() {
        return openPlan;
    }

    public void setOpenPlan(Boolean openPlan) {
        this.openPlan = openPlan;
    }

    public Boolean getApartments() {
        return apartments;
    }

    public void setApartments(Boolean apartments) {
        this.apartments = apartments;
    }

    public String getRoomsType() {
        return roomsType;
    }

    public void setRoomsType(String roomsType) {
        this.roomsType = roomsType;
    }

    public Boolean getPhone() {
        return phone;
    }

    public void setPhone(Boolean phone) {
        this.phone = phone;
    }

    public Boolean getInternet() {
        return internet;
    }

    public void setInternet(Boolean internet) {
        this.internet = internet;
    }

    public Boolean getRoomFurniture() {
        return roomFurniture;
    }

    public void setRoomFurniture(Boolean roomFurniture) {
        this.roomFurniture = roomFurniture;
    }

    public Boolean getKitchenFurniture() {
        return kitchenFurniture;
    }

    public void setKitchenFurniture(Boolean kitchenFurniture) {
        this.kitchenFurniture = kitchenFurniture;
    }

    public Boolean getTelevision() {
        return television;
    }

    public void setTelevision(Boolean television) {
        this.television = television;
    }

    public Boolean getDishwasher() {
        return dishwasher;
    }

    public void setDishwasher(Boolean dishwasher) {
        this.dishwasher = dishwasher;
    }

    public Boolean getRefrigerator() {
        return refrigerator;
    }

    public void setRefrigerator(Boolean refrigerator) {
        this.refrigerator = refrigerator;
    }

    public Boolean getBuiltInTech() {
        return builtInTech;
    }

    public void setBuiltInTech(Boolean builtInTech) {
        this.builtInTech = builtInTech;
    }

    public String getBalcony() {
        return balcony;
    }

    public void setBalcony(String balcony) {
        this.balcony = balcony;
    }

    public String getBathroomUnit() {
        return bathroomUnit;
    }

    public void setBathroomUnit(String bathroomUnit) {
        this.bathroomUnit = bathroomUnit;
    }

    public String getFloorCovering() {
        return floorCovering;
    }

    public void setFloorCovering(String floorCovering) {
        this.floorCovering = floorCovering;
    }
}
