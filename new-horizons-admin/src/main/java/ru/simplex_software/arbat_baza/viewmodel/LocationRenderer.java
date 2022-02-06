package ru.simplex_software.arbat_baza.viewmodel;

import ru.simplex_software.arbat_baza.model.Address;
import ru.simplex_software.arbat_baza.model.MetroStation;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.arbat_baza.model.Street;

/**
 * convert Location of realty object to string.
 */
public class LocationRenderer {
    public static  String toLocolizedString(RealtyObject realtyObject){
        String s="";
        MetroStation metroStation = realtyObject.getMetroLocation().getMetroStation();
        if(metroStation!=null){
            s=s+"м."+ metroStation.getStationName();
        }
        Address address = realtyObject.getAddress();
        if(address.getFiasAddressVector()==null){
            Street street= address.getStreet();
            if(street!=null){
                s = s+", " + street.getStreetName();
                if(address.getHouse_str()!=null){
                    s=s+", "+address.getHouse_str();
                }
                if(address.getKvartira()!=null){
                    s=s+", "+address.getKvartira();
                }
            }
        }else{
           s=s+", "+address.getFiasAddressVector().toString();
        }
        return  s;
    }

}
