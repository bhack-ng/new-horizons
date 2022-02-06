package ru.simplex_software.ord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import ru.simplex_software.arbat_baza.DeleteOutdatedObjectsService;
import ru.simplex_software.arbat_baza.dao.odor.FeedDAO;
import ru.simplex_software.arbat_baza.model.FeedType;
import ru.simplex_software.arbat_baza.model.odor.Feed;

import javax.annotation.Resource;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Задача которая разбивает потенциально большой xml файл на более маленькие.
 * Также следит за тем чтобы рамеры файлов (исходных и вновь созданных) укладывались в лимиты определённых в Feed.
 */
public class SplitFeedTask implements Runnable, ApplicationContextAware {
    private static final Logger LOG = LoggerFactory.getLogger(SplitFeedTask.class);
    @Resource(name = "offerParserExecutor")
    private ThreadPoolTaskExecutor offerParserExecutor;

    @Resource
    private FeedService feedService;
    @Resource
    private FeedDAO feedDAO;
    @Resource
    DeleteOutdatedObjectsService deleteOutdatedObjectService;
    private Feed feed;
    private ApplicationContext appContext;

    List<Future> resultlist=new ArrayList<Future>();


    public SplitFeedTask(Feed feed) {
        this.feed = feed;
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext=appContext;
    }

    @Override
    public void run() {
        try {
            final URL url = new URL(feed.getUrl());
            final InputStream is = url.openStream();
            LimitedInputStream lis = new LimitedInputStream(is, feed.getAllFeedSizeLimit()) {
                @Override
                protected void raiseError(long pSizeMax, long pCount) throws IOException {
                    throw new SizeLimitException("input stream too big");
                }
            };
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(true);
            SAXParser parser = factory.newSAXParser();
            DefaultHandler saxp = new CianRentSplitHandler(feed) {
                @Override
                public OutputStream createOutputStram() {
                    OutputStream splitedOutputStream = new ByteArrayOutputStream(512);
                    return splitedOutputStream;
                }

                @Override
                public void closeOutputStram() {
                    final byte[] bytes = ((ByteArrayOutputStream) splitedOutputStream).toByteArray();
                    if(feed.getFeedType()== FeedType.CIAN_LIVE_RENT) {
                        final Runnable task = (Runnable) appContext.getBean("cianLiveRentOfferParserTask", bytes);
                        resultlist.add(offerParserExecutor.submit(task));
                    }else if(feed.getFeedType()== FeedType.CIAN_LIVE_SALE) {
                        final Runnable task = (Runnable) appContext.getBean("cianLiveSaleOfferParserTask", bytes);
                        resultlist.add(offerParserExecutor.submit(task));
                    }else if (feed.getFeedType() == FeedType.CIAN_COMMERICAL){
                        final Runnable task = (Runnable) appContext.getBean("cianCommerceOfferParserTask",bytes);
                        resultlist.add(offerParserExecutor.submit(task));
                    }
                }

                @Override
                public void endDocument() throws SAXException {
                    updateOnFinish(null);
                }
            };

            parser.parse(lis, saxp);
        } catch (Exception e) {

            try {
                updateOnFinish(e);
            }catch (Exception e2){
                LOG.error(e.getMessage(), e2);
            }
        }
    }

    private void updateOnFinish(Exception ex){
        for(Future f:resultlist){
            try {
                f.get();
            } catch (Exception e) {
                LOG.info(e.getMessage(), e);
            }
        }
        if(ex!=null){
            feedService.saveError(feed,ex);
        }
        deleteOutdatedObjectService.deleteOutdated(feed);
    }
}
