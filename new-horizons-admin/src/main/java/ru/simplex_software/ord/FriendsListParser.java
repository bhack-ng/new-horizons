package ru.simplex_software.ord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.ParseException;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.simplex_software.arbat_baza.model.FeedType;
import ru.simplex_software.arbat_baza.model.odor.Feed;
import ru.simplex_software.ord.model.Friend;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

/**
 * Parse recommended agency (friends).
 */
public class FriendsListParser {
    public static long SIZE_LIMIT=1024*1024;
    private static final Logger LOG = LoggerFactory.getLogger(FriendsListParser.class);
    public List<Friend> parse(String urlStr)  throws IOException, ParseException,
                                                        ParserConfigurationException, XPathException,SAXException{
        if(!StringUtils.hasLength(urlStr) ||!urlStr.toLowerCase().startsWith("http")){
            throw new MalformedURLException("incorrect url="+urlStr);
        }
        final URL url = new URL(urlStr);
        final InputStream is = url.openStream();
        LimitedInputStream lis = new LimitedInputStream(is, SIZE_LIMIT) {
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

        List<Friend> arrayList= new LinkedList<>();

        /**получаем список элементов вложенных в тег friends(список с дружескими агентствами).*/
        NodeList list=(NodeList)xPath.evaluate("//friend", root, XPathConstants.NODESET);

        /**парсинг всех дружеских агентств.*/
        for(int i=1;i<=list.getLength();i++){
            String name = (String)xPath.evaluate("/friends/friend["+i+"]/name", root, XPathConstants.STRING);
            String fUrl = (String)xPath.evaluate("/friends/friend["+i+"]/url", root, XPathConstants.STRING);
            Friend friend = new Friend();
            friend.setName(name);
            friend.setUrl(fUrl);
            /**получим список элементов вложенных в тег feed.(список фиидов конкретного агентства)*/
            NodeList feedListNodes=(NodeList)xPath.evaluate("/friends/friend["+i+"]/feed", root, XPathConstants.NODESET);

            /**парсинг кождого фида в дружеском агентстве.*/
            for(int j=1;j<=list.getLength();j++) {
                Feed feed = new Feed();

                String feedType = (String)xPath.evaluate("/friends/friend["+i+"]/feeds/feed["+j+"]/feedType", root, XPathConstants.STRING);
                if (feedType.equals(FeedType.CIAN_COMMERICAL.toString())) feed.setFeedType(FeedType.CIAN_COMMERICAL);
                else if(feedType.equals(FeedType.CIAN_LIVE_RENT.toString())) feed.setFeedType(FeedType.CIAN_LIVE_RENT);
                else if(feedType.equals(FeedType.CIAN_LIVE_SALE.toString()))feed.setFeedType(FeedType.CIAN_LIVE_SALE);
                else continue;

                String feedURL = (String)xPath.evaluate("/friends/friend["+i+"]/feeds/feed["+j+"]/url", root, XPathConstants.STRING);
                feed.setUrl(feedURL);

                String description = (String)xPath.evaluate("/friends/friend["+i+"]/feeds/feed["+j+"]/description", root, XPathConstants.STRING);
                feed.setDescription(description);

                friend.getFeedList().add(feed);
            }

            arrayList.add(friend);

        }
        return arrayList;


    }
}
