package ru.simplex_software.arbat_baza.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.simplex_software.arbat_baza.model.RealtyObjectType;
import ru.simplex_software.arbat_baza.model.privateHouses.PrivateHouseSale;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class PrivateHouseSaleDAOTest {

    @Resource
    private PrivateHouseSaleDao privateHouseSaleDao;

    @Test
    @Transactional
    public void testSaleDao() {
        PrivateHouseSale sale = new PrivateHouseSale();
        sale.setRealtyObjectType(RealtyObjectType.COMMERCE_LEASE);
        privateHouseSaleDao.saveOrUpdate(sale);

        List<PrivateHouseSale> salesList = privateHouseSaleDao.findAll();

        Assert.assertEquals(1, salesList.size());
    }
}

