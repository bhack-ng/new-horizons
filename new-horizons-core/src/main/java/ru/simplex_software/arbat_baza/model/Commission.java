package ru.simplex_software.arbat_baza.model;

import ru.simplex_software.zkutils.entity.LongIdPersistentEntity;

import javax.persistence.Entity;

/**
 * Комисии.
 */
@Entity
public class Commission extends LongIdPersistentEntity{
    /**комиссия агента в %.*/
    private Double salesCommission;
    /**комиссия клиента в %.*/
    private Double buyerCommission;
    /**наша комиссия. в %  */
    private Double agencyCommission;

    public Double getSalesCommission() {
        return salesCommission;
    }

    public void setSalesCommission(Double salesCommission) {
        this.salesCommission = salesCommission;
    }

    public Double getBuyerCommission() {
        return buyerCommission;
    }

    public void setBuyerCommission(Double buyerCommission) {
        this.buyerCommission = buyerCommission;
    }

    public Double getAgencyCommission() {
        return agencyCommission;
    }

    public void setAgencyCommission(Double agencyCommission) {
        this.agencyCommission = agencyCommission;
    }
}
