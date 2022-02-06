package ru.simplex_software.arbat_baza.model.clients;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * Юридический клиент.
 */
@Entity
public class JuridicalClient extends Client {

    /**
     * Неофициальное название
     */
    private String shortName;

    /**
     * Сайт
     */
    private String site;

    /**
     * ИНН
     */
    @Column(unique = true)
    private String inn;

    /**
     * ОГРН
     */
    @Column(unique = true)
    private String ogrn;

    /**
     * Сотрудники.
     */
    @OneToMany(mappedBy = "company")
    private List<JobPosition> jobPositions;

    public JuridicalClient() {
    }

    public void addJobPosition(JobPosition jobPosition) {
        jobPositions.add(jobPosition);
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getOgrn() {
        return ogrn;
    }

    public void setOgrn(String ogrn) {
        this.ogrn = ogrn;
    }

    public List<JobPosition> getJobPositions() {
        return jobPositions;
    }

    public void setJobPositions(List<JobPosition> jobPositions) {
        this.jobPositions = jobPositions;
    }
}
