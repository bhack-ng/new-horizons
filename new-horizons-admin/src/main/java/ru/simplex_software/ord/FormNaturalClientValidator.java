package ru.simplex_software.ord;

import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import ru.simplex_software.arbat_baza.dao.ClientDAO;
import ru.simplex_software.arbat_baza.model.clients.Client;
import ru.simplex_software.ord.utils.AbstractDataValidator;

import java.util.Map;

/**
 * Валидатор формы физического лица.
 */
public class FormNaturalClientValidator extends AbstractDataValidator {

    public FormNaturalClientValidator(ClientDAO clientDAO, Client client) {
        super(clientDAO, client);
    }

    @Override
    public void validate(ValidationContext context) {
        super.validate(context);

        Map<String, Property> beanProps = context.getProperties(context.getProperty().getBase());

        String name = (String) beanProps.get("name").getValue();
        String email = (String) beanProps.get("email").getValue();
        String phone = (String) beanProps.get("phone").getValue();
        phone = phone == null ? null : phone.trim();
        String mobilePhone = (String) beanProps.get("mobilePhone").getValue();
        mobilePhone = mobilePhone == null ? null : mobilePhone.trim();
        String comment = (String) beanProps.get("comment").getValue();

        validateStringLength("name", name, 255);
        validateEmail("email", email);
        validatePhone("phone", phone, mobilePhone);
        validatePhone("mobilePhone", mobilePhone, phone);
        validateStringLength("comment", comment, 1024);
    }
}
