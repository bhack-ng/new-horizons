package ru.simplex_software.arbat_baza;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.FullComposer;
import org.zkoss.zul.Menu;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Messagebox;
import ru.simplex_software.arbat_baza.dao.RealtyFilterDAO;
import ru.simplex_software.arbat_baza.init.FiasDataloader;
import ru.simplex_software.arbat_baza.model.RealtyFilter;
import ru.simplex_software.arbat_baza.model.RealtyObjectType;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**.*/
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class MenuController extends SelectorComposer implements FullComposer {
    private static final Logger LOG= LoggerFactory.getLogger(MenuController.class);
    @WireVariable
    private RealtyFilterDAO realtyFilterDAO;

    @WireVariable
    private FiasDataloader fiasDataloader;

    @WireVariable
    private AuthService authService;

    final static ExecutorService executorService = Executors.newFixedThreadPool(1);

    private boolean hasRole(String s){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            for(GrantedAuthority g:authentication.getAuthorities()){
                if(g.getAuthority().equals(s)){
                    return true;
                }
            }
            return false;
    }


    @Override
    public void doAfterCompose(Component comp) throws Exception {
        if(comp instanceof Menuitem){

            Menuitem menuitem = (Menuitem) comp;
            String value= menuitem.getValue();
            if(value.length()>0)
                comp.setVisible(hasRole(value));
        }
        if (comp instanceof Menu){
            Menu menu = (Menu) comp;
            String value = menu.getLabel();
            if(value.equals("Администрирование")){
                comp.setVisible(hasRole("ROLE_ADMIN"));
            }

        }
        super.doAfterCompose(comp);
    }



    @Listen("onClick = menuitem#aboutItm")
    public void onAbout(){
        HashMap params = new HashMap();
        Executions. createComponents("/about.zul", null, params);
    }

    @Listen("onClick = menuitem#commerceSaleFilter")
    public void commerceSaleFilter(){
        redirectOrRefresh(RealtyObjectType.COMMERCE_SALE);
    }

    @Listen("onClick = menuitem#commerceLeaseFilter")
    public void commerceLeaseFilter(){
        redirectOrRefresh(RealtyObjectType.COMMERCE_LEASE);
    }

    @Listen("onClick = menuitem#liveSaleFilter")
    public void liveSaleFilter(){
        redirectOrRefresh(RealtyObjectType.LIVE_SALE);
    }
    @Listen("onClick = menuitem#liveLeaseFilter")
    public void liveLeaseSaleFilter(){
        redirectOrRefresh(RealtyObjectType.LIVE_LEASE);
    }
    @Listen("onClick = menuitem#steadSaleFilter")
    public void steadSaleFilter(){
        redirectOrRefresh(RealtyObjectType.STEAD_SALE);
    }
    @Listen("onClick = menuitem#steadRentFilter")
    public void steadRentFilter(){
        redirectOrRefresh(RealtyObjectType.STEAD_RENT);
    }
    @Listen("onClick = menuitem#privateHouseRentFilter")
    public void privateHouseRentFilter(){
        redirectOrRefresh(RealtyObjectType.PRIVATE_HOUSE_RENT);
    }
    @Listen("onClick = menuitem#privateHouseSaleFilter")
    public void privateHouseSaleFilter(){
        redirectOrRefresh(RealtyObjectType.PRIVATE_HOUSE_SALE);
    }

    private void redirectOrRefresh(RealtyObjectType type) {
        RealtyFilter filter = authService.getLogginedAgent().getFilter();
        filter.setRealtyObjectType(type);
        realtyFilterDAO.saveOrUpdate(filter);

//        if(getPage().getDesktop().getRequestPath().startsWith("/index.zul")){
//            HashMap<String, Object> args = new HashMap<>();
//            args.put("type",type);
//            BindUtils.postGlobalCommand(null, null,"searchRealtyObjects", args);
//        }else{
            Executions.sendRedirect("/");
//        }
    }

    @Listen("onClick = menuitem#docItm")
    public void onDoc(){
        HashMap params = new HashMap();
        Executions. createComponents("/help.zul", null, params);
    }

    @Listen("onClick = menuitem#generateOffices")
    public void generateOffices() {
        Executions.createComponents("/generateOffices.zul", null, null);
    }

    @Listen("onClick = menuitem#cianReportItm")
    public void onCianReport(){
        HashMap params = new HashMap();
        Executions. createComponents("/cianReport.zul", null, params);
    }
    @Listen("onClick = menuitem#externalAgencyListItm")
    public void onEditExternalAgencyItm(){
        HashMap params = new HashMap();
        Executions. createComponents("/admin/externalAgencyList.zul", null, params);
    }

    @Listen("onClick = menuitem#importFiasItm")
    public void onImportFias(){
        HashMap params = new HashMap();

        if (!fiasDataloader.getIsDownloading()){
            Executions.createComponents("/WEB-INF/zul/admin/fiasSettings.zul",null,params);
        }
        else {
            Messagebox.show("В данный момент идет скачивание справочника. " +
                    "Настройки станут доступны после окончания загрузки");
        }
    }

    @Listen("onClick=menuitem#agencySettings")
    public void onAgencySettings(){
        HashMap params = new HashMap();
        Executions.createComponents("/WEB-INF/zul/admin/agencySettings.zul", null, params);
    }

    @Listen("onClick=menuitem#DBMessageLogItm")
    public void onDBMessageLogItm() {
        HashMap params = new HashMap();
        Executions.createComponents("/WEB-INF/zul/admin/DBMessageLog.zul", null, params);
    }

    @Listen("onClick=menuitem#excelImportItm")
    public void onExcelImportItm() {
        HashMap params = new HashMap();
        Executions.createComponents("/WEB-INF/zul/excelImport.zul", null, params);
    }

    @Listen("onClick=menuitem#integrationItm")
    public void onIntegrationItm() {
        Executions.createComponents("/integration.zul", null, null);
    }
}
