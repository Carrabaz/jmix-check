package com.company.training.screen.contract;

import io.jmix.ui.screen.*;
import com.company.training.entity.Contract;

@UiController("t_Contract.browse")
@UiDescriptor("contract-browse.xml")
@LookupComponent("contractsTable")
public class ContractBrowse extends StandardLookup<Contract> {
}