package com.company.training.app;

import org.springframework.stereotype.Component;

@Component
public class VatImpl implements Vat {
    @Override
    public int getVatForInvoice() {
        return 10;
    }

    @Override
    public int getVatForStages() {
        return 15;
    }

    @Override
    public int getVatForContract() {
        return 25;
    }


}
