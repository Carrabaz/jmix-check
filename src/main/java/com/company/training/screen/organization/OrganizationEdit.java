package com.company.training.screen.organization;

import io.jmix.ui.screen.*;
import com.company.training.entity.Organization;

@UiController("t_Organization.edit")
@UiDescriptor("organization-edit.xml")
@EditedEntityContainer("organizationDc")
public class OrganizationEdit extends StandardEditor<Organization> {
}