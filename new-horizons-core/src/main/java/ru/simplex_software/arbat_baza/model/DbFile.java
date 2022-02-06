package ru.simplex_software.arbat_baza.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplex_software.zkutils.entity.LongIdPersistentEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

/**
 * "Файл" которых хранится в БД. для быстрого доступа можно узнать длинну и contentType.
 *  Access to file permitted for authenticated users.
 */
@Entity
public class DbFile extends LongIdPersistentEntity {
    private static final Logger LOG = LoggerFactory.getLogger(DbFile.class);
    private String contentType;
    private long length;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private DbFileContent content = new DbFileContent ();

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public DbFileContent getContent() {
        return content;
    }

    public void setContent(DbFileContent content) {
        this.content = content;
    }
}
