package ru.simplex_software.ord.utils;

import org.springframework.beans.factory.annotation.Autowired;
import ru.simplex_software.arbat_baza.dao.FloorSchemeDAO;
import ru.simplex_software.arbat_baza.dao.PhotoDAO;
import ru.simplex_software.arbat_baza.model.FloorScheme;
import ru.simplex_software.arbat_baza.model.ObjectStatus;
import ru.simplex_software.arbat_baza.model.RealtyObject;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.text.NumberFormat;
import java.util.List;
import java.util.Set;

public class JsonBuilder {

    @Autowired
    private FloorSchemeDAO floorSchemeDAO;

    @Autowired
    private PhotoDAO photoDAO;

    /**
     * Получение JSON по объектам на схеме.
     */
    public String buildFloorSchemeJson(FloorScheme floorScheme) {
        return getFloorSchemeBuilder(floorScheme).build().toString();
    }

    /**
     * Получение JSON из нескольких схем.
     */
    public String buildFloorSchemesJson(String isString) {

        // Получение Id.
        String[] strings = isString.split("\\|");

        /** Массив схем.
         * [{"imageId": "123"}, {"imageId": "234"}, {"imageId": "345"}, ...] */
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        for (String string : strings) {
            long id = Long.parseLong(string);
            FloorScheme scheme = floorSchemeDAO.get(id);

            JsonObjectBuilder objectBuilder = (scheme == null) ?
                    Json.createObjectBuilder() : getFloorSchemeBuilder(scheme);
            arrayBuilder.add(objectBuilder);
        }

        // Результат.
        return arrayBuilder.build().toString();
    }

    /**
     * Получение JSON для всех схем.
     */
    public String buildAllFloorSchemesJson() {

        // Получение схем.
        List<FloorScheme> schemes = floorSchemeDAO.findAll();

        /** Массив схем.
         * [{"imageId": "123"}, {"imageId": "234"}, {"imageId": "345"}, ...] */
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        for (FloorScheme scheme : schemes) {
            JsonObjectBuilder objectBuilder = (scheme == null) ?
                    Json.createObjectBuilder() : getFloorSchemeBuilder(scheme);
            arrayBuilder.add(objectBuilder);
        }

        return arrayBuilder.build().toString();
    }

    /**
     * Получение JSON builder по объектам на схеме.
     */
    private JsonObjectBuilder getFloorSchemeBuilder(FloorScheme scheme) {
        // Получение объектов.
        Set<RealtyObject> realtyObjects = scheme.getRealtyObjects();

        /** JSON объект: {"imageId:" "190", "items:" [...]} */
        JsonObjectBuilder builder = Json.createObjectBuilder();

        // Добавление id изображения.
        builder.add("imageId", scheme.getData().getId());

        // Добавление названия изображения.
        builder.add("imageName", scheme.getName());

        /** Массив элементов: [{"number:" 123, ...}, {"number:" 234, ...}, ...] */
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        for (RealtyObject object : realtyObjects) {

            /** Элемент: {"number: 123, "free:" true, "coords:" [...]"} */
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

            // Добавление номера.
            String address = object.getAddress().getKvartira();
            objectBuilder.add("number", (address == null) ? "" : address);

            // Добавление статуса.
            ObjectStatus status = object.getObjectStatus();
            objectBuilder.add("free", status == null || status.getId() <= 2);

            // Добавление площади.
            String areaString = "";
            Double area = object.getArea().getTotal();
            if (area != null) {
                NumberFormat numberFormat = NumberFormat.getInstance();
                areaString = numberFormat.format(area);
            }
            objectBuilder.add("area", areaString);

            // Добавление массива фотографий.
            JsonArrayBuilder photoBuilder = Json.createArrayBuilder();
            List<Long> photoIds = photoDAO.findAdvertiseIdsByRealtyObject(object);
            photoIds.forEach(photoBuilder::add);
            objectBuilder.add("photos", photoBuilder);

            // Добавление текста объявления.
            objectBuilder.add("offerText", object.getNote() == null ? "" : object.getNote());

            /** Добавление массива координат:
             * [[x1,y1], [x2, y2], ... [xn, yn]] */
            JsonArrayBuilder coordBuilder = Json.createArrayBuilder();
            if (object.getFloorSchemeCoordinates() != null) {
                String[] data = object.getFloorSchemeCoordinates().split(",");
                for (int i = 0; i < data.length; i += 2) {
                    int x = Integer.parseInt(data[i]);
                    int y = Integer.parseInt(data[i + 1]);
                    coordBuilder.add(Json.createArrayBuilder().add(x).add(y));
                }
            }
            objectBuilder.add("coords", coordBuilder);
            arrBuilder.add(objectBuilder);
        }
        builder.add("items", arrBuilder);

        return builder;
    }
}
