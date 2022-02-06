package ru.simplex_software.arbat_baza.xml.yandex.jaxb.adapters;

import ru.simplex_software.arbat_baza.xml.yandex.jaxb.Multi;

import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Wrap multiple @XmlEnumValue to @Multi. It is possible use XmlEnumValue without @Multi
 */
public abstract class MultiNameEnumAdapter<E extends Enum> extends XmlAdapter<String, E> {
    protected Class<E> enumClass;

    @Override
    public String marshal(E v) throws Exception {
        final Multi list= v.getClass().getField(v.name()).getAnnotation(Multi.class);
        if(list!=null  && list.value().length>0){
            return list.value()[0].value();
        }
        final String value = v.getClass().getField(v.name()).getAnnotation(XmlEnumValue.class).value();
        return value;
    }

    @Override
    public E unmarshal(String v) throws Exception {
        final E[] enumConstants = enumClass.getEnumConstants();
        for(E en:enumConstants){

            final Multi list= en.getClass().getField(en.name()).getAnnotation(Multi.class);
            if(list!=null){
                for(XmlEnumValue ann:list.value()){
                    if(ann.value().equalsIgnoreCase(v)){
                        return en;
                    }
                }

            }

            final XmlEnumValue ann= enumClass.getField(en.name()).getAnnotation(XmlEnumValue.class);
            if(ann!= null && ann.value().equalsIgnoreCase(v)){
                return en;
            }
        }
        return null;
    }
}
