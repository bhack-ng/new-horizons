package ru.simplex_software.arbat_baza.viewmodel;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zul.Window;
import ru.simplex_software.arbat_baza.model.clients.NaturalClient;
import ru.simplex_software.ord.FormNaturalClientValidator;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ManageNaturalClientVM extends ManageClient<NaturalClient> {

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window window) {
        super.afterCompose(window);

        String clientId = Executions.getCurrent().getParameter("clientId");
        this.client = (clientId.isEmpty()) ? new NaturalClient() :
                (NaturalClient) clientDAO.get(Long.parseLong(clientId));

        this.validator = new FormNaturalClientValidator(clientDAO, client);
    }
}
