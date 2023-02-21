package com.company.training.screen.status;

import io.jmix.ui.screen.*;
import com.company.training.entity.Status;

@UiController("t_Status.browse")
@UiDescriptor("status-browse.xml")
@LookupComponent("statusesTable")
public class StatusBrowse extends StandardLookup<Status> {
}