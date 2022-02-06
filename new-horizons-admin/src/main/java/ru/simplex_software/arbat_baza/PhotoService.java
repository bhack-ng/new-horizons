package ru.simplex_software.arbat_baza;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.simplex_software.arbat_baza.dao.PhotoDAO;
import ru.simplex_software.arbat_baza.dao.PhotoDataDAO;
import ru.simplex_software.arbat_baza.dao.RealtyObjectDAO;
import ru.simplex_software.arbat_baza.model.Photo;
import ru.simplex_software.arbat_baza.model.RealtyObject;

import javax.annotation.Resource;

public class PhotoService {
    @Resource
    PhotoDataDAO photoDataDAO;
    @Resource
    PhotoDAO photoDAO;
    @Resource
    RealtyObjectDAO realtyObjectDAO;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void savePhoto(Photo photo){
        photoDataDAO.saveOrUpdate(photo.getPreview());
        photoDataDAO.saveOrUpdate(photo.getData());
        photoDAO.saveOrUpdate(photo);

    }

}
