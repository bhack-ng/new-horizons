package ru.simplex_software.arbat_baza.viewmodel;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import ru.simplex_software.arbat_baza.dao.FloorSchemeDAO;
import ru.simplex_software.arbat_baza.dao.PhotoDataDAO;
import ru.simplex_software.arbat_baza.dao.RealtyObjectDAO;
import ru.simplex_software.arbat_baza.model.FloorScheme;
import ru.simplex_software.arbat_baza.model.PhotoData;
import ru.simplex_software.arbat_baza.model.RealtyObject;

import java.util.List;

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class BindRealtyObjectToFloorSchemeVM {

    private static final int MAX_FILE_SIZE = 10 * 1024 * 1024;

    @WireVariable
    private RealtyObjectDAO realtyObjectDAO;

    @WireVariable
    private FloorSchemeDAO floorSchemeDAO;

    @WireVariable
    private PhotoDataDAO photoDataDAO;

    @Wire("#frame")
    private Iframe iframe;

    @Wire("#floorSchemeName")
    private Textbox textbox;

    private RealtyObject realtyObject;
    private FloorScheme selectedScheme;
    private String path;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view,
                             @ExecutionArgParam("realtyObject") RealtyObject realtyObject) {

        // Привязка компонентов.
        Selectors.wireComponents(view, this, false);

        // Определение пути к приложению.
        path = Executions.getCurrent().getScheme() + "://" +
                Executions.getCurrent().getServerName() + ":" + Executions.getCurrent().getServerPort() +
                Executions.getCurrent().getContextPath();

        this.realtyObject = realtyObjectDAO.get(realtyObject.getId());

        // Установка начального плана.
        if (this.realtyObject.getFloorScheme() != null) {
            setSelectedScheme(this.realtyObject.getFloorScheme());
        }
    }

    // Передача схем.
    public List<FloorScheme> getFloorSchemes() {
        return floorSchemeDAO.findAll();
    }

    // Загрузка схемы на сервер.
    @Command
    @NotifyChange("floorSchemes")
    public void uploadFloorScheme(@BindingParam("image") AImage image) {

        // Проверка названия плана.
        if (textbox.getValue().isEmpty()) {
            Messagebox.show("Требуется указать название плана", "Ошибка",
                    Messagebox.OK,
                    Messagebox.ERROR);
            return;
        } else if (floorSchemeDAO.findByName(textbox.getValue()) != null) {
            Messagebox.show("План с таким названием уже существует", "Ошибка",
                    Messagebox.OK,
                    Messagebox.ERROR);
            return;
        }

        // Загрузка изображения.
        if (image.getFormat().equals("jpg") || image.getFormat().equals("jpeg")) {
            if (image.getByteData().length > 0) {
                if (image.getByteData().length < MAX_FILE_SIZE) {

                    // Сохранение изображения.
                    PhotoData photoData = new PhotoData();
                    photoData.setData(image.getByteData());
                    photoDataDAO.saveOrUpdate(photoData);

                    // Сохранение плана.
                    FloorScheme scheme = new FloorScheme();
                    scheme.setName(textbox.getValue());
                    scheme.setData(photoData);
                    floorSchemeDAO.saveOrUpdate(scheme);
                    Clients.showNotification("Файл успешно загружен!");

                    // Очистка поля.
                    textbox.setValue("");

                } else {
                    Messagebox.show("Размер файла не должен превышать 10 МБ.",
                            "Ошибка", Messagebox.OK, Messagebox.ERROR);
                }
            }
        } else {
            Messagebox.show("Фаил не является .jpg", "Ошибка",
                    Messagebox.OK,
                    Messagebox.ERROR);
        }
    }

    public FloorScheme getSelectedScheme() {
        return selectedScheme;
    }

    public void setSelectedScheme(FloorScheme selectedScheme) {
        this.selectedScheme = selectedScheme;
        iframe.setSrc(path + "/bind-realty/index.html?realtyObjectId=" + realtyObject.getId()
                + "&floorSchemeId=" + selectedScheme.getId());
    }
}
