package ru.simplex_software.arbat_baza.viewmodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import ru.simplex_software.arbat_baza.dao.ContactsOfOwnerDAO;
import ru.simplex_software.arbat_baza.dao.RealtyObjectDAO;
import ru.simplex_software.arbat_baza.model.ContactsOfOwner;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.zkutils.DetachableModel;

/**
 * Панель контактов собственника.
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ContactsOfOwnerPanelVM {
    private static final Logger LOG= LoggerFactory.getLogger(ContactsOfOwnerPanelVM.class);
    @DetachableModel
    private RealtyObject realtyObject;
    private Component editWin;
    private ContactsOfOwner contactsOfOwner = new ContactsOfOwner();

    @WireVariable
    private RealtyObjectDAO realtyObjectDAO;
    @WireVariable
    private ContactsOfOwnerDAO contactsOfOwnerDAO;
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view
            ,@ExecutionArgParam("realtyObject") RealtyObject realtyObject) {
        this.realtyObject=realtyObject;
        editWin=view;
        contactsOfOwner.setRealtyObject(realtyObject);

    }

    @Command @NotifyChange("*")
    public void addContacts(){
        contactsOfOwnerDAO.saveOrUpdate(contactsOfOwner);
        realtyObject.getContactsOfOwners().add(contactsOfOwner);
        contactsOfOwner = new ContactsOfOwner();
        contactsOfOwner.setRealtyObject(realtyObject);
    }
    @Command @NotifyChange("*")
    public void delete(@BindingParam("contact") ContactsOfOwner contact){
        ContactsOfOwner o = contactsOfOwnerDAO.get(contact.getPrimaryKey());
        realtyObject.getContactsOfOwners().remove(o);
        contactsOfOwnerDAO.delete(o);
        realtyObjectDAO.saveOrUpdate(realtyObject);
    }
    public ContactsOfOwner getContactsOfOwner() {
        return contactsOfOwner;
    }

    public void setContactsOfOwner(ContactsOfOwner contactsOfOwner) {
        this.contactsOfOwner = contactsOfOwner;
    }

    public RealtyObject getRealtyObject() {
        return realtyObject;
    }
}
