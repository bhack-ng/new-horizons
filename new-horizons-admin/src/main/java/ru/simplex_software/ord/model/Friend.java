package ru.simplex_software.ord.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplex_software.arbat_baza.model.odor.Feed;

import java.util.ArrayList;
import java.util.List;

/**
 * Рекомендованное агентство.
 */
public class Friend {
    private static final Logger LOG = LoggerFactory.getLogger(Friend.class);
    private String name;
    private String url;
    private List<Feed> feedList=new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Feed> getFeedList() {
        return feedList;
    }

    public void setFeedList(List<Feed> feedList) {
        this.feedList = feedList;
    }
}
