package com.company.training.screen.status;

import com.company.training.entity.Status;
import io.jmix.ui.screen.LookupComponent;
import io.jmix.ui.screen.StandardLookup;
import io.jmix.ui.screen.UiController;
import io.jmix.ui.screen.UiDescriptor;

@UiController("t_Status.browse")
@UiDescriptor("status-browse.xml")
@LookupComponent("statusesTable")
public class StatusBrowse extends StandardLookup<Status> {
}