package ru.simplex_software.arbat_baza.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplex_software.arbat_baza.model.fias.FiasAddressVector;
import ru.simplex_software.arbat_baza.model.odor.ExternalAgency;
import ru.simplex_software.zkutils.entity.LongIdPersistentEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

/**
 * Агентство недвижимости.
 */

@Entity
public class Agency extends LongIdPersistentEntity{
    private static final Logger LOG = LoggerFactory.getLogger(Agency.class);
    private String phone;
    private String name ;
    @Min(1)
    private int defaultUpdateInterval = 60;
    @OneToMany(mappedBy = "agency" )
    private List<Agent> agentList=new ArrayList<>();
    @OneToMany(mappedBy = "agency" ) @OrderBy("name")//todo make paging for prefomance
    private List<ExternalAgency> externalAgencyList=new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    private FiasAddressVector defaultAddress=new FiasAddressVector();

    @OneToOne
    private DbFile watermark;

    //cайт агенства недвижимости
    //может пригодиться для Яндекс.недвижимости
    private String agencyURL;

    public DbFile getWatermark() {
        return watermark;
    }

    public void setWatermark(DbFile watermark) {
        this.watermark = watermark;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Agent> getAgentList() {
        return agentList;
    }

    public void setAgentList(List<Agent> agentList) {
        this.agentList = agentList;
    }

    public List<ExternalAgency> getExternalAgencyList() {
        return externalAgencyList;
    }

    public void setExternalAgencyList(List<ExternalAgency> externalAgencyList) {
        this.externalAgencyList = externalAgencyList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDefaultUpdateInterval(){
        return defaultUpdateInterval;
    }

    public void setDefaultUpdateInterval(int defaultUpdateInterval){
        this.defaultUpdateInterval = defaultUpdateInterval;
    }

    public FiasAddressVector getDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(FiasAddressVector defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    //геттер и сеттер для agencyURL
    public void setAgencyURL(String url ) { this.agencyURL = url;}
    public String getAgencyURL (){ return agencyURL;}
}
