package ru.simplex_software.arbat_baza.model;

import ru.simplex_software.zkutils.entity.LongIdPersistentEntity;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;

/**
 * Content on photo file.
 */
@Entity
public class PhotoData extends LongIdPersistentEntity{
    @Lob
    @Basic(fetch= FetchType.LAZY)
    private byte[] data;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
