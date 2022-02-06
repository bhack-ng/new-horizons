package ru.simplex_software.arbat_baza.model.commerce;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.arbat_baza.model.price.CommercePrice;
import ru.simplex_software.arbat_baza.model.price.SimplePrice;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * Коммерческий объект недвижимости .
 */
@Entity
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE) @Cacheable
public class Commerce extends RealtyObject {
    /**
     * use RealtyObject.create()
     */
    public Commerce() {
        setArea(new CommerceArea());
        setPrice(new CommercePrice());
        setOptions(new CommerceOptions());
        setBuilding(new CommerceBuilding());
    }

    @Enumerated(EnumType.STRING)
    private CommerceType type;





    @Enumerated(value = EnumType.STRING)
    private ContractType contractType;


    private String special_offer_id;//Уникальный идентификатор акции cian, в которой участвует объект



    public ContractType getContractType() {
        return contractType;
    }

    public void setContractType(ContractType contractType) {
        this.contractType = contractType;
    }



    public String getSpecial_offer_id() {
        return special_offer_id;
    }

    public void setSpecial_offer_id(String special_offer_id) {
        this.special_offer_id = special_offer_id;
    }


    @Override
    public CommercePrice getPrice() {
        return (CommercePrice) super.getPrice();
    }

    public CommerceType getType() {
        return type;
    }

    public void setType(CommerceType type) {
        this.type = type;
    }


    public CommerceOptions getOptions() {
        return (CommerceOptions)super.getOptions();
    }

    @Override
    public CommerceBuilding getBuilding() {
        return (CommerceBuilding)super.getBuilding();
    }
}
