package ru.simplex_software.ord.utils;

public class RealtyObjectTypeUtils {

    /**
     * Получение типа недвижимости.
     */
    public static String getRealtyObjectType(String rawType) {

        String data = rawType.split("_")[0];

        switch (data) {
            case "COMMERCE":
                return "Коммерция";
            case "LIVE":
                return "Жилая";
            case "STEAD":
                return "Загородный дом";
            case "PRIVATE":
                return "Частный дом";
            default:
                return "ОШИБКА!";
        }
    }

    /**
     * Получение типа объявления.
     */
    public static String getOfferType(String rawType) {
        String[] strings = rawType.split("_");
        String data = strings[strings.length - 1];

        switch (data) {
            case "SALE":
                return "Продажа";
            case "LEASE":
            case "RENT":
                return "Аренда";
            default:
                return "ОШИБКА!";
        }
    }
}
