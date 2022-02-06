package ru.simplex_software.ord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import ru.simplex_software.arbat_baza.model.odor.Feed;

import javax.annotation.Resource;
import java.util.concurrent.DelayQueue;

/**
 * constantly take feeds that need update and create task for update(SplitFeedTask).
 */

public class FeedManager implements Runnable ,ApplicationListener<ContextClosedEvent>, InitializingBean, ApplicationContextAware {
    private static final Logger LOG = LoggerFactory.getLogger(FeedManager.class);


    @Resource(name = "feedQueue")
    private DelayQueue<Feed> feedQueue;

    @Resource(name = "feedFetchExecutor")
    private ThreadPoolTaskExecutor feedFetchExecutor;

    private volatile boolean stopFlag=false;
    @Resource
    private FeedService feedService;

    private ApplicationContext appContext;

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext =appContext;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        feedQueue.clear();
        stopFlag=true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        feedService.init();
    }

    @Override
    public void run() {

        while(!stopFlag){
            try {
                Feed feed = feedQueue.take();
                runUpdateFeed(feed);
            } catch (Exception e) {
                LOG.error(e.getMessage(),e);
            }
        }
    }

    public void runUpdateFeed(Feed feed) {
        //feedService.checkQueue();
        feed=feedService.update(feed);
        feedQueue.add(feed);
        final SplitFeedTask splitFeedTask = appContext.getBean(SplitFeedTask.class,feed);
        feedFetchExecutor.submit(splitFeedTask);
    }

    public DelayQueue<Feed> getFeedQueue() {
        return feedQueue;
    }
}
