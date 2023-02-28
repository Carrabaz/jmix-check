package com.company.training.screen.status;

import com.company.training.entity.Status;
import io.jmix.ui.screen.EditedEntityContainer;
import io.jmix.ui.screen.StandardEditor;
import io.jmix.ui.screen.UiController;
import io.jmix.ui.screen.UiDescriptor;

@UiController("t_Status.edit")
@UiDescriptor("status-edit.xml")
@EditedEntityContainer("statusDc")
public class StatusEdit extends StandardEditor<Status> {
}