package com.company.training.screen.invoice;

import io.jmix.core.DataManager;
import io.jmix.core.FileRef;
import io.jmix.core.FileStorage;
import io.jmix.core.FileStorageLocator;
import io.jmix.ui.Notifications;
import io.jmix.ui.UiComponents;
import io.jmix.ui.action.BaseAction;
import io.jmix.ui.component.*;
import io.jmix.ui.download.Downloader;
import io.jmix.ui.exception.FileStorageExceptionHandler;
import io.jmix.ui.screen.*;
import com.company.training.entity.Invoice;
import io.jmix.ui.screen.LookupComponent;
import io.jmix.ui.upload.TemporaryStorage;
import org.flowable.bpmn.converter.export.CollaborationExport;
import org.flowable.engine.task.Attachment;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@UiController("t_Invoice.browse")
@UiDescriptor("invoice-browse.xml")
@LookupComponent("invoicesTable")
public class InvoiceBrowse extends StandardLookup<Invoice> {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(InvoiceBrowse.class);
    /*@Autowired
        private FileMultiUploadField fileUploadBtn;*/
    @Autowired
    private TemporaryStorage temporaryStorage;
    @Autowired
    private DataManager dataManager;
    @Autowired
    private UiComponents uiComponents;

    FileStorage s;
    @Autowired
    private FileStorageLocator fileStorageLocator;

    TemporaryStorage f;
    @Autowired
    private FileMultiUploadField fileMultiField;
    @Autowired
    private Notifications notifications;

    Collection<FileRef> files = new ArrayList<>();

    @Subscribe
    public void onInit(InitEvent event) {

        fileMultiField.addQueueUploadCompleteListener(queueUploadCompleteEvent -> {
            for (Map.Entry<UUID, String> entry : fileMultiField.getUploadsMap().entrySet()) {
                UUID fileId = entry.getKey();
                String fileName = entry.getValue();
                FileStorage fileStorage = fileStorageLocator.getDefault();
                log.info("Storage name " + fileStorage.getStorageName());
                FileRef fileRef = temporaryStorage.putFileIntoStorage(fileId, fileName);
                log.info(fileRef.getStorageName() + " >> " + fileRef.getPath());
                files.add(fileRef);
            }
            notifications.create()
                    .withCaption("Uploaded files: " + fileMultiField.getUploadsMap().values())
                    .show();
            //fileMultiField.clearUploads();
        });
        fileMultiField.addFileUploadErrorListener(queueFileUploadErrorEvent ->
                notifications.create()
                        .withCaption("File upload error")
                        .show());

    }

    /*@Subscribe("fileUploadBtn")
    public void onFileUploadBtnQueueUploadComplete(FileMultiUploadField.QueueUploadCompleteEvent event) {

        fileUploadBtn.addQueueUploadCompleteListener(queueUploadCompleteEvent -> {
            for (Map.Entry<UUID, String> entry : fileUploadBtn.getUploadsMap().entrySet()) {
                UUID fileId = entry.getKey();
                String fileName = entry.getValue();

                //System.out.println("default " + );
                FileRef fileRef = temporaryStorage.putFileIntoStorage(fileId, fileName);
                System.out.println(">>" + fileRef.getFileName());
                Attachment attachment = dataManager.create(Attachment.class);
                //attachment.setF

            }
            fileUploadBtn.clearUploads();
        });
    }*/

    @Autowired
    private Downloader downloader;

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

    @Install(to = "invoicesTable.files", subject = "columnGenerator")
    private Component invoicesTableFilesColumnGenerator(Invoice invoice) {
        if (invoice.getFiles() != null) {

            FileStorage fileStorage = fileStorageLocator.getDefault();
            log.info("Button " + fileStorage.getStorageName());
            log.info("size collection " + files.size());
            //log.info("Exists = " + fileStorage.fileExists(fi));

            LinkButton linkButton = uiComponents.create(LinkButton.class);
            /*linkButton.setAction(new BaseAction("download")
                    .withCaption(invoice.getFiles.getFileName())
                    .withHandler(actionPerformedEvent ->
                            downloader.download(invoice.getDoc())
                    )
            );*/
            return linkButton;
        } else {
            FileStorage fileStorage = fileStorageLocator.getDefault();
            log.info("Button " + fileStorage.getStorageName());
            log.info("size collection " + files.size());
            return new Table.PlainTextCell("<empty>");
        }
    }

}