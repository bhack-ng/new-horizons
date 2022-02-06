package ru.simplex_software.arbat_baza;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * .
 */
public class Utils {
    private static final Logger LOG = LoggerFactory.getLogger(Utils.class);

    @NotNull
    public static String getFullContextUrl(HttpServletRequest request) {
        String urlStr= request.getScheme()+"://"+request.getServerName();
        if(request.getServerPort()!=80){
            urlStr=urlStr+":"+request.getServerPort();
        }
        urlStr=urlStr+request.getContextPath();
        return urlStr;
    }
}
