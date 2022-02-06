package ru.simplex_software.arbat_baza.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.simplex_software.arbat_baza.model.RealtyObjectType;
import ru.simplex_software.arbat_baza.model.stead.SteadRent;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml"})
public class SteadRentDAOTest {

    @Resource
    private SteadRentDao steadRentDao;

    @Test
    @Transactional
    public void testSteadRentDao() {
        SteadRent rent = new SteadRent();
        rent.setRealtyObjectType(RealtyObjectType.COMMERCE_LEASE);
        steadRentDao.saveOrUpdate(rent);

        List<SteadRent> rentList = steadRentDao.findAll();

        Assert.assertEquals(1, rentList.size());
    }
}
