package ru.simplex_software;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplex_software.arbat_baza.model.live.LiveArea;
import ru.simplex_software.arbat_baza.xml.YandexLeaseExportServlet;

public class LogicTest {
    private static final Logger LOG = LoggerFactory.getLogger(LogicTest.class);


    @Test
    public void testHasLift(){
        LiveArea liveArea = new LiveArea();
        boolean hasLift;
//1
        liveArea.setLift_p(null);
        liveArea.setLift_g(null);
        hasLift = YandexLeaseExportServlet.hasLift(liveArea);
        Assert.assertFalse(hasLift);
//2
        liveArea.setLift_p(null);
        liveArea.setLift_g(1);
        hasLift = YandexLeaseExportServlet.hasLift(liveArea);
        Assert.assertTrue(hasLift);
//3
        liveArea.setLift_p(1);
        liveArea.setLift_g(null);
        hasLift = YandexLeaseExportServlet.hasLift(liveArea);
        Assert.assertTrue(hasLift);
//4
        liveArea.setLift_p(1);
        liveArea.setLift_g(1);
         hasLift = YandexLeaseExportServlet.hasLift(liveArea);
        Assert.assertTrue(hasLift);
//5
        liveArea.setLift_p(null);
        liveArea.setLift_g(0);
        hasLift = YandexLeaseExportServlet.hasLift(liveArea);
        Assert.assertFalse(hasLift);
//6
        liveArea.setLift_p(0);
        liveArea.setLift_g(null);
        hasLift = YandexLeaseExportServlet.hasLift(liveArea);
        Assert.assertFalse(hasLift);
//7
        liveArea.setLift_p(0);
        liveArea.setLift_g(0);
       hasLift = YandexLeaseExportServlet.hasLift(liveArea);
        Assert.assertFalse(hasLift);
    }
}
