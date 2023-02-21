package com.company.training.screen.servicecompletioncertificate;

import io.jmix.email.EmailInfo;
import io.jmix.email.EmailInfoBuilder;
import io.jmix.email.Emailer;
import io.jmix.ui.Dialogs;
import io.jmix.ui.Notifications;
import io.jmix.ui.action.DialogAction;
import io.jmix.ui.component.Button;
import io.jmix.ui.component.Component;
import io.jmix.ui.component.GroupTable;
import io.jmix.ui.executor.BackgroundTask;
import io.jmix.ui.model.CollectionLoader;
import io.jmix.ui.screen.*;
import com.company.training.entity.ServiceCompletionCertificate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;

@UiController("t_ServiceCompletionCertificate.browse")
@UiDescriptor("service-completion-certificate-browse.xml")
@LookupComponent("serviceCompletionCertificatesTable")
public class ServiceCompletionCertificateBrowse extends StandardLookup<ServiceCompletionCertificate> {
    private static final Logger log = LoggerFactory.getLogger(ServiceCompletionCertificateBrowse.class);
    @Autowired
    private Emailer emailer;
    @Autowired
    private Dialogs dialogs;
    @Autowired
    private GroupTable<ServiceCompletionCertificate> serviceCompletionCertificatesTable;
    @Autowired
    private Notifications notifications;
    @Autowired
    private CollectionLoader<ServiceCompletionCertificate> serviceCompletionCertificatesDl;


    @Subscribe("showRow")
    public void onShowRowClick(Button.ClickEvent event) {
        String description = serviceCompletionCertificatesTable.getSingleSelected().getDescription();
        int index = serviceCompletionCertificatesTable.getTabIndex();
        //notifications.create().withCaption("Info").withDescription(description + " . " + index).show();

        dialogs.createOptionDialog()
                .withCaption("Email")
                .withMessage("Send the news item by email?")
                .withActions(
                        new DialogAction(DialogAction.Type.YES) {
                            @Override
                            public void actionPerform(Component component) {
                                try {
                                    sendByEmail();
                                } catch (IOException e) {
                                    log.error("Error sending email", e);
                                }
                            }
                        },
                        new DialogAction(DialogAction.Type.NO)
                )
                .show();

    }

    private void sendByEmail() throws IOException {
        EmailInfo emailInfo = EmailInfoBuilder.create()
                .setAddresses("mdg@gptrans.gazprom.ru")
                .setSubject("Testing")
                .setFrom(null)
                .setBody("Sentence with point.")
                .build();
        emailer.sendEmailAsync(emailInfo);
    }

}