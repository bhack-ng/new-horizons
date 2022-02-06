package ru.simplex_software.ord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.simplex_software.arbat_baza.dao.ContactsOfOwnerDAO;
import ru.simplex_software.arbat_baza.dao.MetroStationDAO;
import ru.simplex_software.arbat_baza.dao.RealtyObjectDAO;
import ru.simplex_software.arbat_baza.dao.fias.FiasAddressVectorDAO;
import ru.simplex_software.arbat_baza.dao.fias.FiasObjectDAO;
import ru.simplex_software.arbat_baza.dao.fias.FiasSocrDAO;
import ru.simplex_software.arbat_baza.dao.odor.FeedDAO;
import ru.simplex_software.arbat_baza.model.Commission;
import ru.simplex_software.arbat_baza.model.ContactsOfOwner;
import ru.simplex_software.arbat_baza.model.ExternalObectExt;
import ru.simplex_software.arbat_baza.model.MetroLocation;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.arbat_baza.model.RealtyObjectType;
import ru.simplex_software.arbat_baza.model.fias.FiasObject;
import ru.simplex_software.arbat_baza.model.fias.FiasSocr;
import ru.simplex_software.arbat_baza.model.odor.Feed;
import ru.simplex_software.arbat_baza.model.price.Currency;
import ru.simplex_software.arbat_baza.model.price.SimplePrice;
import ru.simplex_software.ord.utils.FiasAddressVectorUtils;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Common methods.
 */
public abstract class OfferParser implements X {

    private static final Logger LOG = LoggerFactory.getLogger(OfferParser.class);

    @Resource
    protected FiasObjectDAO fiasObjectDAO;

    @Resource
    protected FiasSocrDAO socrDAO;

    @Resource
    protected RealtyObjectDAO realtyObjectDAO;

    @Resource
    protected ContactsOfOwnerDAO contactsOfOwnerDAO;

    @Resource
    protected MetroStationDAO metroStationDAO;

    @Resource
    protected FeedDAO feedDAO;

    @Resource
    protected FiasAddressVectorUtils fiasAddressVectorUtils;

    @Resource
    protected FiasAddressVectorDAO fiasAddressVectorDAO;

    protected XPath xPath = XPathFactory.newInstance().newXPath();
    protected RealtyObjectType realtyObjectType;
    protected byte[] xml;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void run() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();


            ByteArrayInputStream input = new ByteArrayInputStream(xml);
            Document doc = builder.parse(input);
            final Element root = doc.getDocumentElement();


            RealtyObject realtyObject = getOrCreateRealtyObject(root);
            setMetro(realtyObject, root);
            setAddress(realtyObject, root);
            setArea(realtyObject, root);
            setPrice(realtyObject, root);
            setFloor(realtyObject, root);
            setPhoto(realtyObject, root);
            setContactOfOwners(realtyObject, root);
            setOptions(realtyObject, root);
            setNote(realtyObject, root);
            setCommission(realtyObject, root);
            setContractType(realtyObject, root);
            setCommerceBuilding(realtyObject, root);
            realtyObject.getExternalObectExt().setUpdated(Instant.now());
            realtyObject.setChanged(LocalDateTime.now());
            realtyObjectDAO.saveOrUpdate(realtyObject);

            LOG.debug("offer parsed");

        } catch (Exception e) {
            LOG.info(e.getMessage(), e);
        }
    }

    protected void setFloor(RealtyObject realtyObject, Element root) {
    }

    protected void setNote(RealtyObject realtyObject, Element root) {
        try {
            String note = (String) xPath.evaluate("/offer/note", root, XPathConstants.STRING);
            note = note.trim();
            realtyObject.setNote(note);
        } catch (Exception e) {
            LOG.info(e.getMessage(), e);
            realtyObject.getExternalObectExt().getFeed().getErrors().add(e.getMessage());
        }
    }


    protected void setContactOfOwners(RealtyObject realtyObject, Element root) {
        try {
            String value = (String) xPath.evaluate("/offer/phone", root, XPathConstants.STRING);
            final String[] split = value.split(";");
            ArrayList<ContactsOfOwner> toDelete = new ArrayList();
            toDelete.addAll(realtyObject.getContactsOfOwners());//make copy

            for (String p : split) {
                final ContactsOfOwner find = hasAlreadyPhone(realtyObject, p);
                if (find == null) {
                    ContactsOfOwner contactsOfOwner = new ContactsOfOwner();
                    contactsOfOwner.setPhone(p);
                    contactsOfOwnerDAO.saveOrUpdate(contactsOfOwner);
                    realtyObject.getContactsOfOwners().add(contactsOfOwner);
                    contactsOfOwner.setRealtyObject(realtyObject);
                } else {
                    toDelete.remove(find);//don't delete contacts which exists
                }
            }
            //delete old contacts which not exists in  feed
            for (ContactsOfOwner d : toDelete) {
                contactsOfOwnerDAO.delete(d);
            }
        } catch (Exception e) {
//            set error
//            realtyObject.getExternalObectExt();
            LOG.error(e.getMessage(), e);
        }

    }

    private ContactsOfOwner hasAlreadyPhone(RealtyObject realtyObject, String p) {
        final List<ContactsOfOwner> contactsOfOwners = realtyObject.getContactsOfOwners();
        for (int i = 0; i < contactsOfOwners.size(); i++) {
            if (p.equals(contactsOfOwners.get(i).getPhone())) {
                return contactsOfOwners.get(i);
            }
        }
        return null;
    }

    protected void setPrice(RealtyObject realtyObject, Element root) {
        try {
            final SimplePrice price = realtyObject.getPrice();

            price.setValue(intHelper("/offer/price", root, realtyObject));

            String currency = (String) xPath.evaluate("/offer/price/@currency", root, XPathConstants.STRING);
            if (StringUtils.hasLength(currency)) {
                price.setCurrency(Currency.valueOf(currency));
            }
        } catch (Exception e) {
            LOG.info(e.getMessage(), e);
        }

    }

    protected void setPhoto(RealtyObject realtyObject, Element root) {
        realtyObject.getExternalObectExt().getExternalPhoto().clear();
        try {
            NodeList list = (NodeList) xPath.evaluate("/offer/photo", root, XPathConstants.NODESET);
            for (int i = 0; i < list.getLength(); i++) {
                final Node item = list.item(i);
                final String photoUrl = item.getTextContent();
                if (StringUtils.hasLength(photoUrl)) {
                    realtyObject.getExternalObectExt().getExternalPhoto().add(photoUrl);
                }
            }

        } catch (Exception e) {
            LOG.info(e.getMessage(), e);
        }
    }

    protected RealtyObject getOrCreateRealtyObject(Element root) {

        try {
            String externalId = (String) xPath.evaluate("/offer/id", root, XPathConstants.STRING);
            String feedId = (String) xPath.evaluate("/offer/@feedId", root, XPathConstants.STRING);
            Feed feed = feedDAO.get(Long.parseLong(feedId));
            if ((feed == null) || !StringUtils.hasLength(externalId)) {
                feed.getErrors().add("incorrect xml. Id and feedId needed.");
                throw new IllegalArgumentException("incorrect xml. Id and feedId needed.");

            }
            RealtyObject realtyObject = realtyObjectDAO.findExternal(feed, externalId);
            if (realtyObject == null) {
                realtyObject = RealtyObject.create(realtyObjectType);

                realtyObject.setExternalObectExt(new ExternalObectExt());
                realtyObject.getExternalObectExt().setExternalId(externalId);
                realtyObject.getExternalObectExt().setFeed(feed);
            }
            return realtyObject;
        } catch (XPathExpressionException e) {

            throw new IllegalArgumentException("incorrect xml", e);
        }

    }

    /**
     * Получение и установка адреса.
     */
    protected void setAddress(RealtyObject realtyObject, Element root) {
        String localityStr = "";
        String streetStr = "";
        String houseStr = "";
        String fiasIdStr = "";

        try {
            localityStr = (String) xPath.evaluate("/offer/address/@locality", root, XPathConstants.STRING);
            streetStr = (String) xPath.evaluate("/offer/address/@street", root, XPathConstants.STRING);
            houseStr = (String) xPath.evaluate("/offer/address/@house_str", root, XPathConstants.STRING);
            fiasIdStr = (String) xPath.evaluate("/offer/address/@fiasId", root, XPathConstants.STRING);
        } catch (XPathExpressionException e) {
            LOG.info(e.getMessage(), e);
        }

        // Получение вектора адреса.
        FiasObject[] levels;
        if (fiasIdStr.isEmpty()) {
            levels = findLevels(realtyObject, root, localityStr, streetStr);
        } else {
            levels = fiasAddressVectorUtils.findLevelsFromLowLevelAOGUID(fiasIdStr);
        }

        // Установка адреса.
        realtyObject.getAddress().getFiasAddressVector().setLevels(levels);
        realtyObject.getAddress().setHouse_str(houseStr);
    }

    /**
     * Поиск уровней адреса.
     */
    private FiasObject[] findLevels(RealtyObject realtyObject, Element root,
                                    String localityStr, String streetStr) {

        FiasObject[] levels;
        List<FiasObject> regionByCianId = null;

        Integer cianId = intHelper("/offer/address/@admin_area", root, realtyObject);
        if (cianId != null) {
            regionByCianId = fiasObjectDAO.findRegionByCianId(cianId);
        }
        if ((cianId == null) || (regionByCianId.isEmpty())) {
            return new FiasObject[7];
        }
        final FiasObject fiasRegion = regionByCianId.get(0);
        realtyObject.getAddress().getFiasAddressVector().setLevel1(fiasRegion);

        //3-6 уровни (2-5 zLevel)
        if (!org.springframework.util.StringUtils.hasLength(localityStr)) {
            return new FiasObject[7];
        }

        int spliNum = 0;
        levels = realtyObject.getAddress().getFiasAddressVector().toArray();
        String parent = levels[0].getAOGUID();
        final String[] splitedLocality = localityStr.split("\\,", 4);

        for (int level = 3; level < 7 && spliNum < splitedLocality.length; level++) {
            final FiasObject nextLevel = findFiasObjectInLevel(level - 1, splitedLocality[spliNum], parent);
            if (nextLevel != null) {
                levels[level - 1] = nextLevel;
                spliNum++;
                parent = nextLevel.getAOGUID();
            }
        }

        //7 уровень (улицы)
        levels[6] = findFiasObjectInLevel(6, streetStr, parent);

        return levels;
    }

    private void setMetro(RealtyObject realtyObject, Element root) {
        MetroLocation metroLocation = realtyObject.getMetroLocation();
        try {
            String stationMetroId = (String) xPath.evaluate("/offer/metro/@id", root, XPathConstants.STRING);
            if (StringUtils.hasLength(stationMetroId))
                try {
                    Long stMetroId = Long.parseLong(stationMetroId);
                    metroLocation.setMetroStation(metroStationDAO.get(stMetroId));
                } catch (Exception e) {
                    String extId = realtyObject.getExternalObectExt().externalId;
                    String err = "ошибка чтения напараметра: metro id у объекта с id=" + extId;
                    realtyObject.getExternalObectExt().getFeed().getErrors().add(err);
                }


            String wtime = (String) xPath.evaluate("/offer/metro/@wtime", root, XPathConstants.STRING);
            String ttime = (String) xPath.evaluate("/offer/metro/@ttime", root, XPathConstants.STRING);
            if (StringUtils.hasLength(wtime)) {
                metroLocation.setWtimeSelected(true);
                metroLocation.setAnyTime(intHelper("/offer/metro/@wtime", root, realtyObject));
            }
            if (StringUtils.hasLength(ttime)) {
                metroLocation.setWtimeSelected(false);
                metroLocation.setAnyTime(intHelper("/offer/metro/@ttime", root, realtyObject));
            }

        } catch (Exception e) {
            LOG.info(e.getMessage(), e);
        }

    }

    public Integer intHelper(String path, Element root, RealtyObject realtyObject) {
        String extId = realtyObject.getExternalObectExt().externalId;
        Integer correctValue = null;
        String value = null;
        try {
            value = (String) xPath.evaluate(path, root, XPathConstants.STRING);
            if (StringUtils.hasLength(value)) {
                try {
                    correctValue = Integer.parseInt(value);
                    return correctValue;
                } catch (Exception e) {
                    String err = Instant.now().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("d::MMM::uuuu HH::mm::ss"))
                            + "  неверное значение параметра :" + path + " в обекте с id = " + extId + "значение в фиде:" + value;
                    realtyObject.getExternalObectExt().getFeed().getErrors().add(err);
                    return null;
                }
            } else return null;
        } catch (Exception e) {
            String err = "ошибка обьекта с id=" + extId + "  текст ошибки:" + e.getMessage();
            realtyObject.getExternalObectExt().getFeed().getErrors().add(err);
            LOG.info(e.getMessage(), e);
            return null;
        }
    }

    /**
     * этот метод предназначен для парсина элементов которые возваращают "да " "нет"
     * с обработкой ошибок и записью их в базу
     *
     * @param path         путь параметру в xml файле
     * @param root         кореневой элемент xml файла
     * @param realtyObject конкретный обьект в который будет вестись запись в случае успеха
     * @return возвращает "true" либо "false" либо "null"
     */
    public Boolean booleanHelper(String path, Element root, RealtyObject realtyObject) {
        String extId = realtyObject.getExternalObectExt().externalId;    //получаем id обьекта из xml в котором может быть ошибка
        try {
            String value = null;
            value = (String) xPath.evaluate(path, root, XPathConstants.STRING);  //получение данных с xml
            if (StringUtils.hasLength(value)) {                                  // проверка на наличие данных


                if ((value.equals("yes")) || (value.equals("1"))) {
                    return true;
                } else if ((value.equals("no")) || (value.equals("0"))) {
                    return false;
                } else {
                    String err = Instant.now().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("d::MMM::uuuu HH::mm::ss")) +
                            " неверное значение параметра:" + path + " в объекте с id=" + extId
                            + " полученное значение:" + value;
                    realtyObject.getExternalObectExt().getFeed().getErrors().add(err);
                    return false;

                }
            } else return null;
        } catch (Exception e) {
            String err = "ошибка обьекта с id=" + extId + "  текст ошибки:" + e.getMessage();
            realtyObject.getExternalObectExt().getFeed().getErrors().add(err);
            LOG.info(e.getMessage(), e);
            return null;
        }
    }

    public Double doubleHelper(String value, String path, RealtyObject realtyObject) {
        String extId = realtyObject.getExternalObectExt().externalId;
        Double correctValue = null;
        try {
            if (StringUtils.hasLength(value)) {
                correctValue = Double.parseDouble(value);
                return correctValue;
            } else return null;
        } catch (Exception e) {
            String err = Instant.now().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("d::MMM::uuuu HH::mm::ss")) +
                    "неверное значение параметра:" + path + " в объекте с id=" + extId + " полученное значение:" + value;
            realtyObject.getExternalObectExt().getFeed().getErrors().add(err);
            return null;
        }
    }

    /**
     * @param zLevel zero base level.
     */
    public FiasObject findFiasObjectInLevel(int zLevel, String nameWithSocr, String parentAOUID) {
        nameWithSocr = nameWithSocr.trim();
        String upperNameWithSocr = nameWithSocr.toUpperCase();

        final List<FiasSocr> socrs = socrDAO.findFiasSocrByLevel(zLevel + 1);
        String name = null;
        String skr = null;

        for (FiasSocr fs : socrs) {
            if (upperNameWithSocr.startsWith(fs.getSocrName().toUpperCase() + " ")) {
                name = nameWithSocr.substring(fs.getSocrName().length());
                skr = fs.getSocrName();
                break;

            }
            if (upperNameWithSocr.endsWith(" " + fs.getSocrName().toUpperCase())) {
                name = nameWithSocr.substring(0, nameWithSocr.length() - fs.getSocrName().length());
                skr = fs.getSocrName();
                break;
            }
            if (upperNameWithSocr.startsWith(fs.getFullName().toUpperCase())) {
                name = nameWithSocr.substring(fs.getFullName().length());
                skr = fs.getSocrName();
                break;
            }
            if (upperNameWithSocr.endsWith(" " + fs.getFullName().toLowerCase().toUpperCase())) {
                name = nameWithSocr.substring(0, nameWithSocr.length() - fs.getFullName().length());
                skr = fs.getSocrName();
                break;
            }
            if (upperNameWithSocr.equals(fs.getFullName())) {
                name = fs.getFullName();

                break;
            }
        }

        FiasObject result = null;
        if (name == null || skr == null) {

            final List<FiasObject> foList = fiasObjectDAO.findByLevelAndSockr(zLevel + 1, nameWithSocr, parentAOUID);
            if (foList.size() > 0) {
                result = foList.get(0);
            }
        } else {
            name = name.trim();
            final List<FiasObject> foList = fiasObjectDAO.findByLevelAndSockr(zLevel + 1, skr, name, parentAOUID);
            if (foList.size() > 0) {
                result = foList.get(0);
            }
        }
        return result;
    }

    public void setCommission(RealtyObject realtyObject, Element root) {
        Commission commission = realtyObject.getCommission();
        try {
            String salesCommission = (String) xPath.evaluate("/offer/com/@agent", root, XPathConstants.STRING);
            commission.setSalesCommission(doubleHelper(salesCommission, "/offer/com/@agent", realtyObject));
            String buyerCommission = (String) xPath.evaluate("/offer/com/@client", root, XPathConstants.STRING);
            commission.setBuyerCommission(doubleHelper(buyerCommission, "/offer/com/@client", realtyObject));
        } catch (Exception e) {
            realtyObject.getExternalObectExt().getFeed().getErrors().add(e.getMessage());
            LOG.info(e.getMessage(), e);

        }
    }

    protected void setContractType(RealtyObject realtyObject, Element root) {
    }

    protected void setCommerceBuilding(RealtyObject realtyObject, Element root) {
    }

    protected void setArea(RealtyObject realtyObject, Element root) {
    }

    abstract void setOptions(RealtyObject realtyObject, Element root);

    public void setSocrDAO(FiasSocrDAO socrDAO) {
        this.socrDAO = socrDAO;
    }

    public void setFiasObjectDAO(FiasObjectDAO fiasObjectDAO) {
        this.fiasObjectDAO = fiasObjectDAO;
    }
}
