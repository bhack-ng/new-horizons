package ru.simplex_software.arbat_baza.viewmodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import ru.simplex_software.arbat_baza.dao.RealtyObjectDAO;
import ru.simplex_software.arbat_baza.dao.RecommendationDAO;
import ru.simplex_software.arbat_baza.model.RealtyObject;
import ru.simplex_software.arbat_baza.model.Recommendation;
import ru.simplex_software.zkutils.DetachableModel;

/**
 * .
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class RecommendationsPanelVM {
    private static final Logger LOG= LoggerFactory.getLogger(RecommendationsPanelVM.class);
    @DetachableModel
    private RealtyObject realtyObject;
    private Component editWin;
    private Recommendation recommendation= new Recommendation();

    @WireVariable
    private RealtyObjectDAO realtyObjectDAO;
    @WireVariable
    private RecommendationDAO recommendationDAO;
    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view
            ,@ExecutionArgParam("realtyObject") RealtyObject realtyObject) {
        this.realtyObject=realtyObject;
        editWin=view;
        recommendation.setRealtyObject(realtyObject);

    }

    @Command
    @NotifyChange("*")
    public void add(){
        recommendationDAO.saveOrUpdate(recommendation);
        realtyObject.getRecommendations().add(recommendation);
        recommendation = new Recommendation();
        recommendation.setRealtyObject(realtyObject);
    }
    @Command @NotifyChange("*")
    public void delete(@BindingParam("recommendation") Recommendation rec){
        Recommendation o = recommendationDAO.get(rec.getPrimaryKey());
        realtyObject.getRecommendations().remove(o);
        recommendationDAO.delete(o);
        realtyObjectDAO.saveOrUpdate(realtyObject);
    }

    public Recommendation getRecommendation() {
        return recommendation;
    }


    public RealtyObject getRealtyObject() {
        return realtyObject;
    }
}
