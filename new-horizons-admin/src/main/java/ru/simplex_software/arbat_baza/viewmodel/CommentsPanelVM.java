package ru.simplex_software.arbat_baza.viewmodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;
import ru.simplex_software.arbat_baza.AuthService;
import ru.simplex_software.arbat_baza.dao.CommentDAO;
import ru.simplex_software.arbat_baza.dao.ContactsOfOwnerDAO;
import ru.simplex_software.arbat_baza.dao.RealtyObjectDAO;
import ru.simplex_software.arbat_baza.model.Comment;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.zkutils.DetachableModel;

/**
 * .
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class CommentsPanelVM {
    private static final Logger LOG= LoggerFactory.getLogger(CommentsPanelVM.class);
    @DetachableModel
    private RealtyObject realtyObject;
    private Component editWin;
    private Comment comment = new Comment ();

    @WireVariable
    private RealtyObjectDAO realtyObjectDAO;
    @WireVariable
    private ContactsOfOwnerDAO contactsOfOwnerDAO;
    @WireVariable
    private AuthService authService;
    @WireVariable
    private CommentDAO commentDAO;
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view
            ,@ExecutionArgParam("realtyObject") RealtyObject realtyObject) {
        this.realtyObject=realtyObjectDAO.get(realtyObject.getPrimaryKey());
        editWin=view;
        comment.setRealtyObject(realtyObject);
        comment.setAuthor(authService.getLogginedAgent());

    }
    @Command @NotifyChange("*")
    public  void addComment(){
        if( ! StringUtils.hasLength(comment.getText())){
            Messagebox.show("Текст комментапия не может быть пустым");
        }
        commentDAO.saveOrUpdate(comment);
        realtyObject.getComments().add(comment);
//        commerceDAO.saveOrUpdate(commerce);

        comment = new Comment ();
        comment.setRealtyObject(realtyObject);
        comment.setAuthor(authService.getLogginedAgent());
    }


    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public RealtyObject getRealtyObject() {
        return realtyObject;
    }
}
