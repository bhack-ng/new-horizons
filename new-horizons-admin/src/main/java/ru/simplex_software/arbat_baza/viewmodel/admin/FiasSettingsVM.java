package ru.simplex_software.arbat_baza.viewmodel.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import ru.simplex_software.arbat_baza.init.FiasDataloader;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class FiasSettingsVM{

    @WireVariable
    private FiasDataloader fiasDataloader;
    private static final Logger LOG= LoggerFactory.getLogger(FiasSettingsVM.class);
    final static ExecutorService executorService = Executors.newFixedThreadPool(1);
    public Window win;
    public  String fiasURL;
    public String tmpDir;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window view){
        this.win =view;
        fiasURL = fiasDataloader.getFiasURL();
        tmpDir = fiasDataloader.getTmpDirPath();
    }
    @Command
    public void fillFullUnpacked(){
        executorService.submit(()->{
            try {
                fiasDataloader.fillFullUnpacked();
                LOG.info("unpacked file downloaded and parsed");

            } catch (Exception e) {
                LOG.error(e.getMessage(),e);
            }
        });
        LOG.info("");
        Messagebox.show("Процесс загрузки запущен");
        win.detach();
    }




    @Command
    public void downloadFiasCatalog(){
        fiasDataloader.setTmpDirPath(tmpDir);
        fiasDataloader.setFiasURL(fiasURL);
        executorService.submit(()->{
            try {
                fiasDataloader.fillFullDatabase();
                LOG.info("file downloaded and parsed");

            } catch (Exception e) {
                LOG.error(e.getMessage(),e);
            }
        });
        LOG.info("");
        Messagebox.show("Процесс загрузки запущен");
        win.detach();
    }

    public String getFiasURL() {
        return fiasURL;
    }

    public void setFiasURL(String fiasURL) {
        this.fiasURL = fiasURL;
    }

    public String getTmpDir() {
        return tmpDir;
    }

    public void setTmpDir(String tmpDir) {
        this.tmpDir = tmpDir;
    }
}
