package ru.simplex_software.arbat_baza.xml.yandex.jaxb.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Possible variant ov value: «да», «true», «1», «+»; нет false 0 -
 */
public class BooleanAdapter extends XmlAdapter<String, Boolean> {
    String YES[]=new String[]{"да", "true", "1", "+"};
    String  NO[]=new String[]{"нет", "false", "0", "-"};
    @Override
    public Boolean unmarshal(String s) throws Exception {
        for(String v:YES){
            if(v.equalsIgnoreCase(s)){
                return Boolean.TRUE;
            }
        }
        for(String v:NO){
            if(v.equalsIgnoreCase(s)){
                return Boolean.FALSE;
            }
        }
        return null;
    }

    @Override
    public String marshal(Boolean b) throws Exception {
        if(b==null){
            return "";
        }
        return b.toString();
    }
}
