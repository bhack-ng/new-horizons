package ru.simplex_software.arbat_baza;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.simplex_software.arbat_baza.dao.ContactsOfOwnerDAO;
import ru.simplex_software.arbat_baza.dao.RealtyObjectDAO;
import ru.simplex_software.arbat_baza.dao.odor.FeedDAO;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.arbat_baza.model.odor.Feed;

import javax.annotation.Resource;
import java.util.List;

public class DeleteOutdatedObjectsService implements ApplicationContextAware {

    private ApplicationContext context;

    @Resource
    private RealtyObjectDAO realtyObjectDAO;
    @Resource
    private ContactsOfOwnerDAO contactsOfOwnerDAO;
    @Resource
    private FeedDAO feedDAO;

    public void deleteOutdated(Feed feed) {
        int size = 0;
        do {
            DeleteOutdatedObjectsService bean = context.getBean(DeleteOutdatedObjectsService.class);
            size = bean.inTransaction(feed);
        } while (size != 0);
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int inTransaction(Feed feed) {
        feedDAO.refresh(feed);
        List<RealtyObject> list;
        list = realtyObjectDAO.findOutdatedObjects(100, feed);
        for (RealtyObject realtyObject : list) {
            realtyObject.getContactsOfOwners().forEach(contactsOfOwnerDAO::delete);
            realtyObjectDAO.delete(realtyObject);
        }
        return list.size();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
