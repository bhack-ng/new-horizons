package ru.simplex_software.arbat_baza.xml.yandex.jaxb.adapters;

import org.springframework.format.datetime.standard.InstantFormatter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Created by dima on 13.07.16.
 */
public class InstantAdapter extends XmlAdapter<String, Instant> {
    @Override
    public String marshal(Instant v) throws Exception {
        return DateTimeFormatter.ISO_OFFSET_TIME.toFormat().format(v);
    }

    @Override
    public Instant unmarshal(String v) throws Exception {
        return DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(v,Instant::from);
    }
}
