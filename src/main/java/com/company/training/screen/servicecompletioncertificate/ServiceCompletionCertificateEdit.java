package com.company.training.screen.servicecompletioncertificate;

import com.company.training.entity.ServiceCompletionCertificate;
import io.jmix.bpmui.processform.annotation.ProcessForm;
import io.jmix.ui.Notifications;
import io.jmix.ui.component.Button;
import io.jmix.ui.download.DownloadFormat;
import io.jmix.ui.download.Downloader;
import io.jmix.ui.screen.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@ProcessForm
@UiController("t_ServiceCompletionCertificate.edit")
@UiDescriptor("service-completion-certificate-edit.xml")
@EditedEntityContainer("serviceCompletionCertificateDc")
public class ServiceCompletionCertificateEdit extends StandardEditor<ServiceCompletionCertificate> {

    private static final Logger log = LoggerFactory.getLogger(ServiceCompletionCertificateEdit.class);

    @Autowired
    private Downloader downloader;

    @Autowired
    private Notifications notifications;

    /**
     * Получение документа Сертификата
     * @param event
     */
    @Subscribe("downloader")
    public void onDownloaderClick(Button.ClickEvent event) {
        if (getEditedEntity().getFiles().length == 0) {
            notifications.create().withCaption("Документ не прикреплён").show();
        } else {
            downloader.download(getEditedEntity().getFiles(), "CertAttachment", DownloadFormat.JPEG);
        }
    }
}