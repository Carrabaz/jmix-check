package com.company.training.entity;

import io.jmix.core.metamodel.datatype.impl.EnumClass;

import javax.annotation.Nullable;


public enum ContractType implements EnumClass<Integer> {

    FIXPRICE(10),
    TIMEANDMATERIAL(20),
    OUTSTAFF(30);

    private Integer id;

    ContractType(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static ContractType fromId(Integer id) {
        for (ContractType at : ContractType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}