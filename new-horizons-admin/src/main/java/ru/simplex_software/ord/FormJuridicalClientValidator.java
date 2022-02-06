package ru.simplex_software.ord;

import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import ru.simplex_software.arbat_baza.dao.ClientDAO;
import ru.simplex_software.arbat_baza.model.clients.Client;
import ru.simplex_software.ord.utils.AbstractDataValidator;

import java.util.Map;

/**
 * Валидатор формы юридического лица.
 */
public class FormJuridicalClientValidator extends AbstractDataValidator {

    public FormJuridicalClientValidator(ClientDAO clientDAO, Client client) {
        super(clientDAO, client);
    }

    @Override
    public void validate(ValidationContext context) {
        super.validate(context);

        Map<String, Property> beanProps = context.getProperties(context.getProperty().getBase());

        String name = (String) beanProps.get("name").getValue();
        String shortName = (String) beanProps.get("shortName").getValue();
        String site = (String) beanProps.get("site").getValue();
        String email = (String) beanProps.get("email").getValue();
        String phone = (String) beanProps.get("phone").getValue();
        phone = phone == null ? null : phone.trim();
        String mobilePhone = (String) beanProps.get("mobilePhone").getValue();
        mobilePhone = mobilePhone == null ? null : mobilePhone.trim();
        String inn = (String) beanProps.get("inn").getValue();
        String ogrn = (String) beanProps.get("ogrn").getValue();
        String comment = (String) beanProps.get("comment").getValue();

        validateStringLength("name", name, 255);
        validateStringLength("shortName", shortName, 255);
        validateStringLength("site", site, 255);
        validateEmail("email", email);
        validatePhone("phone", phone, mobilePhone);
        validatePhone("mobilePhone", mobilePhone, phone);
        validateInn("inn", inn);
        validateOgrn("ogrn", ogrn);
        validateStringLength("comment", comment, 1024);
    }
}
