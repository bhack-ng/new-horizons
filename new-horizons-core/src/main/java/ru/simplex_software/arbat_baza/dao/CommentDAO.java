package ru.simplex_software.arbat_baza.dao;

import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import ru.simplex_software.arbat_baza.model.Comment;

import java.util.List;

/**
 * .
 */
public interface CommentDAO extends Dao<Comment,Long> {
    @Finder(query = "from Comment")
    public List<Comment> findAll();
}
