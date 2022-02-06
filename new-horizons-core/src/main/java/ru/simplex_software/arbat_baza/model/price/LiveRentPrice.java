package ru.simplex_software.arbat_baza.model.price;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * .
 */
@Entity
public class LiveRentPrice extends RentPrice {
    private static final Logger LOG = LoggerFactory.getLogger(LiveRentPrice.class);
    private Integer prepay;//month
    @Enumerated(value = EnumType.STRING)
    private PayForm payForm=PayForm.ANY;

    private Boolean deposit;

    public Integer getPrepay() {
        return prepay;
    }

    public void setPrepay(Integer prepay) {
        this.prepay = prepay;
    }

    public PayForm getPayForm() {
        return payForm;
    }

    public void setPayForm(PayForm payForm) {
        this.payForm = payForm;
    }

    public Boolean getDeposit() {
        return deposit;
    }

    public void setDeposit(Boolean deposit) {
        this.deposit = deposit;
    }
}
