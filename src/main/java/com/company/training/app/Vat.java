package com.company.training.app;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public interface Vat {
    int getVatForInvoice();

    int getVatForStages();

    int getVatForContract();
}
