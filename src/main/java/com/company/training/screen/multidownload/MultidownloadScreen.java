package com.company.training.screen.multidownload;

import com.company.training.entity.Files;
import io.jmix.core.DataManager;
import io.jmix.ui.component.Button;
import io.jmix.ui.component.ComboBox;
import io.jmix.ui.download.Downloader;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@UiController("t_MultidownloadScreen")
@UiDescriptor("MultiDownLoad-screen.xml")
public class MultidownloadScreen extends Screen {

    @Autowired
    private ComboBox<Files> filesCombobox;

    @Autowired
    private DataManager dataManager;

    @Autowired
    private Downloader downloader;

    public void setFiles(MapScreenOptions options) {
        List<Files> filesList = dataManager.load(Files.class)
                .query("select f from t_Files f where f.entityName = :name and f.entityId = :id")
                .parameter("name", options.getParams().get("Name"))
                .parameter("id", options.getParams().get("Id"))
                .list();
        filesCombobox.setOptionsList(filesList);
    }

    @Subscribe
    public void onInit(InitEvent event) {
        MapScreenOptions options = (MapScreenOptions) event.getOptions();
        setFiles(options);
    }

    @Subscribe("down")
    public void onDownClick(Button.ClickEvent event) {
        downloader.download(filesCombobox.getValue().getFile());
    }
}