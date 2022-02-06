package ru.simplex_software.arbat_baza.viewmodel;

import org.zkoss.zk.ui.Executions;
import ru.simplex_software.arbat_baza.Utils;

import javax.servlet.http.HttpServletRequest;

public class IntegrationVM {
    public String getPath() {
        HttpServletRequest request = (HttpServletRequest) Executions.getCurrent().getNativeRequest();
        return Utils.getFullContextUrl(request);
    }
}
