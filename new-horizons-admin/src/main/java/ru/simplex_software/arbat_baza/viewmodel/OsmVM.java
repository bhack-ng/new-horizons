package ru.simplex_software.arbat_baza.viewmodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.openlayers.Openlayers;
import org.zkoss.openlayers.base.Icon;
import org.zkoss.openlayers.base.LonLat;
import org.zkoss.openlayers.base.Pixel;
import org.zkoss.openlayers.base.Projection;
import org.zkoss.openlayers.base.Size;
import org.zkoss.openlayers.control.LayerSwitcher;
import org.zkoss.openlayers.layer.Markers;
import org.zkoss.openlayers.layer.OSM;
import org.zkoss.openlayers.marker.Marker;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import ru.simplex_software.arbat_baza.GeocodeService;
import ru.simplex_software.arbat_baza.model.GeoCoordinates;
import ru.simplex_software.arbat_baza.model.RealtyObject;

import java.util.Map;
import java.util.Set;

/**
 * .
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class OsmVM {
    private static final Logger LOG = LoggerFactory.getLogger(OsmVM.class);
    @Wire
    private Openlayers map;
    private Markers markers;


    @WireVariable
    private GeocodeService geocodeService;

    private Window geoWin;
    private Map<String, GeoCoordinates> coord;
    private Map.Entry<String, GeoCoordinates> selectedCoord;
    private RealtyObject realtyObject;

    @AfterCompose
    public void doAfterCompose(@ContextParam(ContextType.VIEW) Window view,
                               @ExecutionArgParam("realtyObject")RealtyObject realtyObject) {
        Selectors.wireComponents(view,this,true);
        this.realtyObject=realtyObject;
        geoWin=view;
        try {
            this.coord = geocodeService.getCoordinates(realtyObject.getAddress());
        } catch (Exception e) {
            LOG.error(e.getMessage(),e);
            Messagebox.show(e.getMessage());
        }

        map.addLayer(new OSM("Simple OSM Map"));
        markers = new Markers("Markers");
        map.addLayer(markers);
        map.addControl(new LayerSwitcher());
        map.zoomToMaxExtent();

    }

    public Set<Map.Entry<String, GeoCoordinates>> getCoord() {
        return coord.entrySet();
    }

    public Map.Entry<String, GeoCoordinates> getSelectedCoord() {
        return selectedCoord;
    }

    public void setSelectedCoord(Map.Entry<String, GeoCoordinates> selectedCoord) {
        this.selectedCoord = selectedCoord;
        double longitude = selectedCoord.getValue().getLongitude();
        double latitude = selectedCoord.getValue().getLatitude();

        markers.clearMarkers();
        Size size = new Size(21,25);
        Pixel offset = new Pixel(-(size.getWidth()/2), -size.getHeight());
        Icon icon = new Icon(Executions.getCurrent().getContextPath()+"/img/marker.png",size,offset);
        markers.addMarker(new Marker(new LonLat(longitude, latitude).transform(
                new Projection("EPSG:4326"),
                map.getProjection()),icon));


        map.setCenter(new LonLat(longitude, latitude).transform(
                new Projection("EPSG:4326"),
                map.getProjection()), 18, false, false);


    }
    @Command
    public void save(){
        geoWin.detach();
        Events.postEvent("onChangeGeo", geoWin, selectedCoord);
    }
}
