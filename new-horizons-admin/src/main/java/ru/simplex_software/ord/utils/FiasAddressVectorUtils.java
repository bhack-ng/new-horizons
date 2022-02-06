package ru.simplex_software.ord.utils;

import ru.simplex_software.arbat_baza.dao.fias.FiasObjectDAO;
import ru.simplex_software.arbat_baza.model.fias.FiasObject;

import javax.annotation.Resource;

public class FiasAddressVectorUtils {

    @Resource
    private FiasObjectDAO fiasObjectDAO;

    /**
     * Поиск уровней адреса.
     */
    public FiasObject[] findLevelsFromLowLevelAOGUID(String aoguid) {
        FiasObject[] objects = new FiasObject[7];

        // Добавление первого объекта.
        FiasObject object = fiasObjectDAO.findByAOGUID(aoguid);
        objects[object.getAOLEVEL() - 1] = object;

        // Поиск объектов.
        while (object.getPARENTGUID() != null) {
            object = fiasObjectDAO.findByAOGUID(object.getPARENTGUID());
            objects[object.getAOLEVEL() - 1] = object;
        }

        return objects;
    }
}
