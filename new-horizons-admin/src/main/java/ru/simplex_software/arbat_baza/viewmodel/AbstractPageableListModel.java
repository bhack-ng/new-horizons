package ru.simplex_software.arbat_baza.viewmodel;

import org.zkoss.zul.AbstractListModel;

import java.util.HashMap;
import java.util.List;

public abstract class AbstractPageableListModel<T> extends AbstractListModel<T> {

    protected static int uiPageSize = 3;
    protected static int cachePageSize = uiPageSize;

    protected int elementsCount;
    protected HashMap<Integer, Long> idMap;

    protected AbstractPageableListModel() {
        this.idMap = new HashMap<>();
        this.elementsCount = getElementsCount();
    }

    /**
     * Возвращает элемент по Id.
     */
    abstract T getElementById(Long id);

    /**
     * Возвращает количество элементов.
     */
    abstract int getElementsCount();

    /**
     * Возвращает список id для кеширования.
     */
    abstract List<Long> getIds(int cacheSize, int index);

    /**
     * Возвращает количество элементов в списке.
     */
    public int getSize() {
        return elementsCount;
    }

    /**
     * Обновление модели.
     */
    public void refreshModel() {
        elementsCount = getElementsCount();
        idMap = new HashMap<>();
    }

    /**
     * Возвращает элемент списка по его индексу.
     */
    @Override
    public T getElementAt(int index) {

        if (!idMap.containsKey(index)) {
            List<Long> ids = getIds(cachePageSize, index);

            int listIndex = index;
            for (Long id : ids) {
                idMap.put(listIndex++, id);
            }
        }

        return getElementById(idMap.get(index));
    }

    public int getPageSize() {
        return uiPageSize;
    }

    public void setPageSize(int pageSize) {
        uiPageSize = pageSize;
    }

    public void setCachePageSize(int pageSize) {
        cachePageSize = pageSize;
    }
}
