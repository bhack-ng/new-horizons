package ru.simplex_software.arbat_baza.viewmodel;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.util.Clients;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.arbat_baza.model.commerce.Commerce;
import ru.simplex_software.arbat_baza.model.price.CommercePrice;
import ru.simplex_software.arbat_baza.model.price.Currency;
import ru.simplex_software.arbat_baza.model.price.Period;
import ru.simplex_software.zkutils.PropConverter;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class RentPriceCalculatorVM {
    private static final Logger LOG = LoggerFactory.getLogger(RentPriceCalculatorVM.class);
    private Commerce realtyObject;

    public void setRealtyObject(Commerce realtyObject) {
        this.realtyObject = realtyObject;
    }

    @AfterCompose
    public void afterCompose(@ExecutionArgParam("realtyObject") RealtyObject realtyObject) {
        this.realtyObject = (Commerce) realtyObject;
    }

    public String getMonthPrice() {
        String countResults = "";
        Period period = realtyObject.getPrice().getPeriod();
        if (period.equals((realtyObject.getPrice()).getPeriod())) {
            countResults = realtyObject.getPrice().getValue() + " " + realtyObject.getPrice().getCurrency();
        }
        if (period.equals((realtyObject.getPrice()).getPeriod())) {
            countResults = realtyObject.getPrice().getValue() * realtyObject.getArea().getTotal() / 12 + " " + realtyObject.getPrice().getCurrency();
        }
        return countResults;
    }

    public String getM2YearPrice() {
        String countResults = "";
        Period period = realtyObject.getPrice().getPeriod();
        if (period.equals(Period.MONTH)) {
            countResults = realtyObject.getPrice().getValue() * 12 / realtyObject.getArea().getTotal() + " " + realtyObject.getPrice().getCurrency();
        }
        if (period.equals(Period.M2_PER_YEAR)) {
            countResults = realtyObject.getPrice().getValue() + " " + realtyObject.getPrice().getCurrency();
        }
        return countResults;
    }


}
