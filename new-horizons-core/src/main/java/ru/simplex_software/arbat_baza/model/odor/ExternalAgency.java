package ru.simplex_software.arbat_baza.model.odor;

import org.springframework.util.StringUtils;
import ru.simplex_software.arbat_baza.model.Agency;
import ru.simplex_software.zkutils.entity.LongIdPersistentEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.util.ArrayList;
import java.util.List;

/**
 * Внешние агентства для синхронизации.
 */
@Entity
public class ExternalAgency extends LongIdPersistentEntity{
    private String name;
    /**
     * Link to installation of  anothe instance of this program.
     * Setter contain logic: add 'http' to begin and '/' at end.
     *
     */
    private String site;
    private boolean enable;
    private boolean friend=true;
    @OneToMany(mappedBy = "externalAgency" , cascade = CascadeType.ALL) @OrderBy("description")
    private List<Feed> feedList= new ArrayList<>();
    @ManyToOne
    private Agency agency;
    /** Период обновления фидов по умолчанию.*/
    private int updatePeriod = 60;

    public int getFeedCount(){
        return feedList.size();
    }

    public Agency getAgency() {
        return agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public List<Feed> getFeedList() {
        return feedList;
    }

    public void setFeedList(List<Feed> feedList) {
        this.feedList = feedList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        if(StringUtils.hasLength(site)){
            if(!site.endsWith("/")){
                site=site+'/';
            }
            if(!site.startsWith("http")){
                site="http://"+site;
            }
        }

        this.site = site;
    }

    public int getUpdatePeriod() {
        return updatePeriod;
    }

    public void setUpdatePeriod(int updatePeriod) {
        this.updatePeriod = updatePeriod;
    }

    public boolean isFriend() {
        return friend;
    }

    public void setFriend(boolean friend) {
        this.friend = friend;
    }
}
