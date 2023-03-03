package com.company.training.app;

import com.company.training.entity.Contract;
import io.jmix.search.index.annotation.AutoMappedField;
import io.jmix.search.index.annotation.JmixEntitySearchIndex;

@JmixEntitySearchIndex(entity = Contract.class)
public interface ContractIndex {

    @AutoMappedField(includeProperties = {"customer.name", "performer.name", "number", "totalAmount"})
    void orderMapping();
}
