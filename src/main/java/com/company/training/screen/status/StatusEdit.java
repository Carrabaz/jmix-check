package com.company.training.screen.status;

import io.jmix.ui.screen.*;
import com.company.training.entity.Status;

@UiController("t_Status.edit")
@UiDescriptor("status-edit.xml")
@EditedEntityContainer("statusDc")
public class StatusEdit extends StandardEditor<Status> {
}