package com.company.training.app;

import com.company.training.entity.Contract;
import com.company.training.entity.CustomSettings;
import com.company.training.entity.Stage;
import io.jmix.appsettings.AppSettings;
import io.jmix.core.DataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Service
public class StageService {

    @Autowired
    private DataManager dataManager;

    @Autowired
    private AppSettings appSettings;

    /**
     * Create default contract stage, if no one added
     *
     * @param editedContract newly created contract
     */
    public void createDefaultStage(Contract editedContract) {
        Stage initialStage = dataManager.create(Stage.class);
        initialStage.setName("Start and End");
        if (Objects.isNull(editedContract.getDateFrom())) {
            initialStage.setDateFrom(LocalDate.now());
        } else {
            initialStage.setDateFrom(editedContract.getDateFrom());
        }
        if (Objects.isNull(editedContract.getDateTo())) {
            initialStage.setDateTo(LocalDate.now());
        } else {
            initialStage.setDateTo(editedContract.getDateTo());
        }
        initialStage.setAmount(editedContract.getAmount());
        initialStage.setVat(BigDecimal.valueOf(initialStage.getAmount())
                .multiply(appSettings.load(CustomSettings.class).getStageVat())
                .multiply(computeCustomerVat(editedContract)));
        initialStage.setTotalAmount(initialStage.getAmount() + initialStage.getVat().intValue());
        initialStage.setDescription("Start till End");
        initialStage.setContract(editedContract);
        dataManager.save(initialStage);
    }

    private BigDecimal computeCustomerVat(Contract customer) {
        boolean escapeVat = (customer.getCustomer().getEscapeVat() == null) ? true : false;
        return escapeVat ? BigDecimal.valueOf(0) : BigDecimal.valueOf(1);
    }
}
