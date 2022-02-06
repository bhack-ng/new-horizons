package ru.simplex_software.arbat_baza.xml.yandex.jaxb.adapters;

import ru.simplex_software.arbat_baza.xml.yandex.jaxb.Category;

/**
 * Created by dima on 11.07.16.
 */
public class CategoryAdapter extends MultiNameEnumAdapter {
    public CategoryAdapter() {
        enumClass=Category.class;
    }
}
