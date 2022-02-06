package ru.simplex_software.arbat_baza.model;

import ru.simplex_software.zkutils.entity.LongIdPersistentEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Комментарии к объекту недвижимости.
 */
@Entity
public class Comment extends LongIdPersistentEntity{
    @NotNull
    @Column(length = 1024) @Size(max=1024, min=1)
    private String text;
    private Date created = new Date();
    @ManyToOne
    private RealtyObject realtyObject;
    @ManyToOne
    private Agent author;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public RealtyObject getRealtyObject() {
        return realtyObject;
    }

    public void setRealtyObject(RealtyObject realtyObject) {
        this.realtyObject = realtyObject;
    }

    public Agent getAuthor() {
        return author;
    }

    public void setAuthor(Agent author) {
        this.author = author;
    }
}
