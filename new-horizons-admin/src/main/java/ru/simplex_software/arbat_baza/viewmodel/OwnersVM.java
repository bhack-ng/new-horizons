package ru.simplex_software.arbat_baza.viewmodel;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;
import ru.simplex_software.arbat_baza.dao.ClientDAO;
import ru.simplex_software.arbat_baza.dao.ClientTaskDAO;
import ru.simplex_software.arbat_baza.model.clients.Client;
import ru.simplex_software.arbat_baza.model.clients.NaturalClient;

import java.util.List;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class OwnersVM {

    @WireVariable
    private ClientDAO clientDAO;

    @WireVariable
    private ClientTaskDAO clientTaskDAO;

    private Filter filter = new Filter();
    private AbstractPageableListModel<Client> ownersListModel;

    @AfterCompose
    public void afterCompose() {
        // Инициализация модели.
        ownersListModel = new AbstractPageableListModel<Client>() {
            @Override
            Client getElementById(Long id) {
                return clientDAO.get(id);
            }

            @Override
            int getElementsCount() {
                return (int) clientDAO.countByFilter(filter.getName(), filter.getEmail(),
                        filter.getPhone(), filter.getMobilePhone(), filter.getInn(),
                        filter.getOgrn(), filter.getSite(),
                        filter.isNatural(), filter.isJuridical());
            }

            @Override
            List<Long> getIds(int cacheSize, int index) {
                return clientDAO.findIdsByFilter(filter.getName(), filter.getEmail(),
                        filter.getPhone(), filter.getMobilePhone(), filter.getInn(),
                        filter.getOgrn(), filter.getSite(),
                        filter.isNatural(), filter.isJuridical(),
                        cacheSize, index);
            }
        };
        ownersListModel.setPageSize(10);
    }

    /**
     * Добавление физического владельца.
     */
    @Command
    public void addNaturalOwner() {
        Executions.sendRedirect("/manageNaturalClient.zul?clientId=");
    }

    /**
     * Добавление юридического владельца.
     */
    @Command
    public void addJuridicalOwner() {
        Executions.sendRedirect("/manageJuridicalClient.zul?clientId=");
    }

    /**
     * Редактирование владельца.
     */
    @Command
    public void editOwner(@BindingParam("owner") Client client) {
        String url = (client instanceof NaturalClient) ? "/manageNaturalClient.zul"
                : "/manageJuridicalClient.zul";
        url += "?clientId=" + client.getId() + "&viewMode=false";

        Executions.sendRedirect(url);
    }

    /**
     * Просмотр владельца.
     */
    @Command
    public void viewOwner(@BindingParam("owner") Client client) {
        String url = (client instanceof NaturalClient) ? "/manageNaturalClient.zul"
                : "/manageJuridicalClient.zul";
        url += "?clientId=" + client.getId() + "&viewMode=true";

        Executions.sendRedirect(url);
    }

    /**
     * Удаление владельца.
     */
    @Command
    public void deleteOwner(@BindingParam("ownerId") long clientId) {

        Messagebox.show("Вы точно хотите удалить клиента?", "Удаление клиента",
                Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, e -> {
                    if (Messagebox.ON_YES.equals(e.getName())) {

                        // Получение клиента.
                        Client client = clientDAO.get(clientId);

                        // Удаление клиента.
                        client.setDeleted(true);
                        clientDAO.saveOrUpdate(client);

                        // Фильтрация списка.
                        ownersListModel.refreshModel();
                        BindUtils.postNotifyChange(null, null, this, "ownersListModel");
                    }
                });
    }

    /**
     * Фильтрует список владельцев.
     */
    @Command
    @NotifyChange("ownersListModel")
    public void applyFilter() {
        ownersListModel.refreshModel();
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public AbstractPageableListModel getOwnersListModel() {
        return ownersListModel;
    }

    /**
     * Класс для фильтрации.
     */
    public static class Filter {
        private boolean natural = true;
        private boolean juridical = true;
        private String name;
        private String site;
        private String phone;
        private String mobilePhone;
        private String email;
        private String comment;
        private String inn;
        private String ogrn;

        public boolean isNatural() {
            return natural;
        }

        public void setNatural(boolean natural) {
            this.natural = natural;
        }

        public boolean isJuridical() {
            return juridical;
        }

        public void setJuridical(boolean juridical) {
            this.juridical = juridical;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getMobilePhone() {
            return mobilePhone;
        }

        public void setMobilePhone(String mobilePhone) {
            this.mobilePhone = mobilePhone;
        }

        public String getSite() {
            return site;
        }

        public void setSite(String site) {
            this.site = site;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getInn() {
            return inn;
        }

        public void setInn(String inn) {
            this.inn = inn;
        }

        public String getOgrn() {
            return ogrn;
        }

        public void setOgrn(String ogrn) {
            this.ogrn = ogrn;
        }
    }
}
