package ru.simplex_software.ord;

import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

import java.util.Date;
import java.util.Map;

/**
 * Валидатор формы управления задачей.
 */
public class FormClientTasksValidator extends AbstractValidator {

    @Override
    public void validate(ValidationContext context) {
        Map<String, Property> beanProps = context.getProperties(context.getProperty().getBase());

        Date date = (Date) beanProps.get("executionDatetime").getValue();
        String description = (String) beanProps.get("description").getValue();

        // Валидация даты.
        if (date.before(new Date())) {
            this.addInvalidMessage(context, "executionDatetime", "Нужно указать " +
                    "будущие дату и время.");
        }

        // Валидация описания задачи.
        if (description != null && description.length() > 1024) {
            this.addInvalidMessage(context, "description", "Длина описания не " +
                    "должна превышать 1024 символа.");
        }
    }
}
