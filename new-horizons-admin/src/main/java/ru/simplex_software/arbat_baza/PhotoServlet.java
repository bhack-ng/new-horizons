package ru.simplex_software.arbat_baza;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestHandler;
import ru.simplex_software.arbat_baza.dao.PhotoDAO;
import ru.simplex_software.arbat_baza.dao.PhotoDataDAO;
import ru.simplex_software.arbat_baza.model.Photo;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * .
 */
public class PhotoServlet implements HttpRequestHandler {
    private static final Logger LOG = LoggerFactory.getLogger(PhotoServlet.class);

    @Resource
    private PhotoDAO photoDAO;
    @Resource
    private PhotoDataDAO photoDataDAO;


    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id;
        try {
            String idStr = request.getParameter("id");
            id = Long.parseLong(idStr);
        }catch (Exception ex){
            response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
            return;
        }
        String preview = request.getParameter("preview");
        Photo photo = photoDAO.get(id);
        if (photo == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
            return;
        }

        long cacheAge=60*60*24*30*12;//1 год
        long expiry = new Date().getTime() + cacheAge*1000;

        response.setDateHeader("Expires", expiry);
        response.setHeader("Cache-Control", "max-age="+ cacheAge);

        response.setContentType(photo.getContentType());

        if(preview==null){
            if( photo.getStampedContent()==null) {//copy and add watermark
                    PhotoUtils.addStampedData(photo);
                    if(photo.getStampedContent()!=null){
                        photoDataDAO.saveOrUpdate(photo.getStampedContent());
                    }
                    photoDAO.saveOrUpdate(photo);
            }
            if(photo.getStampedContent()==null) {
                response.setContentLength(photo.getData().getData().length);
                response.getOutputStream().write(photo.getData().getData());
            }else{
                response.setContentLength(photo.getStampedContent().getData().length);
                response.getOutputStream().write(photo.getStampedContent().getData());
            }

        }else{
            response.setContentLength(photo.getPreview().getData().length);
            response.getOutputStream().write(photo.getPreview().getData());
        }

    }

}
