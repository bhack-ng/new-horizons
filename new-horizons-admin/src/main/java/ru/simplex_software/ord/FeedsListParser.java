package ru.simplex_software.ord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.simplex_software.arbat_baza.model.FeedType;
import ru.simplex_software.arbat_baza.model.odor.ExternalAgency;
import ru.simplex_software.arbat_baza.model.odor.Feed;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 * Parser of list of feeds of agency.
 */

public class FeedsListParser {
    public static long FEEDS_XML_SIZE_LIMIT = 16384;
    private static final Logger LOG = LoggerFactory.getLogger(FeedsListParser.class);


    public List<Feed> parse(ExternalAgency externalAgency) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
        List<Feed> result = null;
        result = new LinkedList<>();
        final URL url = new URL(externalAgency.getSite() + "feeds.xml");
        final InputStream is = url.openStream();
        LimitedInputStream lis = new LimitedInputStream(is, FEEDS_XML_SIZE_LIMIT) {
            @Override
            protected void raiseError(long pSizeMax, long pCount) throws IOException {
                throw new SizeLimitException("input stream too big");
            }
        };


        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();


        Document doc = builder.parse(lis);
        final Element root = doc.getDocumentElement();

        XPath xPath = XPathFactory.newInstance().newXPath();

        NodeList list = (NodeList) xPath.evaluate("//feed", root, XPathConstants.NODESET);
        for (int i = 1; i <= list.getLength(); i++) {
            final Node item = list.item(i);
            String ft = (String) xPath.evaluate("/feeds/feed[" + i + "]/feedType", root, XPathConstants.STRING);
            String fUrl = (String) xPath.evaluate("/feeds/feed[" + i + "]/url", root, XPathConstants.STRING);
            String description = (String) xPath.evaluate("/feeds/feed[" + i + "]/description", root, XPathConstants.STRING);
            final Feed feed = new Feed();
            feed.setFeedType(FeedType.valueOf(ft));
            if(StringUtils.hasLength(externalAgency.getSite())){
                feed.setUrl(externalAgency.getSite()+fUrl);
            }
            feed.setDescription(description);
            feed.setExternalAgency(externalAgency);
            result.add(feed);
        }
        return result;
    }
}
