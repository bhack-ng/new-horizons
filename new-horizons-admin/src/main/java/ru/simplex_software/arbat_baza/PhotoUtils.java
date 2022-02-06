package ru.simplex_software.arbat_baza;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import ru.simplex_software.arbat_baza.model.Agent;
import ru.simplex_software.arbat_baza.model.DbFile;
import ru.simplex_software.arbat_baza.model.Photo;
import ru.simplex_software.arbat_baza.model.PhotoData;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Created by dima on 20.05.16.
 */
public class PhotoUtils {
    private static final Logger LOG= LoggerFactory.getLogger(PhotoUtils.class);
    public static void makePreviewImageInmem(Photo photo) throws IOException {
        if( photo.getData()==null || photo.getData().getData().length==0 ) {
            return;
        }
        if( ! StringUtils.hasLength(photo.getContentType())){
            return;
        }
        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(photo.getData().getData()));
        if(originalImage ==null){
            return;
        }
        int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
        ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();

        BufferedImage resizeImageJpg = resizeImage(originalImage, type);
        ImageIO.write(resizeImageJpg, photo.getContentType().substring("image/".length()), baOutputStream);
        photo.getPreview().setData(baOutputStream.toByteArray());
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int type){
        BufferedImage resizedImage = new BufferedImage(120, 80, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, 120, 80, null);
        g.dispose();
        return resizedImage;
    }

    public static void addStampedData(Photo photo) {
        if (!StringUtils.hasLength(photo.getContentType())) {
            return;
        }

        if(photo.getRealtyObject().getAgent()==null){
            LOG.error("method must not be called for external objects");
            return;
        }
        final DbFile watermark = photo.getRealtyObject().getAgent().getAgency().getWatermark();
        if(watermark==null){
            return;
        }

        try{

            final ByteArrayInputStream watermarkStream = new ByteArrayInputStream(watermark.getContent().getData());
            BufferedImage bg = ImageIO.read(watermarkStream);

            PhotoData stampedData = new PhotoData();
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(photo.getData().getData()));
            image.getGraphics().drawImage(bg,image.getWidth()-bg.getWidth(), image.getHeight()-bg.getHeight(), null);

            ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, photo.getContentType().substring("image/".length()), baOutputStream);
            stampedData.setData(baOutputStream.toByteArray());
            photo.setStampedContent(stampedData);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }


    public static String getAgentImageUrl(Agent agent){
        if(agent.getPhoto()==null || agent.getPhoto().length==0){
            return "";
        }else{
            //add version for refresh image
            return "/agent/photo?login=" + agent.getLogin()+"&date="+agent.getVersion()+"&timestamp="+ (new Date().getTime());
        }

    }

}
