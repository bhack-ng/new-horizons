package ru.simplex_software.arbat_baza.init;

import org.springframework.beans.factory.InitializingBean;
import ru.simplex_software.arbat_baza.dao.PhotoDAO;

import javax.annotation.Resource;

/**
 * Запуск загрузчика данных.
 */
public class DataloaderInit implements InitializingBean{
    @Resource
    private Dataloader dataloader;
    @Resource
    private PhotoDAO photoDAO;

    @Override
    public void afterPropertiesSet() throws Exception {
        dataloader.load();
        dataloader.objectStatuses();
        dataloader.importMetroStations();
        dataloader.importDirectionRoad();
        dataloader.insertAgency();
        dataloader.insertAgent();
        dataloader.insertAdminArea();
        dataloader.importDirectionRoad();
    }

}
