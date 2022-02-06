package ru.simplex_software.ord;

import ru.simplex_software.arbat_baza.model.fias.FiasObject;

/**
 * Created by Nick on 14.09.2016.
 */
public interface X extends Runnable {

    public FiasObject findFiasObjectInLevel(int zLevel, String nameWithSocr, String parentAOUID);
}
