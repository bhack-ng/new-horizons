package ru.simplex_software.arbat_baza.model.live;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplex_software.arbat_baza.model.AbstractOptions;

import javax.persistence.Entity;

/**
 * Options for LiveSaleRealty.
 */
@Entity
public class LiveSaleOptions extends AbstractOptions {
    private static final Logger LOG = LoggerFactory.getLogger(LiveSaleOptions.class);
    /**
     * Положение окон
     */
    private WindowsPosition windowsPosition;
    /**
     * Ипотека
     */
    private Boolean homeMortgage;
    /**
     * Наличие телефона.
     */
    private Boolean  phone;

    public WindowsPosition getWindowsPosition() {
        return windowsPosition;
    }

    public void setWindowsPosition(WindowsPosition windowsPosition) {
        this.windowsPosition = windowsPosition;
    }

    public Boolean getHomeMortgage() {
        return homeMortgage;
    }

    public void setHomeMortgage(Boolean homeMortgage) {
        this.homeMortgage = homeMortgage;
    }

    public Boolean getPhone() {
        return phone;
    }

    public void setPhone(Boolean phone) {
        this.phone = phone;
    }
}
