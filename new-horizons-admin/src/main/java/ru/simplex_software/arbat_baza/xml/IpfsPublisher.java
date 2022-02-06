package ru.simplex_software.arbat_baza.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplex_software.arbat_baza.AuthService;
import ru.simplex_software.arbat_baza.dao.LiveSaleRealtyDAO;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.arbat_baza.model.live.LiveSaleRealty;

import javax.annotation.Resource;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.util.*;

public class IpfsPublisher {
    private static final Logger LOG = LoggerFactory.getLogger(IpfsPublisher.class);
    @Resource
    private LiveSaleExportServlet liveSaleExportServlet;

    @Resource
    private LiveSaleRealtyDAO liveSaleRealtyDAO;

    @Resource
    private AuthService authService;

    public void publishAll() throws Exception{
        List<LiveSaleRealty> liveRealtyList = liveSaleRealtyDAO.findToSiteExport();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter pw=new PrintWriter(new OutputStreamWriter(baos));
        XMLOutputFactory output = XMLOutputFactory.newInstance();
        XMLStreamWriter xmlWriter = output.createXMLStreamWriter(pw);


        XmlUtils.outputAgent.set(false);
        liveSaleExportServlet.liveRealtyList=liveRealtyList;
        liveSaleExportServlet.writeXml(xmlWriter,"/site/liveSale.xml");

        byte[] bXml= baos.toByteArray();
        LOG.warn(new String(bXml));

    }


}
