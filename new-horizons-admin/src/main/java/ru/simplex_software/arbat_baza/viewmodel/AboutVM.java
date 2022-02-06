package ru.simplex_software.arbat_baza.viewmodel;

import org.apache.log4j.spi.LoggerFactory;
import org.slf4j.Logger;
import org.zkoss.bind.annotation.AfterCompose;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Nick on 21.09.2016.
 */
public class AboutVM {
    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(AboutVM.class);
    String propertyFileName = "version.properties";
    String version = null;
    String data = null;
    InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propertyFileName) ;
    Properties properties = null;

    @AfterCompose
    public void afterCompose(){
        properties = getPropertiesFromFile(inputStream);
        if(properties!= null) {
            setData(properties);
            setVersion(properties);
        }
    }

    public Properties getPropertiesFromFile(InputStream in){
        Properties result = new Properties();
        if(in!= null){
            try {
                result.load(in);
            }catch (IOException e){
                LOG.info(e.getMessage(),e);
            }
        }

        return result;
    }

    public void setVersion(Properties prop){
        version = prop.getProperty("buildNumber");
    }
    public  void setData(Properties prop){
        data = prop.getProperty("buildDate");
    }


    public String getVersion() {
        return version;
    }

    public String getData() {
        return data;
    }
}
