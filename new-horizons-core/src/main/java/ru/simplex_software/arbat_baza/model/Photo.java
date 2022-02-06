package ru.simplex_software.arbat_baza.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.simplex_software.zkutils.entity.LongIdPersistentEntity;

import javax.persistence.*;

/**
 * Photo of realty object.
 */
@Entity @Cacheable(value = true)
@Cache(usage= CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region="Photo")
public class Photo extends LongIdPersistentEntity{
    private String path;
    private String name;
    private String contentType;
    private boolean main=false;
    private boolean advertise=false;
    private boolean plan=false;
    private boolean facad=false;
    @ManyToOne()
    private RealtyObject realtyObject;

    @OneToOne (fetch= FetchType.LAZY)
    private PhotoData data=new PhotoData();

    @OneToOne (fetch= FetchType.LAZY)
    private PhotoData stampedContent;


    @OneToOne (fetch= FetchType.LAZY)
    private PhotoData preview=new PhotoData();

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isMain() {
        return main;
    }

    public void setMain(boolean main) {
        this.main = main;
    }

    public boolean isAdvertise() {
        return advertise;
    }

    public void setAdvertise(boolean advertise) {
        this.advertise = advertise;
    }

    public boolean isPlan() {
        return plan;
    }

    public void setPlan(boolean plan) {
        this.plan = plan;
    }

    public boolean isFacad() {
        return facad;
    }

    public void setFacad(boolean facad) {
        this.facad = facad;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PhotoData getData() {
        return data;
    }

    public void setData(PhotoData data) {
        this.data = data;
    }

    public PhotoData getPreview() {
        return preview;
    }

    public void setPreview(PhotoData preview) {
        this.preview = preview;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public RealtyObject getRealtyObject() {
        return realtyObject;
    }

    public void setRealtyObject(RealtyObject realtyObject) {
        this.realtyObject = realtyObject;
    }

    public PhotoData getStampedContent() {
        return stampedContent;
    }

    public void setStampedContent(PhotoData stampedContent) {
        this.stampedContent = stampedContent;
    }
}
