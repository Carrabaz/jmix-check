package com.company.training.screen.organization;

import io.jmix.ui.screen.*;
import com.company.training.entity.Organization;

@UiController("t_Organization.browse")
@UiDescriptor("organization-browse.xml")
@LookupComponent("organizationsTable")
public class OrganizationBrowse extends StandardLookup<Organization> {
}