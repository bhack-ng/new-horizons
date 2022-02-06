package ru.simplex_software.arbat_baza.model.odor;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.jetbrains.annotations.NotNull;
import ru.simplex_software.arbat_baza.model.FeedType;
import ru.simplex_software.zkutils.entity.LongIdPersistentEntity;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * Feed fo check updates.
 */
@Entity
public class Feed extends LongIdPersistentEntity implements Delayed{
    @ManyToOne
    private ExternalAgency externalAgency;
    private String url;
    private String description;
    @ElementCollection (fetch = FetchType.EAGER)@Cascade(CascadeType.ALL)
    private List<String> errors = new ArrayList<String>();
    private Instant lastUpdate ;
    @Enumerated(EnumType.STRING)
    private FeedType feedType;
    /** in minutes.*/
    private int updateInterval=60;
    private long allFeedSizeLimit=1024*1024*16;
    private long offerSizeLimit=1024*16;
    private long offerCountLimit=1024*16;

    private boolean enable=true;

    public Feed() {
    }

    @Override @Transient
    public long getDelay(@NotNull TimeUnit unit) {
        if(unit!= TimeUnit.NANOSECONDS){
            throw new IllegalArgumentException("implemented only for nanoseconds");
        }
        if(lastUpdate==null){
            return -1;
        }
        Duration duration = Duration.between(lastUpdate, Instant.now());
        duration=duration.minusMinutes(updateInterval);

        final long result = -1 * (duration.getSeconds() * 1000 * 1000 * 1000 + duration.getNano());
        return result;
    }

    @Override
    public int compareTo(@NotNull Delayed o) {
        return new Long(getDelay(TimeUnit.NANOSECONDS)).compareTo(o.getDelay(TimeUnit.NANOSECONDS));
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ExternalAgency getExternalAgency() {
        return externalAgency;
    }

    public void setExternalAgency(ExternalAgency externalAgency) {
        this.externalAgency = externalAgency;
    }

    public Instant getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getUpdateInterval() {
        return updateInterval;
    }

    public void setUpdateInterval(int updateInterval) {
        this.updateInterval = updateInterval;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public long getAllFeedSizeLimit() {
        return allFeedSizeLimit;
    }

    public void setAllFeedSizeLimit(long allFeedSizeLimit) {
        this.allFeedSizeLimit = allFeedSizeLimit;
    }

    public long getOfferSizeLimit() {
        return offerSizeLimit;
    }

    public void setOfferSizeLimit(long offerSizeLimit) {
        this.offerSizeLimit = offerSizeLimit;
    }

    public long getOfferCountLimit() {
        return offerCountLimit;
    }

    public void setOfferCountLimit(long offerCountLimit) {
        this.offerCountLimit = offerCountLimit;
    }

    public FeedType getFeedType() {
        return feedType;
    }

    public void setFeedType(FeedType feedType) {
        this.feedType = feedType;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
