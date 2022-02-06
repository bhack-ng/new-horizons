package ru.simplex_software.arbat_baza.xml.yandex.jaxb;

import ru.simplex_software.arbat_baza.xml.yandex.jaxb.adapters.AgentCategoryAdapter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

/**
 * Created by dima on 12.07.16.
 */
@XmlRootElement
public class SalesAgent {
    private String name;
    @XmlElement(name = "phone") @Size(min = 1)
    List<String> phoneList;

    @XmlElement @XmlJavaTypeAdapter(value = AgentCategoryAdapter.class)
    private Category category;
    private String organization;
    @XmlElement(name = "agency-id")
    private String agencyId;


    private String url;
    private String email;


    private String photo;
    private String partner;


    public static enum Category{
        @Multi({@XmlEnumValue("владелец"), @XmlEnumValue("owner") })OWNER,
        @Multi({@XmlEnumValue("агентство") , @XmlEnumValue("agency") })AGENCY,
        @Multi({@XmlEnumValue("застройщик") , @XmlEnumValue("developer") })DEVELOPER,
    }

}
