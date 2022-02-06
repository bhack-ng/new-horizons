package ru.simplex_software.arbat_baza.xml;

import org.apache.commons.lang3.text.StrBuilder;
import org.xml.sax.Attributes;
import ru.simplex_software.arbat_baza.dao.CianAdminAreaDAO;
import ru.simplex_software.arbat_baza.model.Address;
import ru.simplex_software.arbat_baza.model.Agent;
import ru.simplex_software.arbat_baza.model.CianAdminArea;
import ru.simplex_software.arbat_baza.model.CianIdContainer;
import ru.simplex_software.arbat_baza.model.MetroStation;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.arbat_baza.model.fias.FiasAddressVector;
import ru.simplex_software.arbat_baza.model.fias.FiasObject;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import static ru.simplex_software.arbat_baza.PhotoUtils.getAgentImageUrl;

/**
 * Commen methods used in all xml feeds
 */
public class XmlUtils {

    public static ThreadLocal<Boolean>  outputAgent = new ThreadLocal<>();

    public static  void outputAgent(XMLStreamWriter xmlWriter, Agent agent) throws XMLStreamException {
        if( outputAgent.get() && agent !=null){
            xmlWriter.writeStartElement("agent");
            notNulAtrr(xmlWriter,"id",agent.getExternalId());
            {


                xmlWriter.writeStartElement("fio");
                xmlWriter.writeCharacters(agent.getFio());
                xmlWriter.writeEndElement();

                xmlWriter.writeStartElement("email");
                xmlWriter.writeCharacters(agent.getEmail());
                xmlWriter.writeEndElement();

                xmlWriter.writeStartElement("phone");
                xmlWriter.writeCharacters(agent.getPhone());
                xmlWriter.writeEndElement();

                xmlWriter.writeStartElement("phone");
                xmlWriter.writeCharacters(getAgentImageUrl(agent));
                xmlWriter.writeEndElement();

            }
            xmlWriter.writeEndElement();
        }
    }





    public static void notNulAtrr(XMLStreamWriter writer, String attr, Object val) throws XMLStreamException {
        if(val!=null){
            if(val instanceof CianIdContainer) {
                CianIdContainer cianIdCont = (CianIdContainer) val;
                writer.writeAttribute(attr, "" + cianIdCont.getCianId());
            }else if(val instanceof Integer){
                Integer integer = (Integer) val;//??
                writer.writeAttribute(attr, val.toString());
            }else if(val instanceof Boolean){
                Boolean aBoolean = (Boolean) val;
                writer.writeAttribute(attr, aBoolean?"yes":"no");

            }else {
                writer.writeAttribute(attr, val.toString());
            }

        }
    }
    /** return attibute Value .*/
    public static String getAttrValue(Attributes attributes, String name){
        return attributes.getValue(attributes.getIndex(name));
    }

    public  static String createLocality(Address addr) {
        StrBuilder locality=new StrBuilder();
        final FiasObject[] arr = addr.getFiasAddressVector().toArray();
        for(int i=2; i<6;i++){
            if(arr[i]!=null){
                locality.append(arr[i].getOFFNAME());
                locality.append(" ");
                locality.append(arr[i].getSHORTNAME());
                if(i!=5l){locality.append(", ");}
            }
        }
        return locality.toString();
    }

    public  static String createString (Address addr) {
        if(addr.getFiasAddressVector().getLevel7()==null){
            return "";
        }
        StrBuilder str=new StrBuilder();
        final FiasObject fiasStreet = addr.getFiasAddressVector().getLevel7();
        str.append(fiasStreet.getOFFNAME());
        str.append(" ");
        str.append(fiasStreet.getSHORTNAME());
        return str.toString();
    }

    public  static String createAdminArea (Address addr) {
        if(addr.getFiasAddressVector().getLevel1()==null){
            return "";
        }
        StrBuilder str=new StrBuilder();
        final FiasObject fiasAA = addr.getFiasAddressVector().getLevel1();
        str.append(fiasAA.getOFFNAME());
        str.append(" ");
        str.append(fiasAA.getSHORTNAME());
        return str.toString();
    }

    public static void writeInsieAdress(XMLStreamWriter xmlWriter, Address addr, CianAdminAreaDAO cianAdminAreaDAO) throws XMLStreamException {
        xmlWriter.writeAttribute("admin_area", getAdminArea(addr, cianAdminAreaDAO));
        xmlWriter.writeAttribute("locality", createLocality(addr));
        if(addr.getFiasAddressVector().getLevel7()!=null){
            xmlWriter.writeAttribute("street",createString(addr));
        }
        notNulAtrr(xmlWriter,"house_str",addr.getHouse_str());
    }

    public static String getAdminArea(Address address, CianAdminAreaDAO cianAdminAreaDAO) {
        final FiasAddressVector fiasAddressVector = address.getFiasAddressVector();
        final FiasObject level1 = fiasAddressVector.getLevel1();
        if(level1!=null){
            final CianAdminArea byFiasAOGUID = cianAdminAreaDAO.findByFiasAOGUID(level1.getAOGUID());
            if(byFiasAOGUID!=null){
                return ""+byFiasAOGUID.getCianId();
            }
        }
        return "";
    }
    public static void writePhoneNumbers(XMLStreamWriter xmlWriter, Agent agent) throws XMLStreamException {
        if(agent.getPhone()!=null || agent.getAgency().getPhone()!=null){
            xmlWriter.writeStartElement("phone");
            String phoneString="";
            phoneString = concatPhones(agent, phoneString);
            xmlWriter.writeCharacters(phoneString);
            xmlWriter.writeEndElement();
        }
    }

    private static String concatPhones(Agent agent, String phoneString) {
        String agentPhone = agent.getPhone();
        String phone = agent.getAgency().getPhone();
        if(agentPhone!=null){
            phoneString=agentPhone;
        }
        if(agentPhone!=null&&phone!=null){
            phoneString=phoneString+";";
        }
        if(phone!=null){
            phoneString=phoneString+phone;
        }
        return phoneString;
    }

    public static boolean hasMetroInformation(RealtyObject com) {
        return com.getMetroLocation().getMetroStation()!=null||com.getMetroLocation().getAnyTime()!=null;
    }

    public static void booleanNotNull01(XMLStreamWriter writer, String attr,Boolean val)throws XMLStreamException{
        if(val==null){
            return;
        }
        writer.writeAttribute(attr, val?"1":"0");

    }

    public static void wrigthMetroToXML(XMLStreamWriter xmlWriter,RealtyObject lsr) throws XMLStreamException {
        if (lsr.getMetroLocation() != null && hasMetroInformation(lsr)) {
            xmlWriter.writeStartElement("metro");
            MetroStation station = lsr.getMetroLocation().getMetroStation();
            if (station != null) {
                xmlWriter.writeAttribute("id", "" + station.getId());
            }
            if (lsr.getMetroLocation().isWtimeSelected()) { //следует указывать только один из атрибутов, отвечающих за расстояние: wtime или ttime
                notNulAtrr(xmlWriter, "wtime", lsr.getMetroLocation().getAnyTime());//wtime расстояние в минутах до метро пешком (для циан)
            } else {
                notNulAtrr(xmlWriter, "ttime", lsr.getMetroLocation().getAnyTime());//ttime расстояние в минутах до метро транспортом (для циан)
            }
            xmlWriter.writeEndElement();
        }
    }
}
