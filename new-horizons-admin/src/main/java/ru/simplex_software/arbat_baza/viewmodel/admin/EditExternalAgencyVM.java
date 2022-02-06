package ru.simplex_software.arbat_baza.viewmodel.admin;

import org.hibernate.procedure.internal.PostgresCallableStatementSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;
import ru.simplex_software.arbat_baza.AuthService;
import ru.simplex_software.arbat_baza.DeleteOutdatedObjectsService;
import ru.simplex_software.arbat_baza.dao.AgencyDAO;
import ru.simplex_software.arbat_baza.dao.odor.ExternalAgencyDAO;
import ru.simplex_software.arbat_baza.dao.odor.FeedDAO;
import ru.simplex_software.arbat_baza.model.Agency;
import ru.simplex_software.arbat_baza.model.odor.ExternalAgency;
import ru.simplex_software.arbat_baza.model.odor.Feed;
import ru.simplex_software.ord.FeedService;
import ru.simplex_software.ord.FeedsListParser;
import ru.simplex_software.zkutils.DetachableModel;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.DelayQueue;

/**
 * Edit of external agency.
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class EditExternalAgencyVM {
    private static final Logger LOG= LoggerFactory.getLogger(EditExternalAgencyVM.class);
    @WireVariable
    private ExternalAgencyDAO externalAgencyDAO;
    @WireVariable
    private AgencyDAO agencyDAO;
    @WireVariable
    private AuthService authService;
    @WireVariable
    private FeedDAO feedDAO;
    @DetachableModel
    private Feed selectedFeed;
    @WireVariable
    FeedService feedService;
    @WireVariable
    DeleteOutdatedObjectsService deleteOutdatedObjectsService;


    private ExternalAgency externalAgency;

    private Window win;
    @WireVariable
    private DelayQueue<Feed> feedQueue;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window view,
                             @ExecutionArgParam("externalAgency") ExternalAgency externalAgency){
        this.externalAgency=externalAgency;
        win=view;
    }

    @Command
    public void createFeed(){
        final Feed feed = new Feed();
        feed.setExternalAgency(externalAgency);
        HashMap params = new HashMap();
        params.put("feed", feed);
        Component editWin = Executions.createComponents("/WEB-INF/zul/admin/editFeed.zul", win, params);
        editWin.addEventListener("onChangeList", e-> refresh());

    }


    @Command @NotifyChange("*")
    public void createAllFeeds(){
        try {
            final List<Feed> allFeeds = new FeedsListParser().parse(externalAgency);
            allFeeds.forEach(feedDAO::saveOrUpdate);
            allFeeds.forEach(externalAgency.getFeedList()::add);
        } catch (Exception e) {
            LOG.error(e.getMessage(),e);
        }

    }
    @Command
    public void editFeed(@BindingParam("feed") Feed feed){
        HashMap params = new HashMap();
        params.put("feed", feed);
        Component editWin = Executions.createComponents("/WEB-INF/zul/admin/editFeed.zul", win, params);
        editWin.addEventListener("onChangeList", e-> refresh());
    }

    @Command
    public void recommendatons(){
        HashMap params = new HashMap();
        params.put("externalAgency", externalAgency);
        Component editWin = Executions.createComponents("/WEB-INF/zul/admin/friendList.zul", win, params);
    }

    @Command
    public void editSelectedFeed(){
        HashMap params = new HashMap();
        params.put("feed", selectedFeed);
        Component editWin = Executions.createComponents("/WEB-INF/zul/admin/editFeed.zul", win, params);
        editWin.addEventListener("onChangeList", e-> refresh());
    }

    @Command
    public void deleteSelectedFeed(){
        externalAgency = selectedFeed.getExternalAgency();
        if(externalAgency!=null){

            externalAgency.getFeedList().remove(selectedFeed);
            BindUtils.postNotifyChange(null,null,externalAgency,"feedList");

        }

        feedService.update(selectedFeed);
        deleteOutdatedObjectsService.deleteOutdated(selectedFeed);
        feedDAO.delete(selectedFeed);
        feedQueue.remove(selectedFeed);
        BindUtils.postGlobalCommand(null,null,"refresh",null);
    }

    @Command
    public void openFeedMessageLog(@BindingParam("feed")Feed feed){
        HashMap params = new HashMap();
        feed = feedService.refresh(feed);
        params.put("errors", feed.getErrors());
        Executions.createComponents("/WEB-INF/zul/admin/feedMessageLog.zul", null, params);
    }

    @Command
    private void toggleSelectedFeed(){
        selectedFeed.setEnable(!selectedFeed.isEnable());
        if(selectedFeed.isEnable()){
            feedQueue.add(selectedFeed);
        }else{
            feedQueue.remove(selectedFeed);
        }
        Events.postEvent("onChangeList", win, null);
    }





    private void refresh() {
        externalAgency=externalAgencyDAO.get(externalAgency.getId());
        BindUtils.postNotifyChange(null,null, this,"*");
        Events.postEvent("onChangeList", win, null);

    }

    @Command
    public void save(){
        if(externalAgency.getId()==null){
            externalAgencyDAO.saveOrUpdate(externalAgency);
        }else{
            externalAgencyDAO.merge(externalAgency);
        }
        final Agency agency = authService.getLogginedAgent().getAgency();
        if(!agency.getExternalAgencyList().contains(externalAgency)){//performance check
            agency.getExternalAgencyList().add(externalAgency);
        }
        agencyDAO.saveOrUpdate(agency);
        Events.postEvent("onChangeList", win, null);
        win.detach();
    }

    @Command
    public void close(){
        Events.postEvent("onChangeList", win, null);
        win.detach();
    }
    public ExternalAgency getExternalAgency() {
        return externalAgency;
    }

    public Feed getSelectedFeed() {
        return selectedFeed;
    }

    public void setSelectedFeed(Feed selectedFeed) {
        this.selectedFeed = selectedFeed;
    }
}
