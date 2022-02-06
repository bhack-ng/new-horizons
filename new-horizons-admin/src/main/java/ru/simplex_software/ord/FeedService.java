package ru.simplex_software.ord;

import net.sf.autodao.PersistentEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.simplex_software.arbat_baza.dao.RealtyFilterDAO;
import ru.simplex_software.arbat_baza.dao.odor.FeedDAO;
import ru.simplex_software.arbat_baza.model.RealtyFilter;
import ru.simplex_software.arbat_baza.model.odor.Feed;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.DelayQueue;

/**
 * Инициализация и обновление feedQueue данными из базы.
 */
public class FeedService {

    private static final Logger LOG = LoggerFactory.getLogger(FeedService.class);
    @Resource(name = "feedQueue")
    private DelayQueue<Feed> feedQueue;

    @Resource
    private FeedDAO feedDAO;

    @Resource
    RealtyFilterDAO realtyFilterDAO;

    @Transactional
    public void init(){
        final List<Feed> allActive = feedDAO.findAllActive();
        feedQueue.addAll(allActive);
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Feed update(Feed feed) {
        if(feed.getId()!=null){
            feed = feedDAO.get(feed.getId());
        }
        feed.setLastUpdate(Instant.now());
        feed.getErrors().clear();
        feedDAO.saveOrUpdate(feed);
        return feed;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void filterUpdate(RealtyFilter filter){
        realtyFilterDAO.saveOrUpdate(filter);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Feed saveError(Feed feed, Exception e) {
        LOG.error(e.getMessage(), e);
        feed=feedDAO.get(feed.getId());
        feed.getErrors().add(Instant.now().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("d::MMM::uuuu HH::mm::ss"))+
                " Ошибка:"+e.getClass().getName()+" "+e.getMessage());
        feedDAO.saveOrUpdate(feed);
        return feed;
    }

    public boolean checkQueue(){
        final Object[] objects = feedQueue.toArray();
        final HashMap<Serializable, Boolean> map = new HashMap<>();
        for(Object o:objects){
            PersistentEntity pe=(PersistentEntity)o;
            final Boolean put = map.put(pe.getPrimaryKey(), Boolean.TRUE);
            if(put!=null){
                LOG.error("queue contains duplicats with id"+pe.getPrimaryKey());
                return false;
            }

        }
        return true;
    }


    @Transactional
    public void refresh(){
        feedQueue.clear();
        init();
    }

    @Transactional (propagation = Propagation.REQUIRES_NEW)
    public Feed refresh(Feed feed){
        feedDAO.refresh(feed);
        return feed;
    }


}
