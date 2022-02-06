package ru.simplex_software.arbat_baza.viewmodel.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.Converter;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;
import ru.simplex_software.arbat_baza.EnumConverter;
import ru.simplex_software.arbat_baza.dao.odor.FeedDAO;
import ru.simplex_software.arbat_baza.model.FeedType;
import ru.simplex_software.arbat_baza.model.odor.ExternalAgency;
import ru.simplex_software.arbat_baza.model.odor.Feed;
import ru.simplex_software.ord.FeedManager;
import ru.simplex_software.zkutils.DetachableModel;

import java.util.concurrent.DelayQueue;

/**
 * Редактирование фида.
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class EditFeedVM {
    private static final Logger LOG= LoggerFactory.getLogger(ExternalAgencyListVM.class);

    private Feed feed;
    @WireVariable
    private DelayQueue<Feed> feedQueue;
    @DetachableModel
    private ExternalAgency externalAgency;
    Converter enumFromModelConverter = new EnumConverter("ru.simplex_software.arbat_baza.model");

    @WireVariable
    private FeedDAO feedDAO;

    @WireVariable
    private FeedManager feedManager;

    private FeedType[] feedTypes=FeedType.values();

    private Window win;
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window view, @ExecutionArgParam("feed") Feed feed ){
        this.feed=feed;
        externalAgency=feed.getExternalAgency();
        win=view;
    }

    public Feed getFeed() {
        return feed;
    }

    @Command
    public void save(){
        if(externalAgency!=null && !externalAgency.getFeedList().contains(feed)){
            externalAgency.getFeedList().add(feed);
        }
        else {
            feedQueue.remove(feed);
        }
        feedManager.runUpdateFeed(feed);

        Events.postEvent("onChangeList", win, null);
        win.detach();
    }

    @Command
    public void runUpdate(){
        feedManager.runUpdateFeed(feed);
    }

    public FeedType[] getFeedTypes() {
        return feedTypes;
    }

    public Converter getEnumFromModelConverter() {
        return enumFromModelConverter;
    }

}
