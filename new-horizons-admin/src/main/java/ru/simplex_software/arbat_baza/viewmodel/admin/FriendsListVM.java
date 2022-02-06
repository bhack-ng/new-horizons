package ru.simplex_software.arbat_baza.viewmodel.admin;

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
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;
import ru.simplex_software.arbat_baza.AuthService;
import ru.simplex_software.arbat_baza.dao.odor.ExternalAgencyDAO;
import ru.simplex_software.arbat_baza.model.odor.ExternalAgency;
import ru.simplex_software.ord.FriendsListParser;
import ru.simplex_software.ord.model.Friend;

import java.util.List;

/**
 * View Model for show agency recommendation.
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class FriendsListVM {
    private static final Logger LOG = LoggerFactory.getLogger(FriendsListVM.class);
    @WireVariable
    private ExternalAgencyDAO externalAgencyDAO;
    @WireVariable
    private AuthService authService;
    private List<Friend> friendList;
    public Window win;
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window view, @ExecutionArgParam("externalAgency") ExternalAgency externalAgency) {
        this.win = view;
        try {
            String friendsUrl;
            if(externalAgency.getSite().endsWith("/")){
                friendsUrl = externalAgency.getSite() + "friends.xml";
            }else{
                friendsUrl = externalAgency.getSite() + "/friends.xml";
            }
            friendList=new FriendsListParser().parse(friendsUrl);
        }catch (Exception e){
            LOG.error(e.getMessage());
        }
    }

    @Command @NotifyChange("*")
    public void addAgency(@BindingParam("friend") Friend friend){
        final ExternalAgency externalAgency = new ExternalAgency();
        externalAgency.setName(friend.getName());
        externalAgency.setSite(friend.getUrl());
        externalAgency.setAgency(authService.getLogginedAgent().getAgency());
        externalAgencyDAO.saveOrUpdate(externalAgency);
        for (int i=0;i<friend.getFeedList().size();i++){
            friend.getFeedList().get(i).setExternalAgency(externalAgency);
        }
        externalAgency.setFeedList(friend.getFeedList());

        authService.getLogginedAgent().getAgency().getExternalAgencyList().add(externalAgency);
        BindUtils.postGlobalCommand(null, null, "refreshExternalAgencyList", null);
    }

    public boolean containAgency(Friend friend){
        return externalAgencyDAO.containAgency(friend.getUrl());
    }

    public List<Friend> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<Friend> friendList) {
        this.friendList = friendList;
    }
}
