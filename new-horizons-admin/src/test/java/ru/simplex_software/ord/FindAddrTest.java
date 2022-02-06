package ru.simplex_software.ord;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.simplex_software.arbat_baza.dao.fias.FiasObjectDAO;
import ru.simplex_software.arbat_baza.model.fias.FiasObject;

import javax.annotation.Resource;
import java.util.List;

/**
 * .
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class FindAddrTest {
    private static final Logger LOG = LoggerFactory.getLogger(FindAddrTest.class);

    @Resource
    private FiasObjectDAO fiasObjectDAO;

    @Test @Transactional
    public void testIt(){
        final List<FiasObject> result = fiasObjectDAO.findByLevelAndSockr(3, "р-н", "Пушкинский", "29251dcf-00a1-4e34-98d4-5c47484a36d4");
    }
}
