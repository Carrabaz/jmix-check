package com.company.training.entity;

import io.jmix.appsettings.defaults.AppSettingsDefault;
import io.jmix.appsettings.entity.AppSettingsEntity;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@JmixEntity
@Table(name = "T_CUSTOM_SETTINGS")
@Entity(name = "t_CustomSettings")
public class CustomSettings extends AppSettingsEntity {

    @InstanceName
    @JmixGeneratedValue
    @Column(name = "UUID")
    private UUID uuid;

    @AppSettingsDefault("0.2")
    @Column(name = "CONTRACT_VAT", precision = 19, scale = 2)
    private BigDecimal contractVat;

    @AppSettingsDefault("0.2")
    @Column(name = "STAGE_VAT", precision = 19, scale = 2)
    private BigDecimal stageVat;

    @AppSettingsDefault("0.2")
    @Column(name = "INVOICE_VAT", precision = 19, scale = 2)
    private BigDecimal invoiceVat;

    @AppSettingsDefault("0.2")
    @Column(name = "CERTIFICATE_VAT", precision = 19, scale = 2)
    private BigDecimal certificateVat;

    public BigDecimal getCertificateVat() {
        return certificateVat;
    }

    public void setCertificateVat(BigDecimal certificateVat) {
        this.certificateVat = certificateVat;
    }

    public BigDecimal getInvoiceVat() {
        return invoiceVat;
    }

    public void setInvoiceVat(BigDecimal invoiceVat) {
        this.invoiceVat = invoiceVat;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public BigDecimal getStageVat() {
        return stageVat;
    }

    public void setStageVat(BigDecimal stageVat) {
        this.stageVat = stageVat;
    }

    public BigDecimal getContractVat() {
        return contractVat;
    }

    public void setContractVat(BigDecimal contractVat) {
        this.contractVat = contractVat;
    }
}