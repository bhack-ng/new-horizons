package ru.simplex_software.arbat_baza.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.simplex_software.arbat_baza.model.RealtyObjectType;
import ru.simplex_software.arbat_baza.model.privateHouses.PrivateHouseRent;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class PrivateHouseRentDAOTest {

    @Resource
    private PrivateHouseRentDao privateHouseRentDao;

    @Test
    @Transactional
    public void testRentDao() {

        PrivateHouseRent rent = new PrivateHouseRent();
        rent.setRealtyObjectType(RealtyObjectType.COMMERCE_LEASE);
        privateHouseRentDao.saveOrUpdate(rent);

        List<PrivateHouseRent> rentList = privateHouseRentDao.findAll();

        Assert.assertEquals(1, rentList.size());
    }
}
