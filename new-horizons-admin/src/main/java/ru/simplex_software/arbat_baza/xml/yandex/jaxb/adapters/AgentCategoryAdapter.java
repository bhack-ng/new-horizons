package ru.simplex_software.arbat_baza.xml.yandex.jaxb.adapters;

import ru.simplex_software.arbat_baza.xml.yandex.jaxb.SalesAgent;

/**
 * Created by dima on 12.07.16.
 */
public class AgentCategoryAdapter extends MultiNameEnumAdapter {
    public AgentCategoryAdapter() {
        this.enumClass = SalesAgent.Category.class;
    }
}
