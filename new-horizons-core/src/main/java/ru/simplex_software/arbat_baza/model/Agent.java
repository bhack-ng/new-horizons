package ru.simplex_software.arbat_baza.model;

import net.sf.autodao.PersistentEntity;
import org.hibernate.Hibernate;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Риэлтер, Агент агенства недвижимости, пользователь системы.
 */
@Entity
public class Agent implements PersistentEntity<String> {
    private long externalId;
    public enum Role {
        ADMIN, AGENT, OFFICE_MANAGER
    }
    private boolean blocked = false;
    @Id
    private String login;
    @Version
    private int version;

    @ManyToOne
    private Agency agency;
    @NotNull @Size(min=6)
    private String password;
    private String phone;
    private String fio;
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role=Role.AGENT;
    @Lob @Basic(fetch= FetchType.LAZY)
    private byte[] photo;
    private String photoContentType;

    @OneToOne(cascade = CascadeType.ALL)
    private RealtyFilter filter = new RealtyFilter(this);

    @Override
    public String getPrimaryKey() {
        return login;
    }

    public long getExternalId() {
        return externalId;
    }

    public void setExternalId(long externalId) {
        this.externalId = externalId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return photoContentType;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public RealtyFilter getFilter() {
        return filter;
    }

    public void setFilter(RealtyFilter filter) {
        this.filter = filter;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Agency getAgency() {
        return agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }

    @Override
    public int hashCode() {
        if(login==null){
            return 0;
        }else{
            return login.hashCode();
        }
    }

//todo move to superclass
    @Override
    public boolean equals(Object obj) {

        if(obj instanceof PersistentEntity){
            PersistentEntity pe = (PersistentEntity) obj;
            if(login==null) {
                return false;
            }
            if (!Hibernate.getClass(this).equals(Hibernate.getClass(pe))) {
                return false;
            }

            return login.equals(pe.getPrimaryKey());

        }else{
            return false;
        }
    }

    public boolean getBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}
