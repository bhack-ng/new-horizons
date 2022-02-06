package ru.simplex_software.arbat_baza.xml.yandex.jaxb;

import ru.simplex_software.arbat_baza.xml.yandex.jaxb.adapters.InstantAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.Instant;
import java.util.List;

/**
 * Created by dima on 11.07.16.
 */
@XmlRootElement(name = "realty-feed", namespace = "http://webmaster.yandex.ru/schemas/feed/realty/2010-06")
@XmlAccessorType(XmlAccessType.FIELD)
public class RealtyFeed {
    @XmlElement(name = "generation-date")
    @XmlJavaTypeAdapter(InstantAdapter.class)
    private Instant generationDate;
    @XmlElement(name = "offer")
    public List<Offer> offerList;

    public Instant getGenerationDate() {
        return generationDate;
    }

    public void setGenerationDate(Instant generationDate) {
        this.generationDate = generationDate;
    }

    public List<Offer> getOffer() {
        return offerList;
    }

    public void setOffer(List<Offer> offerList) {
        this.offerList= offerList;
    }
}
