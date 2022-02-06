package ru.simplex_software.arbat_baza.xml.yandex.parser;

/**
 * Created by dima on 11.07.16.
 */
public class RealtyFeedHanler extends AbHandler{
    public RealtyFeedHanler() {
        tagName="realty-feed";
        handlerMap.put("offer", new OfferHandler());
        handlerMap.put("generation-date", new GenerationDateHandler());
    }
}
