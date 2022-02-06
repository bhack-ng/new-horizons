package ru.simplex_software.arbat_baza.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplex_software.zkutils.entity.LongIdPersistentEntity;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;

/**
 * Содержимое файла.
 */
@Entity
public class DbFileContent extends LongIdPersistentEntity{
    private static final Logger LOG = LoggerFactory.getLogger(DbFileContent.class);
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
