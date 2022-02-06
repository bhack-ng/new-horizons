package ru.simplex_software.arbat_baza.viewmodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;
import ru.simplex_software.arbat_baza.model.RealtyObject;

/**
 * Валидация полей "Этаж" и "Этажность" реализована с помощью constraint
 */
public class YandexValidator extends AbstractValidator {
    private static final Logger LOG = LoggerFactory.getLogger(YandexValidator.class);

    @Override
    public void validate(ValidationContext ctx) {

        String message = "Обязательное поле для Яндекс.Недвижимость";

        RealtyObject realtyObject = (RealtyObject) ctx.getProperty().getValue();
        //если рекламируется и выбран для публикации в Яндекс
        if (((realtyObject.getObjectStatus()==null)||(realtyObject.getObjectStatus().getId()!= 1l))||(!realtyObject.isPublishYandexRealty())) {
            return;
        }

        if (realtyObject.getArea().getTotal() == null) {
            addInvalidMessage(ctx, "area.total", message);

        }

        if (realtyObject.getPrice().getValue() == null) {
            addInvalidMessage(ctx, "price.value", message);
        }

        if (realtyObject.getArea().getRoomsCount()==null){
            addInvalidMessage(ctx, "area.roomsCount", message);
        }
    }

}





