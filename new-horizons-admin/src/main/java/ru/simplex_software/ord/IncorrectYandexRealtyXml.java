package ru.simplex_software.ord;

/**
 * Некорректный xml документ формата yandex.Недвижимость.
 */
public class IncorrectYandexRealtyXml extends IllegalArgumentException {
    public IncorrectYandexRealtyXml(String s) {
        super(s);
    }
}
