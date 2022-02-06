package ru.simplex_software.arbat_baza.model.fias;

import net.sf.autodao.PersistentEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.Comparator;

/**
 * Адексный объект. Из таблицы AS_ADDROBJ.
 */
@Entity @Cache(usage= CacheConcurrencyStrategy.NONSTRICT_READ_WRITE) @Cacheable
public class FiasObject implements PersistentEntity<String> {
    @Index(name = "AOGUID_idx")

    private Double longitude;
    private Double latitude;
    private String AOGUID;
    private String FORMALNAME;
    @Length(max = 2)
    private String REGIONCODE;
    @Basic(fetch = FetchType.LAZY)
    private String AUTOCODE;
    @Basic(fetch = FetchType.LAZY)
    private String AREACODE;
    @Basic(fetch = FetchType.LAZY)
    private String CITYCODE;
    @Basic(fetch = FetchType.LAZY)
    private String CTARCODE;
    @Basic(fetch = FetchType.LAZY)
    private String PLACECODE;
    @Basic(fetch = FetchType.LAZY)
    private String PLANCODE;
    @Basic(fetch = FetchType.LAZY)
    private String STREETCODE;
    @Basic(fetch = FetchType.LAZY)
    private String EXTRCODE;
    @Basic(fetch = FetchType.LAZY)
    private String SEXTCODE;

    private String OFFNAME;
    @Basic(fetch = FetchType.LAZY)
    private String POSTALCODE;
    @Basic(fetch = FetchType.LAZY)
    private String IFNSFL;
    @Basic(fetch = FetchType.LAZY)
    private String TERRIFNSFL;
    @Basic(fetch = FetchType.LAZY)
    private String IFNSUL;
    @Basic(fetch = FetchType.LAZY)
    private String TERRIFNSUL;
    @Basic(fetch = FetchType.LAZY)
    private String OKATO;
    @Basic(fetch = FetchType.LAZY)
    private String OKTMO;
    @Basic(fetch = FetchType.LAZY)
    private LocalDate UPDATEDATE;

    private String SHORTNAME;
    @Index(name = "AOLEVEL_idx")
    private int AOLEVEL;
    @Index(name = "PARENTGUID_idx")
    private String PARENTGUID;
    @Id
    private String AOID;
    private String PREVID;
    private String NEXTID;
    @Basic(fetch = FetchType.LAZY)
    private String CODE;
    @Basic(fetch = FetchType.LAZY)
    private String PLAINCODE;
    private int ACTSTATUS;
    private int LIVESTATUS;
    private int CENTSTATUS;
    private int OPERSTATUS;
    private int CURRSTATUS;
    private LocalDate STARTDATE;
    private LocalDate  ENDDATE;
    private String NORMDOC;
    private String CADNUM;
    private String DIVTYPE;

    @Override
    public String getPrimaryKey() {
        return AOID;
    }

    public int getACTSTATUS() {
        return ACTSTATUS;
    }

    public void setACTSTATUS(int ACTSTATUS) {
        this.ACTSTATUS = ACTSTATUS;
    }


    public String getAOGUID() {
        return AOGUID;
    }

    public void setAOGUID(String AOGUID) {
        this.AOGUID = AOGUID;
    }

    public String getAOID() {
        return AOID;
    }

    public void setAOID(String AOID) {
        this.AOID = AOID;
    }

    public int getAOLEVEL() {
        return AOLEVEL;
    }

    public void setAOLEVEL(int AOLEVEL) {
        this.AOLEVEL = AOLEVEL;
    }

    public String getAREACODE() {
        return AREACODE;
    }

    public void setAREACODE(String AREACODE) {
        this.AREACODE = AREACODE;
    }

    public String getAUTOCODE() {
        return AUTOCODE;
    }

    public void setAUTOCODE(String AUTOCODE) {
        this.AUTOCODE = AUTOCODE;
    }

    public String getCADNUM() {
        return CADNUM;
    }

    public void setCADNUM(String CADNUM) {
        this.CADNUM = CADNUM;
    }

    public int getCENTSTATUS() {
        return CENTSTATUS;
    }

    public void setCENTSTATUS(int CENTSTATUS) {
        this.CENTSTATUS = CENTSTATUS;
    }

    public String getCITYCODE() {
        return CITYCODE;
    }

    public void setCITYCODE(String CITYCODE) {
        this.CITYCODE = CITYCODE;
    }

    public String getCODE() {
        return CODE;
    }

    public void setCODE(String CODE) {
        this.CODE = CODE;
    }

    public String getCTARCODE() {
        return CTARCODE;
    }

    public void setCTARCODE(String CTARCODE) {
        this.CTARCODE = CTARCODE;
    }

    public int getCURRSTATUS() {
        return CURRSTATUS;
    }

    public void setCURRSTATUS(int CURRSTATUS) {
        this.CURRSTATUS = CURRSTATUS;
    }

    public String getDIVTYPE() {
        return DIVTYPE;
    }

    public void setDIVTYPE(String DIVTYPE) {
        this.DIVTYPE = DIVTYPE;
    }


    public String getEXTRCODE() {
        return EXTRCODE;
    }

    public void setEXTRCODE(String EXTRCODE) {
        this.EXTRCODE = EXTRCODE;
    }

    public String getFORMALNAME() {
        return FORMALNAME;
    }

    public void setFORMALNAME(String FORMALNAME) {
        this.FORMALNAME = FORMALNAME;
    }

    public String getIFNSFL() {
        return IFNSFL;
    }

    public void setIFNSFL(String IFNSFL) {
        this.IFNSFL = IFNSFL;
    }

    public String getIFNSUL() {
        return IFNSUL;
    }

    public void setIFNSUL(String IFNSUL) {
        this.IFNSUL = IFNSUL;
    }

    public int getLIVESTATUS() {
        return LIVESTATUS;
    }

    public void setLIVESTATUS(int LIVESTATUS) {
        this.LIVESTATUS = LIVESTATUS;
    }

    public String getNEXTID() {
        return NEXTID;
    }

    public void setNEXTID(String NEXTID) {
        this.NEXTID = NEXTID;
    }

    public String getNORMDOC() {
        return NORMDOC;
    }

    public void setNORMDOC(String NORMDOC) {
        this.NORMDOC = NORMDOC;
    }

    public String getOFFNAME() {
        return OFFNAME;
    }

    public void setOFFNAME(String OFFNAME) {
        this.OFFNAME = OFFNAME;
    }

    public String getOKATO() {
        return OKATO;
    }

    public void setOKATO(String OKATO) {
        this.OKATO = OKATO;
    }

    public String getOKTMO() {
        return OKTMO;
    }

    public void setOKTMO(String OKTMO) {
        this.OKTMO = OKTMO;
    }

    public int getOPERSTATUS() {
        return OPERSTATUS;
    }

    public void setOPERSTATUS(int OPERSTATUS) {
        this.OPERSTATUS = OPERSTATUS;
    }

    public String getPARENTGUID() {
        return PARENTGUID;
    }

    public void setPARENTGUID(String PARENTGUID) {
        this.PARENTGUID = PARENTGUID;
    }

    public String getPLACECODE() {
        return PLACECODE;
    }

    public void setPLACECODE(String PLACECODE) {
        this.PLACECODE = PLACECODE;
    }

    public String getPLAINCODE() {
        return PLAINCODE;
    }

    public void setPLAINCODE(String PLAINCODE) {
        this.PLAINCODE = PLAINCODE;
    }

    public String getPLANCODE() {
        return PLANCODE;
    }

    public void setPLANCODE(String PLANCODE) {
        this.PLANCODE = PLANCODE;
    }

    public String getPOSTALCODE() {
        return POSTALCODE;
    }

    public void setPOSTALCODE(String POSTALCODE) {
        this.POSTALCODE = POSTALCODE;
    }

    public String getPREVID() {
        return PREVID;
    }

    public void setPREVID(String PREVID) {
        this.PREVID = PREVID;
    }

    public String getREGIONCODE() {
        return REGIONCODE;
    }

    public void setREGIONCODE(String REGIONCODE) {
        this.REGIONCODE = REGIONCODE;
    }

    public String getSEXTCODE() {
        return SEXTCODE;
    }

    public void setSEXTCODE(String SEXTCODE) {
        this.SEXTCODE = SEXTCODE;
    }

    public String getSHORTNAME() {
        return SHORTNAME;
    }

    public void setSHORTNAME(String SHORTNAME) {
        this.SHORTNAME = SHORTNAME;
    }


    public String getSTREETCODE() {
        return STREETCODE;
    }

    public void setSTREETCODE(String STREETCODE) {
        this.STREETCODE = STREETCODE;
    }

    public String getTERRIFNSFL() {
        return TERRIFNSFL;
    }

    public void setTERRIFNSFL(String TERRIFNSFL) {
        this.TERRIFNSFL = TERRIFNSFL;
    }

    public String getTERRIFNSUL() {
        return TERRIFNSUL;
    }

    public void setTERRIFNSUL(String TERRIFNSUL) {
        this.TERRIFNSUL = TERRIFNSUL;
    }

    public LocalDate getENDDATE() {
        return ENDDATE;
    }

    public void setENDDATE(LocalDate ENDDATE) {
        this.ENDDATE = ENDDATE;
    }

    public LocalDate getSTARTDATE() {
        return STARTDATE;
    }

    public void setSTARTDATE(LocalDate STARTDATE) {
        this.STARTDATE = STARTDATE;
    }

    public LocalDate getUPDATEDATE() {
        return UPDATEDATE;
    }

    public void setUPDATEDATE(LocalDate UPDATEDATE) {
        this.UPDATEDATE = UPDATEDATE;
    }

    @Override
    public String toString() {
        return "FiasObject{" +
                "ACTSTATUS=" + ACTSTATUS +
                ", AOGUID='" + AOGUID + '\'' +
                ", FORMALNAME='" + FORMALNAME + '\'' +
                ", REGIONCODE='" + REGIONCODE + '\'' +
                ", AUTOCODE='" + AUTOCODE + '\'' +
                ", AREACODE='" + AREACODE + '\'' +
                ", CITYCODE='" + CITYCODE + '\'' +
                ", CTARCODE='" + CTARCODE + '\'' +
                ", PLACECODE='" + PLACECODE + '\'' +
                ", PLANCODE='" + PLANCODE + '\'' +
                ", STREETCODE='" + STREETCODE + '\'' +
                ", EXTRCODE='" + EXTRCODE + '\'' +
                ", SEXTCODE='" + SEXTCODE + '\'' +
                ", OFFNAME='" + OFFNAME + '\'' +
                ", POSTALCODE='" + POSTALCODE + '\'' +
                ", IFNSFL='" + IFNSFL + '\'' +
                ", TERRIFNSFL='" + TERRIFNSFL + '\'' +
                ", IFNSUL='" + IFNSUL + '\'' +
                ", TERRIFNSUL='" + TERRIFNSUL + '\'' +
                ", OKATO='" + OKATO + '\'' +
                ", OKTMO='" + OKTMO + '\'' +
                ", UPDATEDATE=" + UPDATEDATE +
                ", SHORTNAME='" + SHORTNAME + '\'' +
                ", AOLEVEL=" + AOLEVEL +
                ", PARENTGUID='" + PARENTGUID + '\'' +
                ", AOID='" + AOID + '\'' +
                ", PREVID='" + PREVID + '\'' +
                ", NEXTID='" + NEXTID + '\'' +
                ", CODE='" + CODE + '\'' +
                ", PLAINCODE='" + PLAINCODE + '\'' +
                ", LIVESTATUS=" + LIVESTATUS +
                ", CENTSTATUS=" + CENTSTATUS +
                ", OPERSTATUS=" + OPERSTATUS +
                ", CURRSTATUS=" + CURRSTATUS +
                ", STARTDATE=" + STARTDATE +
                ", ENDDATE=" + ENDDATE +
                ", NORMDOC='" + NORMDOC + '\'' +
                ", CADNUM='" + CADNUM + '\'' +
                ", DIVTYPE='" + DIVTYPE + '\'' +
                '}';
    }
    public String toShortString(){
        return FORMALNAME +" "+ SHORTNAME;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

}
