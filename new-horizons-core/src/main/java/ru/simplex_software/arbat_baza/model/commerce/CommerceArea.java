package ru.simplex_software.arbat_baza.model.commerce;

import ru.simplex_software.arbat_baza.AbstractArea;

import javax.persistence.Entity;

/**
 * Информация о площади объекта.
 */
@Entity
public class CommerceArea extends AbstractArea {
    /**
     * Минимальная площадь (если сдается по частям).
     */
    private Double min;

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }
}
