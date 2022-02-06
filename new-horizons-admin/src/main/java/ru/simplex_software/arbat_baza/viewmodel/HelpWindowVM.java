package ru.simplex_software.arbat_baza.viewmodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import ru.simplex_software.arbat_baza.Utils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * Окно помощи.
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class HelpWindowVM  {
    private static final Logger LOG= LoggerFactory.getLogger(MainWindowVM.class);
    private String[] listURL;


    @AfterCompose
    public void afterCompose(){
        HttpServletRequest request =(HttpServletRequest) Executions.getCurrent().getNativeRequest();
        listURL = new String[4];
        listURL[0]= Utils.getFullContextUrl(request)+"/cian/liveSale.xml";
        listURL[1]= Utils.getFullContextUrl(request)+"/cian/liveLease.xml";
        listURL[2]= Utils.getFullContextUrl(request)+"/cian/commerical.xml";
        listURL[3] = Utils.getFullContextUrl(request) + "/show-offices/index.html";
    }
    @Command
    public void cianReport() {
        HashMap params = new HashMap();
        Executions.createComponents("/cianReport.zul", null, params);
    }

    @Command
    public void tabOpen(@BindingParam("uRLNumber") int uRLNumber){
        Executions.getCurrent().sendRedirect(listURL[uRLNumber],"_blank");

    }

    public String[] getListURL() {
        return listURL;
    }
}
