package ru.simplex_software.school_mark.view;

import org.zkoss.bind.BindContext;
import org.zkoss.bind.Converter;
import org.zkoss.zul.Datebox;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * .
 */
public class Jsr310Converter implements Converter<Date, LocalDate, Datebox> {
    @Override
    public Date coerceToUi(LocalDate beanProp, Datebox component, BindContext ctx) {
        if(beanProp==null) return null;
        Date date = Date.from(beanProp.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return date;
    }

    @Override
    public LocalDate coerceToBean(Date compAttr, Datebox component, BindContext ctx) {
        LocalDate date = compAttr.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return date;
    }
}
