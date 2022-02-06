package ru.simplex_software.ord;

import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;
import ru.simplex_software.arbat_baza.dao.RealtyObjectDAO;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.arbat_baza.model.commerce.Commerce;

import java.util.Map;

public class FormGenerateOfficesValidator extends AbstractValidator {

    private RealtyObjectDAO realtyObjectDAO;

    public FormGenerateOfficesValidator(RealtyObjectDAO realtyObjectDAO) {
        this.realtyObjectDAO = realtyObjectDAO;
    }

    @Override
    public void validate(ValidationContext context) {

        Map<String, Property> beanProps = context.getProperties(context.getProperty().getBase());

        long id = (long) beanProps.get("id").getValue();
        int minNumber = (int) beanProps.get("minNumber").getValue();
        int maxNumber = (int) beanProps.get("maxNumber").getValue();

        validateId(context, id);
        validateNumbers(context, minNumber, maxNumber);
    }

    public void validateId(ValidationContext context, long id) {
        RealtyObject object = realtyObjectDAO.get(id);

        if (object == null) {
            this.addInvalidMessage(context, "id", "Объект не найден.");
        } else if (!(object instanceof Commerce)) {
            this.addInvalidMessage(context, "id", "Некорректный объект.");
        }
    }

    public void validateNumbers(ValidationContext context, int min, int max) {

        if (min < 0) {
            this.addInvalidMessage(context, "minNumber", "Минимальный номер не может быть меньше нуля.");
        }
        if (min > max) {
            this.addInvalidMessage(context, "minNumber", "Минимальный номер не может быть больше максимального.");
        }
        if (max < min) {
            this.addInvalidMessage(context, "maxNumber", "Максимальный номер не может быть иеньше минимального.");
        }
    }
}
