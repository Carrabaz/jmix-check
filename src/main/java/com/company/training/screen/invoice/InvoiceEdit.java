package com.company.training.screen.invoice;

import io.jmix.core.DataManager;
import io.jmix.core.FileRef;
import io.jmix.core.event.EntityChangedEvent;
import io.jmix.ui.Notifications;
import io.jmix.ui.component.FileMultiUploadField;
import io.jmix.ui.component.FileStorageUploadField;
import io.jmix.ui.component.SingleFileUploadField;
import io.jmix.ui.component.UploadField;
import io.jmix.ui.screen.*;
import com.company.training.entity.Invoice;
import io.jmix.ui.upload.TemporaryStorage;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.Map;
import java.util.UUID;

@UiController("t_Invoice.edit")
@UiDescriptor("invoice-edit.xml")
@EditedEntityContainer("invoiceDc")
public class InvoiceEdit extends StandardEditor<Invoice> {
    @Autowired
    private FileMultiUploadField fileMultiUploadField;
    @Autowired
    private FileStorageUploadField manuallyControlledField;
    @Autowired
    private TemporaryStorage temporaryStorage;
    @Autowired
    private Notifications notifications;
    @Autowired
    private DataManager dataManager;

    @Subscribe
    public void onInit(InitEvent event) {

        fileMultiUploadField.addQueueUploadCompleteListener(queueUploadCompleteEvent -> {
            for (Map.Entry<UUID, String> entry : fileMultiUploadField.getUploadsMap().entrySet()) {
                UUID fileId = entry.getKey();
                String fileName = entry.getValue();
                FileRef fileRef = temporaryStorage.putFileIntoStorage(fileId, fileName);
            }
            notifications.create()
                    .withCaption("Uploaded files: " + fileMultiUploadField.getUploadsMap().values())
                    .show();
            fileMultiUploadField.clearUploads();
        });
        fileMultiUploadField.addFileUploadErrorListener(queueFileUploadErrorEvent ->
                notifications.create()
                        .withCaption("File upload error")
                        .show());
    }

    @Subscribe("manuallyControlledField")
    public void onManuallyControlledFieldFileUploadSucceed(SingleFileUploadField.FileUploadSucceedEvent event) {
        File file = temporaryStorage.getFile(manuallyControlledField.getFileId());
        if (file != null) {
            notifications.create()
                    .withCaption("File is uploaded to temporary storage at " + file.getAbsolutePath())
                    .show();
        }

        FileRef fileRef = temporaryStorage.putFileIntoStorage(manuallyControlledField.getFileId(), event.getFileName());
        manuallyControlledField.setValue(fileRef);
        notifications.create()
                .withCaption("Uploaded file: " + manuallyControlledField.getFileName())
                .show();
    }

    @Subscribe("manuallyControlledField")
    public void onManuallyControlledFieldFileUploadError(SingleFileUploadField.FileUploadErrorEvent event) {
        notifications.create()
                .withCaption("File upload error")
                .show();
    }
}