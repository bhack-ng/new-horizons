package ru.simplex_software.arbat_baza.dao;

import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import net.sf.autodao.Limit;
import net.sf.autodao.Named;
import ru.simplex_software.arbat_baza.model.Photo;
import ru.simplex_software.arbat_baza.model.RealtyObject;

import java.util.List;

public interface PhotoDAO extends Dao<Photo,Long> {
    @Finder(query = "from Photo")
    List<Photo> findAll();

    @Finder(query = "from Photo where preview is null")
    List<Photo> findWithoutPreview();

    @Finder(query = "from Photo where data is null")
    List<Photo> findWithoutData(@Limit int limit);

    @Finder(query = "select id from Photo where realtyObject=:realtyObject AND main = true")
    List<Long> getMainPhotoId(@Named("realtyObject") RealtyObject realtyObject, @Limit int limit);

    @Finder(query = "select id from Photo where realtyObject = :realtyObject AND advertise = TRUE ORDER BY main DESC")
    List<Long> findAdvertiseIdsByRealtyObject(@Named("realtyObject") RealtyObject realtyObject);
}

