package ru.simplex_software.arbat_baza.viewmodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.arbat_baza.model.commerce.Commerce;
import ru.simplex_software.arbat_baza.model.live.LiveLeaseRealty;
import ru.simplex_software.arbat_baza.model.live.LiveSaleRealty;
import ru.simplex_software.arbat_baza.model.privateHouses.PrivateHouseRent;
import ru.simplex_software.arbat_baza.model.privateHouses.PrivateHouseSale;
import ru.simplex_software.arbat_baza.model.stead.SteadRent;
import ru.simplex_software.arbat_baza.model.stead.SteadSale;
import ru.simplex_software.arbat_baza.viewmodel.commerce.CommerceFlatDescRendererRu;
import ru.simplex_software.arbat_baza.viewmodel.live.LiveLeaseDescRendererRu;
import ru.simplex_software.arbat_baza.viewmodel.live.LiveSaleDescRendererRu;

/**
 * Создаёт строковое предстваление колоенки "Параметры Объекта".
 */

public class FlatDescriptionRenderer {
    private static final Logger LOG = LoggerFactory.getLogger(FlatDescriptionRenderer.class);
    public String toLocolizedString(RealtyObject realtyObject){
        return " abstract";
    }
    public CommerceFlatDescRendererRu getRenderer(Commerce com){
      return new CommerceFlatDescRendererRu();
    }
    public LiveLeaseDescRendererRu getRenderer(LiveLeaseRealty liveRealty){
        return new LiveLeaseDescRendererRu();
    }
    public LiveSaleDescRendererRu getRenderer(LiveSaleRealty liveSaleRealty){
        return new LiveSaleDescRendererRu();
    }
    public SteadSaleDescRendererRu getRenderer(SteadSale steadSale){
        return new SteadSaleDescRendererRu();
    }
    public PrivateHouseSaleDescRendererRu getRenderer(PrivateHouseSale privateHouseSale){
        return new PrivateHouseSaleDescRendererRu();
    }
    public PrivateHouseRentDescRendererRu getRenderer(PrivateHouseRent privateHouseRent){
        return new PrivateHouseRentDescRendererRu();
    }
    public SteadRentDescRendererRu getRenderer(SteadRent steadRent){
        return new SteadRentDescRendererRu();
    }

    public String nullChk(Object obj){
        if(obj==null){
            return "-";
        }
        return obj.toString();

    }
}
