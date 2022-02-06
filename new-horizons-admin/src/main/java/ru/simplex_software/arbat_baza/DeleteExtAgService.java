package ru.simplex_software.arbat_baza;

import ru.simplex_software.arbat_baza.DeleteOutdatedObjectsService;
import ru.simplex_software.arbat_baza.dao.RealtyFilterDAO;
import ru.simplex_software.arbat_baza.dao.odor.ExternalAgencyDAO;
import ru.simplex_software.arbat_baza.model.RealtyFilter;
import ru.simplex_software.arbat_baza.model.odor.ExternalAgency;
import ru.simplex_software.arbat_baza.model.odor.Feed;
import ru.simplex_software.ord.FeedManager;
import ru.simplex_software.ord.FeedService;

import javax.annotation.Resource;
import java.util.List;


public class DeleteExtAgService {
    @Resource
    FeedService feedService;
    @Resource
    FeedManager feedManager;
    @Resource
    RealtyFilterDAO realtyFilterDAO;
    @Resource
    ExternalAgencyDAO externalAgencyDAO;
    @Resource
    DeleteOutdatedObjectsService deleteOutdatedObjectsService;

    public void deleteExternalAgency(ExternalAgency externalAgency){
        for(Feed feed :externalAgency.getFeedList()){
            feedService.update(feed);
            deleteOutdatedObjectsService.deleteOutdated(feed);
        }

        List<RealtyFilter> listRealtyFilter= realtyFilterDAO.deleteExternalAgency(externalAgency);

        for (RealtyFilter realtyFilter:listRealtyFilter){
            realtyFilter.setExternalAgency(null);
            feedService.filterUpdate(realtyFilter);
        }
        for(Feed f :externalAgency.getFeedList()){
             feedManager.getFeedQueue().remove(f);
        }
        externalAgencyDAO.delete(externalAgency);
    }
}
