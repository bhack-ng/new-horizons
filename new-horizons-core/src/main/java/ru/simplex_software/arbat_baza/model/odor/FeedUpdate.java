package ru.simplex_software.arbat_baza.model.odor;

import java.time.Instant;

/**
 * Update of field
 */
public class FeedUpdate {

    private Feed feed;

    private Instant updateStartTime = Instant.now() ;
    private Instant updateEndTime = Instant.now() ;

    public Instant getUpdateEndTime() {
        return updateEndTime;
    }

    public void setUpdateEndTime(Instant updateEndTime) {
        this.updateEndTime = updateEndTime;
    }

    public Instant getUpdateStartTime() {
        return updateStartTime;
    }

    public void setUpdateStartTime(Instant updateStartTime) {
        this.updateStartTime = updateStartTime;
    }
}
