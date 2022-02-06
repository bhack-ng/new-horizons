package ru.simplex_software.arbat_baza;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;
import ru.simplex_software.arbat_baza.dao.AgencyDAO;
import ru.simplex_software.arbat_baza.model.Agency;

import javax.annotation.Resource;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml","classpath:AgencyServiceConfigurations.xml"})

/**
 * this class

 */

public class AgencyTest {
    private static final Logger LOG = LoggerFactory.getLogger(AgencyTest.class);
    @Resource
    AgencyService agencyService;
    private Agency a = new Agency();

    @Test()
    @Rollback(false)
    public void setDefaultUpdateInterval1() {
        a.setDefaultUpdateInterval(32);
        agencyService.save(a);
        agencyService.delete(a);

    }


    @Test(expected = Exception.class)
    @Rollback(false)
    public void setDefaultUpdateInterval2() {
        a.setDefaultUpdateInterval(0);
        agencyService.save(a);
        agencyService.delete(a);

    }

}
