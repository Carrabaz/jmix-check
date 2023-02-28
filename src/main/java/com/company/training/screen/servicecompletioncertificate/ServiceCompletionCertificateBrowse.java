package com.company.training.screen.servicecompletioncertificate;

import com.company.training.entity.ServiceCompletionCertificate;
import io.jmix.ui.screen.LookupComponent;
import io.jmix.ui.screen.StandardLookup;
import io.jmix.ui.screen.UiController;
import io.jmix.ui.screen.UiDescriptor;

@UiController("t_ServiceCompletionCertificate.browse")
@UiDescriptor("service-completion-certificate-browse.xml")
@LookupComponent("serviceCompletionCertificatesTable")
public class ServiceCompletionCertificateBrowse extends StandardLookup<ServiceCompletionCertificate> {
}