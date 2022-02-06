package ru.simplex_software.arbat_baza.xml.yandex.jaxb.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Adapter that read xml enums ignore case and convert space to underscore (_)
 */
public abstract class SpaceLowCaseEnumAdapter<E extends Enum> extends XmlAdapter<String, E> {
    protected Class<E> enumClass;

    @Override
    public String marshal(E v) throws Exception {
        return null;
    }

    @Override
    public E unmarshal(String v) throws Exception {
        String str=v.replace('_', ' ');
        for (E e: enumClass.getEnumConstants()){
            if(e.name().equalsIgnoreCase(str)){
                return e;
            }
        }
        return null;
    }
}
