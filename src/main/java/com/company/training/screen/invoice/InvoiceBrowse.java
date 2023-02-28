package com.company.training.screen.invoice;

import com.company.training.entity.Files;
import com.company.training.entity.Invoice;
import io.jmix.core.DataManager;
import io.jmix.core.FileRef;
import io.jmix.ui.Notifications;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.UiComponents;
import io.jmix.ui.action.BaseAction;
import io.jmix.ui.component.*;
import io.jmix.ui.download.Downloader;
import io.jmix.ui.model.CollectionContainer;
import io.jmix.ui.screen.LookupComponent;
import io.jmix.ui.screen.*;
import io.jmix.ui.upload.TemporaryStorage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@UiController("t_Invoice.browse")
@UiDescriptor("invoice-browse.xml")
@LookupComponent("invoicesTable")
public class InvoiceBrowse extends StandardLookup<Invoice> {

    @Autowired
    private TemporaryStorage temporaryStorage;

    @Autowired
    private DataManager dataManager;

    @Autowired
    private UiComponents uiComponents;

    @Autowired
    private FileMultiUploadField fileMultiField;

    @Autowired
    private Notifications notifications;

    @Autowired
    private ScreenBuilders screenBuilders;

    @Autowired
    private GroupTable<Invoice> invoicesTable;

    @Autowired
    private Downloader downloader;

    @Autowired
    private CollectionContainer<Invoice> invoicesDc;

    @Autowired
    private Form form;

    @Subscribe("invoicesTable")
    public void onInvoicesTableSelection(Table.SelectionEvent<Invoice> event) {
        form.setVisible(true);
    }

    @Subscribe
    public void onInit(InitEvent event) {
        fileMultiField.addQueueUploadCompleteListener(queueUploadCompleteEvent -> {
            for (Map.Entry<UUID, String> entry : fileMultiField.getUploadsMap().entrySet()) {
                UUID fileId = entry.getKey();
                String fileName = entry.getValue();
                FileRef fileRef = temporaryStorage.putFileIntoStorage(fileId, fileName);
                Files files = dataManager.create(Files.class);
                files.setEntityName(invoicesDc.getEntityMetaClass().getName());
                files.setEntityId(invoicesTable.getSingleSelected().getId().toString());
                files.setFile(fileRef);
                files.setFileName(fileName);
                dataManager.save(files);
            }
            notifications.create()
                    .withCaption("Uploaded files: " + fileMultiField.getUploadsMap().values())
                    .show();
        });
        fileMultiField.addFileUploadErrorListener(queueFileUploadErrorEvent ->
                notifications.create()
                        .withCaption("File upload error")
                        .show());
    }

    @Install(to = "invoicesTable.doc", subject = "columnGenerator")
    private Component invoicesTableDocColumnGenerator(Invoice invoice) {
        if (invoice.getDoc() != null) {
            LinkButton linkButton = uiComponents.create(LinkButton.class);
            linkButton.setAction(new BaseAction("download")
                    .withCaption(invoice.getDoc().getFileName())
                    .withHandler(actionPerformedEvent ->
                            downloader.download(invoice.getDoc())
                    )
            );
            return linkButton;
        } else {
            return new Table.PlainTextCell("<empty>");
        }
    }

    @Install(to = "invoicesTable.multiFiles", subject = "columnGenerator")
    private Component invoicesTableMultiFilesColumnGenerator(Invoice invoice) {
        List<Files> filesList = dataManager.load(Files.class)
                .query("select f from t_Files f where f.entityName = :name and f.entityId = :id")
                .parameter("name", invoicesDc.getEntityMetaClass().getName())
                .parameter("id", invoice.getId().toString())
                .list();
        if (filesList.isEmpty()) {
            return new Table.PlainTextCell("<empty>");
        } else {
            LinkButton linkButton = uiComponents.create(LinkButton.class);
            linkButton.setAction(new BaseAction("fileF")
                    .withCaption("Many")
                    .withHandler(actionPerformedEvent -> {
                                Screen s = screenBuilders.screen(this)
                                        .withScreenId("t_MultidownloadScreen")
                                        .withOptions(new MapScreenOptions(Map.of(
                                                "Id", invoice.getId().toString(),
                                                "Name", invoicesDc.getEntityMetaClass().getName())))
                                        .withOpenMode(OpenMode.DIALOG).build();
                                s.show();
                            }
                    )
            );
            return linkButton;
        }
    }
}