package ru.simplex_software.arbat_baza.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplex_software.arbat_baza.dao.SteadRentDao;
import ru.simplex_software.arbat_baza.dao.SteadSaleDao;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.arbat_baza.model.fias.FiasAddressVector;
import ru.simplex_software.arbat_baza.model.stead.Stead;
import ru.simplex_software.arbat_baza.model.stead.SteadRent;
import ru.simplex_software.arbat_baza.model.stead.SteadSale;

import javax.annotation.Resource;
import javax.xml.stream.XMLStreamException;
import java.util.ArrayList;
import java.util.List;

public class YandexSteadServlet extends AbstractYandexExportServlet {
    private static final Logger LOG = LoggerFactory.getLogger(YandexSteadServlet.class);

    @Resource
    SteadSaleDao steadSaleDao;
    @Resource
    SteadRentDao steadRentDao;

    @Override
    protected List<? extends RealtyObject> loadListRealtyObjects() {
        List<SteadSale> saleList = steadSaleDao.findToYandexExport();
        List<SteadRent> rentList = steadRentDao.findToYandexExport();
        List<Stead> steadList = new ArrayList<Stead>();
        steadList.addAll(saleList);
        steadList.addAll(rentList);
        return steadList;
    }

    @Override
    protected void writeOptions(RealtyObject offer) throws XMLStreamException {
        //is empty
    }

    @Override
    protected void writeAboutObject(RealtyObject offer) throws XMLStreamException {
        //is empty
    }

    @Override
    protected void writeTypeObject() throws XMLStreamException {

        tagWriter("category", "участок");
    }

    @Override
    protected void insideLocalityTag(RealtyObject offer) throws XMLStreamException {
        Stead stead = (Stead) offer;
        FiasAddressVector fiasAddressVector = stead.getAddress().getFiasAddressVector();
        if (!fiasAddressVector.getLevel1().getFORMALNAME().toString().equals("Москва")) {
            return;
        }

        if (stead.getDirectionRoad() == null) {
            return;
        }
        tagWriter("direction", stead.getDirectionRoad().getRoadName());
        tagWriter("distance", stead.getDistanceToCity());
    }
}

