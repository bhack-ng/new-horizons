package ru.simplex_software.arbat_baza.viewmodel;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import ru.simplex_software.arbat_baza.dao.FloorSchemeDAO;
import ru.simplex_software.arbat_baza.dao.RealtyObjectDAO;
import ru.simplex_software.arbat_baza.model.FloorScheme;
import ru.simplex_software.arbat_baza.model.RealtyObject;

import java.util.List;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ManageFloorSchemesVM {

    @WireVariable
    private FloorSchemeDAO floorSchemeDAO;

    @WireVariable
    private RealtyObjectDAO realtyObjectDAO;

    /**
     * Возвращает все планы.
     */
    public List<FloorScheme> getFloorSchemes() {
        return floorSchemeDAO.findAll();
    }

    /**
     * Удаление плана.
     */
    @Command
    public void deleteFloorScheme(@BindingParam("id") long id) {

        // Получение плана.
        FloorScheme scheme = floorSchemeDAO.get(id);

        // Удаление ссылок.
        for (RealtyObject realtyObject : scheme.getRealtyObjects()) {
            realtyObject.setFloorScheme(null);
            realtyObject.setFloorSchemeCoordinates(null);
        }

        // Удаление плана.
        floorSchemeDAO.delete(scheme);

        // Перезагрузка страницы.
        Executions.getCurrent().sendRedirect("");
    }
}
