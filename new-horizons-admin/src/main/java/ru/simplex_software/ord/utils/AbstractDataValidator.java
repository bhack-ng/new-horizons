package ru.simplex_software.ord.utils;

import org.springframework.util.StringUtils;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;
import ru.simplex_software.arbat_baza.dao.ClientDAO;
import ru.simplex_software.arbat_baza.model.clients.Client;

public class AbstractDataValidator extends AbstractValidator {

    private static final String EMAIL_REGEXP = ".+@.+\\.[a-z]+";
    private static final String PHONE_REGEXP = "(\\+7|8)\\(?\\d{3}\\)?\\d{3}\\-?\\d{2}\\-?\\d{2}";
    private static final String INN_REGEXP = "\\d{10}";
    private static final String OGRN_REGEXP = "\\d{13}";

    private ValidationContext context;
    private ClientDAO clientDAO;
    private long clientId;

    public AbstractDataValidator(ClientDAO clientDAO, Client client) {
        this.clientId = (client.getId() == null) ? -1 : client.getId();
        this.clientDAO = clientDAO;
    }

    @Override
    public void validate(ValidationContext context) {
        this.context = context;
    }

    public void validateStringLength(String key, String string, int maxLength) {
        if (string != null) {
            if (string.length() > maxLength) {
                addInvalidMessage(context, key, "Длина поля не должна" +
                        " превышать " + maxLength + " символов.");
            }
        }
    }

    /**
     * Валидация почты.
     */
    public void validateEmail(String key, String email) {
        if (email != null) {
            if (!email.matches(EMAIL_REGEXP)) {
                addInvalidMessage(context, key, "Некорректный email.");
            } else if (clientDAO.hasDuplicateByEmail(email, clientId)) {
                addInvalidMessage(context, key, "Email занят.");
            }
        }
    }

    /**
     * Валидация номера телефона.
     */
    public void validatePhone(String key, String phone, String otherPhone) {
        if (StringUtils.hasLength(phone)) {
            if (phone.equals(otherPhone)) {
                addInvalidMessage(context, key, "Телефоны не должны совпадать");
            } else if (!phone.matches(PHONE_REGEXP)) {
                addInvalidMessage(context, key, "Некорректный телефон.");
            } else if (clientDAO.hasDuplicateByPhone(phone, clientId)) {
                addInvalidMessage(context, key, "Телефон занят.");
            }
        }
    }

    /**
     * Валидация ИНН.
     */
    public void validateInn(String key, String inn) {
        if (inn != null) {
            if (!inn.matches(INN_REGEXP)) {
                addInvalidMessage(context, key, "Некорректный ИНН.");
            } else if (clientDAO.hasDuplicateByINN(inn, clientId)) {
                addInvalidMessage(context, key, "ИНН занят.");
            }
        }
    }

    /**
     * Валидация ОГРН.
     */
    public void validateOgrn(String key, String ogrn) {
        if (ogrn != null) {
            if (!isOgrnString(ogrn)) {
                addInvalidMessage(context, key, "Некорректный ОГРН.");
            } else if (clientDAO.hasDuplicateByOGRN(ogrn, clientId)) {
                addInvalidMessage(context, key, "ОГРН занят.");
            }
        }
    }

    /**
     * Проверка строки на ОГРН.
     * 13-знак: контрольное число: младший разряд остатка от деления
     * предыдущего 12-значного числа на 11, если остаток от деления равен 10,
     * то контрольное число равно 0.
     */
    private boolean isOgrnString(String ogrn) {
        if (ogrn.matches(OGRN_REGEXP)) {

            long number = Long.parseLong(ogrn.substring(0, 12)) % 11;

            int control = (number == 10) ? 0 : (int) number % 10;

            int lastDigit = Character.getNumericValue(ogrn.charAt(12));

            return lastDigit == control;
        } else {
            return false;
        }
    }
}
