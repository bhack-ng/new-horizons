package ru.simplex_software.arbat_baza.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.simplex_software.arbat_baza.model.RealtyObjectType;
import ru.simplex_software.arbat_baza.model.stead.SteadSale;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml"})
public class SteadSaleDAOTest {

    @Resource
    private SteadSaleDao steadSaleDao;

    @Test
    @Transactional
    public void testSteadSaleDao() {
        SteadSale sale = new SteadSale();
        sale.setRealtyObjectType(RealtyObjectType.COMMERCE_LEASE);
        steadSaleDao.saveOrUpdate(sale);

        List<SteadSale> salesList = steadSaleDao.findAll();

        Assert.assertEquals(1, salesList.size());
    }
}
