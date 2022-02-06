package ru.simplex_software.arbat_baza.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplex_software.zkutils.entity.LongIdPersistentEntity;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * Суперкласс опций.
 */
@Entity @Inheritance(strategy = InheritanceType.JOINED)
public class AbstractOptions extends LongIdPersistentEntity {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractOptions.class);
}
