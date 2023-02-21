package com.company.training.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.Composition;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@JmixEntity
@Table(name = "T_CONTRACT", indexes = {
        @Index(name = "IDX_T_CONTRACT_CUSTOMER", columnList = "CUSTOMER_ID"),
        @Index(name = "IDX_T_CONTRACT_PERFORMER", columnList = "PERFORMER_ID"),
        @Index(name = "IDX_T_CONTRACT_STATUS", columnList = "STATUS_ID")
})
@Entity(name = "t_Contract")
public class Contract {

    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @JoinColumn(name = "CUSTOMER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Organization customer;

    @JoinColumn(name = "PERFORMER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Organization performer;

    @Column(name = "NUMBER_")
    private Long number;

    @Column(name = "SIGNED_DATE")
    private LocalDate signedDate;

    @Column(name = "TYPE_")
    private Integer type;

    @Column(name = "DATE_FROM")
    private LocalDate dateFrom;

    @Column(name = "DATE_TO")
    private LocalDate dateTo;

    @Column(name = "AMOUNT")
    private Integer amount;

    @Column(name = "VAT", precision = 19, scale = 2)
    private BigDecimal vat;

    @Column(name = "TOTAL_AMOUNT")
    private Integer totalAmount;

    @Column(name = "CUSTOMER_SIGNER")
    private String customerSigner;

    @Column(name = "PERFORMER_SIGNER")
    private String performerSigner;

    @JoinColumn(name = "STATUS_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Status status;

    @Composition
    @OneToMany(mappedBy = "contract")
    private List<Stage> stages;

    public List<Stage> getStages() {
        return stages;
    }

    public void setStages(List<Stage> stages) {
        this.stages = stages;
    }

    public void setVat(BigDecimal vat) {
        this.vat = vat;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getPerformerSigner() {
        return performerSigner;
    }

    public void setPerformerSigner(String performerSigner) {
        this.performerSigner = performerSigner;
    }

    public String getCustomerSigner() {
        return customerSigner;
    }

    public void setCustomerSigner(String customerSigner) {
        this.customerSigner = customerSigner;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getVat() {
        return vat;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public ContractType getType() {
        return type == null ? null : ContractType.fromId(type);
    }

    public void setType(ContractType type) {
        this.type = type == null ? null : type.getId();
    }

    public LocalDate getSignedDate() {
        return signedDate;
    }

    public void setSignedDate(LocalDate signedDate) {
        this.signedDate = signedDate;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Organization getPerformer() {
        return performer;
    }

    public void setPerformer(Organization performer) {
        this.performer = performer;
    }

    public Organization getCustomer() {
        return customer;
    }

    public void setCustomer(Organization customer) {
        this.customer = customer;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @InstanceName
    public String contractName() {
        return "< " + getNumber() + " " + getCustomer().getName() + " >";
    }
}