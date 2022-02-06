package ru.simplex_software.arbat_baza.xml.yandex.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.Instant;

/**
 * Created by dima on 12.07.16.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Metro {
    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "time-on-foot")
    private String  timeOnFoot;
    @XmlElement(name = "time-on-transport")
    private String timeOnTransport;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimeOnFoot() {
        return timeOnFoot;
    }

    public void setTimeOnFoot(String timeOnFoot) {
        this.timeOnFoot = timeOnFoot;
    }

    public String getTimeOnTransport() {
        return timeOnTransport;
    }

    public void setTimeOnTransport(String timeOnTransport) {
        this.timeOnTransport = timeOnTransport;
    }
}
