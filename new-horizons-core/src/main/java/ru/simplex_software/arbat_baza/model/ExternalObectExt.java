package ru.simplex_software.arbat_baza.model;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import ru.simplex_software.arbat_baza.model.odor.Feed;
import ru.simplex_software.zkutils.entity.LongIdPersistentEntity;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Дополнительные данные для объектов других агентств скачанных с фида feed.
 */
@Entity
public class ExternalObectExt extends LongIdPersistentEntity{
    /**
     * Фид с которого объект загружен.
     */
    @ManyToOne
    public Feed feed;

    /**
     * Идентификатор в фиде владельце.
     */
    public String externalId;


    /**
     * Время обновления объекта.
     */
    public Instant updated=Instant.now();

    /** Links to photo on owner site.*/
    @ElementCollection() @Cascade(CascadeType.ALL)
    private List<String> externalPhoto =  new ArrayList();

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public List<String> getExternalPhoto() {
        return externalPhoto;
    }

    public void setExternalPhoto(List<String> externalPhoto) {
        this.externalPhoto = externalPhoto;
    }

    public Feed getFeed() {
        return feed;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
    }

    public Instant getUpdated() {
        return updated;
    }

    public void setUpdated(Instant updated) {
        this.updated = updated;
    }
}
